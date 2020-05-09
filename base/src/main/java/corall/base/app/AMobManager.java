package corall.base.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 软件管理主类 <软件不包括内置软件><br>
 * 对软件的操作(add delete 方法)要注意对加载过来的AppItem的状态更新
 *
 * Created by linlin_91 on 2015/6/23.
 */
public abstract class AMobManager<A extends AMApplication> {

    public static final String TAG = AMobManager.class.getSimpleName();

    // SD卡写测试是否可写
    private boolean isSDcardCanWrite = false;
    // SD卡写测试最近一次测试时间
    private long lastSDcardTestWriteTime = 0L;
    // SD卡写测试间隔
    public static final long SDCARD_WRITE_TEST_INTERVEL = 10000L;
    // SD卡写测试文件名
    public static final String SDcardTestWriteFileName = "testFile";

    protected A imContext;

    public AMobManager(A mApplication) {
        this.imContext = mApplication;

    }


    /**
     * 获取自身版本号
     * @return
     */
    public int getSelfVersionCode(){
        int vCode = 9999;
        try{
            PackageManager pManager = imContext.getPackageManager();
            PackageInfo pIinfo = pManager.getPackageInfo(imContext.getPackageName(), 0);
            vCode = pIinfo.versionCode;

        }catch (Exception e){
            e.printStackTrace();

        }

        return vCode;

    }

}
