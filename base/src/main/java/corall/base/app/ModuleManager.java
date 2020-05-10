package corall.base.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 模块管理器
 * Created by linlin_91 on 2015/6/24.
 *
 * 修改：on 2017/8/15
 * 支持存储不同模块的 imContext
 * imContext 的存储以map的形式存放
 * key 为各自模块中 *Module中的 COMMENT_MODULE 字段
 * 兼容支持旧版，旧版的 key 为：DEFAULT_IMCONTEXT
 *
 */
public class ModuleManager {

    public static final String TAG = ModuleManager.class.getSimpleName();

    private Map<String, CorApplication> mContentMap = new HashMap<>();

    /**
     * 如果没有传 moduleMark 就使用默认标识
     */
    public static final String DEFAULT_IMCONTEXT = "default_imContext";

    // 模块实现列表
    private Map<String, Object> moduleMap = new HashMap<String, Object>();

    // 用于缓存构造同步
    private ReadWriteLock rwlock = new ReentrantReadWriteLock(false);

    /**
     * 没有设置 moduleMark 则使用给予一个默认的 moduleMark
     * 兼容早起版本写法。
     * @param imContext
     */
    public ModuleManager(CorApplication imContext) {
        putContentByModuleMark(imContext, DEFAULT_IMCONTEXT);
    }


    /**
     * 设置 moduleMark 对应的 imContext
     * 这是个 map 键值对 moduleMark 为key
     * @param imContext
     * @param moduleMark 模块标志
     */
    public void putContentByModuleMark(CorApplication imContext, String moduleMark) {
        mContentMap.put(moduleMark, imContext);

    }

    /**
     * 根据 moduleMark 获取对应的 imContext
     *
     * @param moduleMark 模块标志
     * @return
     */
    private CorApplication getContentByModuleMark(String moduleMark) {
        CorApplication imContext = mContentMap.get(moduleMark);
        //兼容旧版写法
        if (imContext == null) {
            imContext = mContentMap.get(DEFAULT_IMCONTEXT);
        }

        return imContext;

    }

    /**
     * 这里只返回已经初始化的模块
     *
     * @return
     */
    public Map<String, AModule> getModuleMapView() {
        Map<String, AModule> moduleViewMap = new HashMap<String, AModule>();
        for (Map.Entry<String, Object> entry : moduleMap.entrySet()) {
            if (entry.getValue() instanceof AModule) {
                moduleViewMap.put(entry.getKey(), (AModule) entry.getValue());
            }
        }
        return moduleViewMap;
    }

    /**
     * 注册模块
     *
     * @param moduleMark 模块标示
     */
    public void registerModule(String moduleMark, ModuleConfig mConfig) {
        rwlock.writeLock().lock();
        try {

            moduleMap.put(moduleMark, mConfig);
            // 如果没有延迟加载，则直接初始化模块
            if (!mConfig.isDelay()) {
                getContentByModuleMark(moduleMark).getSubModule(moduleMark);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            rwlock.writeLock().unlock();
        }

    }

    /**
     * 取消模块注册
     *
     * @param moduleMark 模块标示
     */
    public void unregisterModule(String moduleMark) {
        rwlock.writeLock().lock();
        try {

            Object obj = moduleMap.remove(moduleMark);
            if (obj instanceof AModule) {
                ((AModule) obj).releaseModule();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            rwlock.writeLock().unlock();
        }

    }

    /**
     * 释放所有模块，主要用于退出时资源释放
     */
    public void releaseModules() {
        rwlock.writeLock().lock();
        try {

            Collection cacheData = moduleMap.values();
            for (Object obj : cacheData) {
                if (obj instanceof AModule) {
                    ((AModule) obj).releaseModule();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            rwlock.writeLock().unlock();
        }

    }

    /**
     * 获得模块
     *
     * @param moduleMark 模块标示
     */
    public AModule getModule(String moduleMark) {
        rwlock.readLock().lock();
        AModule module = null;
        try {
            Object obj = moduleMap.get(moduleMark);
            if (obj != null) {
                if (obj instanceof AModule) {
                    module = (AModule) obj;

                } else if (obj instanceof ModuleConfig) {
                    rwlock.readLock().unlock();
                    rwlock.writeLock().lock();
                    try {
                        // 加载当前模块及其依赖模块
                        module = loadModAndDependMods(moduleMark);

                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        rwlock.readLock().lock();
                        rwlock.writeLock().unlock();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            rwlock.readLock().unlock();
        }
        return module;
    }

    /**
     * 加载当前模块及其依赖的模块
     * 如果已存在模块及认为其所依赖的模块都已加载了
     *
     * @param moduleMark 当前模块的模块名称
     * @return 当前模块
     */
    private AModule loadModAndDependMods(String moduleMark) throws Exception {
        Object obj = moduleMap.get(moduleMark);
        AModule module = null;
        if (obj != null) {
            // 当加载当前模块时，这里要多次判断，避免再次进入的时候已经被初始化过了。
            if (obj instanceof AModule) {
                module = (AModule) obj;

            } else if (obj instanceof ModuleConfig) {
                // 加载依赖模块
                ModuleConfig mConfig = (ModuleConfig) obj;
                for (String dependMark : mConfig.getDependModules()) {
                    loadModAndDependMods(dependMark);
                }

                // 加载模块
                CorApplication imContext = getContentByModuleMark(moduleMark);
                module = doBuildModule(imContext, moduleMark, mConfig.getModule());
                if (module != null) {
                    // 把加载的模块存到模块映射中
                    moduleMap.put(moduleMark, module);
                }
            }
        }

        return module;
    }

    /**
     * 创建模块
     *
     * @param amApplication
     * @param key           缓存标示
     * @param aModule     缓存实现完整类
     * @return
     */
    private AModule doBuildModule(CorApplication amApplication, String key, AModule aModule) throws Exception {
        if(aModule!=null) {
            // 创建模块
            aModule.buildModule();
        }

        return aModule;
    }


}
