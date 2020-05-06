package zjj.work2017;

import android.app.Application;

import corall.base.HttpHelper;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initNetWork();
    }

    private void initNetWork() {
        HttpHelper.HelperParam param = new HttpHelper.HelperParam("http://gt.supercall.xyz");
        HttpHelper.getInstance().setDebug(true);
        HttpHelper.getInstance().init(param);
    }

}
