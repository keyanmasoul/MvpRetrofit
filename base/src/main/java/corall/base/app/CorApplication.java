package corall.base.app;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Message;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import corall.base.bean.GlobalMessageEvent;
import corall.base.bean.MessageEvent;
import corall.base.util.AppInfoUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 全局应用环境抽象类
 * <p/>
 */
public abstract class CorApplication<B extends CorBeanManager> extends Application {

    public static final String TAG = CorApplication.class.getSimpleName();

    // 全局唯一本類實例
    private static CorApplication corApplication;

    // 最后一次更新时间
    protected volatile long lastInitTimestamp;
    // Mob模块的Bean对象管理器
    protected B mBeanManager;
    // 基础数据是否初始化正常，如果基础数据不正常，那么不应该启动。
    private boolean baseDataOk = false;
    // 当前使用的 window 环境，用于一些全局处理。
    private Context mWindowToken;

    /**
     * 获得实例，用于那些无法直接获取全局环境的地方。
     *
     * @return
     */
    public static CorApplication getInstance() {
        return corApplication;
    }


    /**
     * 主application没有继承自AMApplication时，直接调用这个方法初始化，如果已经继承了该类，则无需调用该方法
     *
     * @param application
     */
    public void init(Application application) {
        try {
            //手动把系统context代理到这个application里
            attachBaseContext(application.getBaseContext());

            if (corApplication == null) {
                corApplication = this;
            }

            // 预初始化
            preInitApplication();

            // 实际初始化
            initMApplication();

            // 数据预加载
            initMobPreferData();

            baseDataOk = true;


        } catch (Exception e) {
            e.printStackTrace();
            baseDataOk = false;
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            corApplication = this;
            // 预初始化
            preInitApplication();

            // 实际初始化
            initMApplication();

            // 数据预加载
            initMobPreferData();

            baseDataOk = true;


        } catch (Exception e) {
            e.printStackTrace();
            baseDataOk = false;
        }

    }

    /**
     * 提供给子类覆盖onLowMemory()方法
     */
    protected void doOnLowMemory() {

    }


    public void clear() {

        // 清除子模块资源
        try {
            getModuleManager().releaseModules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public final void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doOnConfigurationChanged();
    }

    /**
     * 提供给子类覆盖onConfigurationChanged()方法
     */
    protected void doOnConfigurationChanged() {

    }

    /**
     * 初始化系统需要的数据
     *
     * @throws Exception
     */
    protected void initMApplication() throws Exception {


        lastInitTimestamp = System.currentTimeMillis();

        // Mob模块的Bean管理器初始化，要在所有实例前进行
        mBeanManager = createBeanManager();

        EventBus.getDefault().register(this);
        // 初始化基础数据
        initContextConfig();


        // 初始化配置
        initMobConfig();

        // 注册定时器处理
        initAlarmManager();

        // 扩展模块管理
        initSubModules();

        AppInfoUtil.registerActivityLifecycleCallbacks(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleGlobalMessage(GlobalMessageEvent event) {
        Log.d("handleGlobalMessage", event.toString());
    }

    /**
     * 得到Bean管理器
     *
     * @return
     */
    protected B getBeanManager() {
        return mBeanManager;
    }

    public ModuleManager getModuleManager() {
        return mBeanManager.getModuleManager();
    }

    /**
     * 注册定时器处理
     *
     * @return
     */
    protected AlarmManager initAlarmManager() {
        // 注册定时更新
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setPackage(getPackageName());

        return alarmManager;
    }


    /**
     * 执行必要的数据的预处理
     */
    protected void initMobPreferData() {

    }

    /**
     * 最后一次初始化的实际
     *
     * @return the lastInitTimestamp
     */
    public long getLastInitTimestamp() {
        return lastInitTimestamp;
    }


    // 初始化模块
    private void initSubModules() throws Exception {
        ModuleManager moduleManager = getModuleManager();
        if (getModulesConfig() == null) {
            return;
        }
        ArrayList<ModuleConfig> list = getModulesConfig();
        for (ModuleConfig config : list) {
            moduleManager.registerModule(config.getModuleName(), config);
        }

    }

    /**
     * 长时间存活是初始化相关数据
     *
     * @param force 是否强制
     */
    public boolean checkForLongLive(boolean force) {
        long currentTimestamp = System.currentTimeMillis();


        if (force || currentTimestamp - lastInitTimestamp >= 1000 * 60 * 15) {


            lastInitTimestamp = currentTimestamp;

            // 初始其他模块
            Map<String, AModule> moduleViewMap = getModuleManager().getModuleMapView();
            for (Map.Entry<String, AModule> entry : moduleViewMap.entrySet()) {
                try {
                    entry.getValue().initForLongLive();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;

        } else {
            return false;
        }
    }


    public Context getMWindowToken() {
        return mWindowToken;
    }

    public void setMWindowToken(Context mWindowToken) {
        this.mWindowToken = mWindowToken;
    }


    public AModule getSubModule(String moduleMark) {
        return getModuleManager().getModule(moduleMark);
    }

    public boolean isBaseDataOk() {
        return baseDataOk;
    }

    /**
     * 预初始化，这里做些需要提前处理的东西
     */
    protected void preInitApplication() {
        Hawk.init(this).build();
    }


    /**
     * 延时gc，资源释放
     *
     * @param msg
     */
    protected void releaseForExist(Message msg) {

    }

    //////////////////////////// <抽象方法的定义> ///////////////////////////

    /**
     * 创建Bean管理器
     */
    protected abstract B createBeanManager();



    /**
     * 处理基础数据的初始化
     * 不必须每次都初始化所有，但要注意哪些是可能改变的。
     *
     * @throws Exception
     */
    protected abstract void initContextConfig() throws Exception;

    /**
     * 初始化配置
     */
    protected abstract void initMobConfig() throws Exception;


    /**
     * 获得模块配置
     *
     * @return
     */
    public abstract ArrayList<ModuleConfig> getModulesConfig();


    public void sendEmptyMessage(int what, long delay) {
        MessageEvent event = new MessageEvent();
        event.setWhat(what);
        EventBus.getDefault().post(event);
    }

    public void sendMessage(MessageEvent messageEvent) {
        EventBus.getDefault().post(messageEvent);
    }

    public void sendMessageDelay(final MessageEvent messageEvent, long delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                EventBus.getDefault().post(messageEvent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void sendEmptyMessageDelay(final int what, long delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Long aLong) {
                MessageEvent event = new MessageEvent();
                event.setWhat(what);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }


}
