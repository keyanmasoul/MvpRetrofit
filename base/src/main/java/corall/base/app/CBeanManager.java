package corall.base.app;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean对象管理器，主要用于延时初始化（用的时候才初始化）
 * <p/>
 * Created by linlin_91 on 2016/02/26.
 */
public abstract class CBeanManager {

    /**
     * 存放单例对象
     */
    protected static final ConcurrentHashMap<String, Object> singletonInstanceMap = new ConcurrentHashMap<String,
            Object>(200, 0.75f, 1);

    protected CorApplication imContext;

    public CBeanManager(CorApplication imContext) {
        this.imContext = imContext;

    }

    protected abstract <T> T createBean(Class<T> clazz, String mark);

    /**
     * 获取单例对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T getBean(Class<T> clazz) {
        return getBean(clazz, "");
    }

    protected <T> T getBean(Class<T> clazz, String mark) {
        String key = clazz.getName() + mark;
        Object instance = singletonInstanceMap.get(key);
        if (instance == null) {
            // 同步代码块内还要再进行一次非空判断
            synchronized (clazz) {
                instance = singletonInstanceMap.get(key);
                if (instance == null) {
                    instance = createBean(clazz, mark);
                    singletonInstanceMap.putIfAbsent(key, instance);
                }
            }

        }

        return (T) instance;

    }


}
