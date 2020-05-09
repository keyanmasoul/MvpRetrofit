package corall.base.app;

import android.content.Intent;
import android.os.Message;

/**
 * 一个模块的封转
 * Created by linlin_91 on 2015/6/24.
 */
public abstract class AModule {

    // 应用类型
    protected AMApplication imContext;
    // 缓存标示
    protected String moduleMark;
    // 是否已经初始化了
    private volatile boolean inited;

    public AModule(AMApplication context, String moduleMark) {
        this.imContext = context;
        this.moduleMark = moduleMark;

    }

    /**
     * 创建模块
     *
     * @return
     */
    public final AModule buildModule() throws Exception {
        if (inited) {
            throw new IllegalStateException("init module: " + moduleMark + " twice ?");
        }

        inited = true;

        // 注册处理消息处理器


        // 初始化环境相关
        doInitContext();

        // 实际的创建
        doBuildModule();

        // 初始化子模块业务相关
        doInitModule();

        return this;
    }


    public void startService(Intent intent) {
        imContext.startService(intent);
    }


    /**
     * 在模块被取消注册是使用
     */
    public void releaseModule() {

    }

    /**
     * 通过给定的参数生成与模块相关的唯一的id
     *
     * @param args
     * @return
     */
    public long getUniqueId(String... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg);
        }
        return (getClass().getName() + stringBuilder.toString()).hashCode();
    }

    //////////////////////// <抽象方法定义>  ////////////////////////

    /**
     * 注销初始化
     */
    public abstract void initForLoginOut();

    /**
     * 长时间存活初始化
     */
    public abstract void initForLongLive();

    /**
     * 初始化环境相关
     *
     * @throws Exception
     */
    protected abstract void doInitContext() throws Exception;

    /**
     * 模块实际的创建
     *
     * @throws Exception
     */
    protected abstract void doBuildModule() throws Exception;

    /**
     * 初始化子模块，业务关联
     */
    protected abstract void doInitModule() throws Exception;

    /**
     * 日志标签
     *
     * @return
     */
    public abstract String TAG();


    /**
     * 具体消息处理
     *
     * @param msg
     */
    protected abstract void subHandleMessage(Message msg);

    //////////////////////// <抽象方法定义>  ////////////////////////

}
