package corall.base.mvp;

import android.content.Intent;
import android.os.Message;


import java.util.List;

import corall.base.app.CorApplication;

/**
 * Created by dell on 2018/10/26.
 */

public abstract class BasePresenter<V extends BaseProxyView> implements IBasePresenter {

    protected CorApplication imContext;

    private V mView;

    private BaseMVPActivity mHost;

    public BasePresenter(BaseMVPActivity baseActivity) {
        imContext = CorApplication.getInstance();
        mHost = baseActivity;
    }

    public void attach(V view) {
        mView = view;
    }

    public void detach() {
        if (mView != null) {
            mView.detachView();
            mView = null;
        }
        mHost = null;
        imContext = null;
    }

    @Override
    public V view() {
        return mView;
    }

    @Override
    public BaseMVPActivity host() {
        return mHost;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {
        if (mView != null) {
            mView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mView != null) {
            mView.onPause();
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void initDataAfterView() {

    }

    @Override
    public void subHandleMessage(Message msg) {

    }

    @Override
    public void onBackPressed() {
        if (view() != null) {
            view().onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }

    protected void onCompatPermissionGranted(List<String> grantedPermissions, int requestCode) {
    }

    protected void onCompatPermissionDenied(List<String> grantedPermissions, int requestCode) {
    }
}

