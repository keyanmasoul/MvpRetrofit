package corall.base.mvp;

import android.content.Intent;
import android.os.Message;


public interface IBasePresenter<V extends IBaseProxyView> {

    BaseMVPActivity host();

    V view();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onNewIntent(Intent intent);


    void initData();

    void initDataAfterView();

    void subHandleMessage(Message msg);

    void onBackPressed();

    void onActivityResult(int requestCode, int resultCode, Intent intent);

}
