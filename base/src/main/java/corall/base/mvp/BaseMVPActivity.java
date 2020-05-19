package corall.base.mvp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import cor.base.R;
import corall.base.app.CorApplication;

abstract public class BaseMVPActivity<P extends BasePresenter, V extends BaseProxyView> extends
        AppCompatActivity {

    protected CorApplication imContext;
    protected P mPresenter;

    protected abstract P initPresenter();

    protected abstract V initProxyView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imContext = CorApplication.getInstance();
        imContext.setMWindowToken(this);

        mPresenter = initPresenter();
        V proxyView = initProxyView();
        setContentView(proxyView);

        mPresenter.attach(proxyView);
        proxyView.attach(mPresenter);

        mPresenter.initData();
        proxyView.initView(savedInstanceState);
        mPresenter.initDataAfterView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        CorApplication.getInstance().setMWindowToken(this);
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        CorApplication.getInstance().setMWindowToken(null);
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter.detach();
            mPresenter = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPresenter != null) {
            mPresenter.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mPresenter != null) {
            mPresenter.onNewIntent(intent);
        }
    }

    protected static final int REQUEST_PERMISSION_CODE_DEFAULT = 5001;

    /**
     * Request permissions.
     */
    public void requestPermission(int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    public void requestPermission(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE_DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0) {
            List<String> deniedPermissions = new ArrayList<>();
            List<String> grantedPermissions = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permissions[i]);
                } else {
                    deniedPermissions.add(permissions[i]);
                }
            }
            onCompatPermissionGranted(grantedPermissions, requestCode);
            onCompatPermissionDenied(deniedPermissions, requestCode);
        }
    }

    protected void onCompatPermissionGranted(List<String> grantedPermissions, int requestCode) {
        mPresenter.onCompatPermissionGranted(grantedPermissions, requestCode);
    }

    protected void onCompatPermissionDenied(List<String> deniedPermissions, int requestCode) {
        mPresenter.onCompatPermissionDenied(deniedPermissions, requestCode);
    }

    ///////////////////////////////  以下是启动或关闭Activity的代码  ///////////////////////////////

    @Override
    public void finish() {
        super.finish();
        backOverridePendingTransition();
    }

    public void startActivityWithAnim(Intent intent) {
        startActivity(intent);
        //startOverridePendingTransition();
    }

    public void startActivityWithAnim(Class pointClass) {
        Intent intent = new Intent(this, pointClass);
        startActivity(intent);
        //startOverridePendingTransition();
    }

    public void startActivityWithAnim(Class pointClass, int requestCode) {
        Intent intent = new Intent(this, pointClass);
        startActivityForResult(intent, requestCode);
        //startOverridePendingTransition();
    }

    public void startActivityWithAnim(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
        //startOverridePendingTransition();
    }

    protected final void startOverridePendingTransition() {
        super.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    protected final void backOverridePendingTransition() {
        super.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    ///////////////////////////////  以下是消息框架的代码  ///////////////////////////////

    /**
     * 发送消息
     *
     * @param message
     */

    /**
     * 发送定时消息
     *
     * @param message
     */

    /**
     * 发送延时消息
     *
     * @param message
     * @param delay
     */

    /**
     * 发送空消息
     *
     * @param what
     */

    /**
     * 发送延时空消息
     *
     * @param what
     * @param delay
     */

    /**
     * 移除某个消息
     *
     * @param what
     */

    /**
     * 子类处理对应的消息
     *
     * @param msg
     */

    /**
     * 主界面消息处理器，希望它存在的时候处理所有可能的消息
     */

    public static void printLog(@NonNull String tag, @NonNull String message) {
        Log.e(tag, message);
    }

}
