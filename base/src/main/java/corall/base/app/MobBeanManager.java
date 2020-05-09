package corall.base.app;

/**
 * Mob模块的Bean对象管理器
 * Created by linlin_91 on 2015/6/17.
 */
public class MobBeanManager extends ABeanManager {

    public MobBeanManager(AMApplication imContext) {
        super(imContext);
    }

    @Override
    protected <T> T createBean(Class<T> clazz, String mark) {
        Object obj = null;
        if (clazz == ModuleManager.class) {
            // 模块管理器用于相关扩展模块
            obj = new ModuleManager(imContext);

        }

        return (T) obj;
    }


    /**
     * 获取模块管理器
     *
     * @return
     */
    public ModuleManager getModuleManager() {

        return getBean(ModuleManager.class);
    }

    /**
     * 获取模块管理器 增加不同模块标记
     * SDK专用写法
     * @param imContext   不同模块的 application
     * @param moduleMark  不同模块的标记key
     * @return
     */
    public ModuleManager getModuleManager(AMApplication imContext, String moduleMark){
        ModuleManager moduleManager = getBean(ModuleManager.class);
        moduleManager.putContentByModuleMark(imContext, moduleMark);

        return moduleManager;

    }

}
