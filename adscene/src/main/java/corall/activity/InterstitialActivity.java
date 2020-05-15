package corall.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import corall.adscene.EntranceType;


/**
 * desc: 模拟 插屏广告（将native、banner等广告以插屏的形式展示）
 * date: 2018/11/12
 * author: ancun
 */
public abstract class InterstitialActivity extends Activity {

    protected static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    protected static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    protected static final String ENTRANCE_TYPE = "entranceType";

    protected BroadcastReceiver mReceiver;
    protected EntranceType mEntranceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
    }

    protected void initHomeKeyReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action) && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                    if (reason != null && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        close();
                    }
                } else if (!TextUtils.isEmpty(action) && action.equals(Intent.ACTION_SCREEN_OFF)) {
                    close();
                }
            }
        };

        IntentFilter homeFilter = new IntentFilter();
        homeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        homeFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, homeFilter);
    }

    public abstract void close();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//            if (mEntranceType != null) {
//                // 针对现有的广告缓存，部分取消release
//                if (!mEntranceType.getName().equals(EntranceType.WEB_SITE_PAGE.getName()) ||
//                        !mEntranceType.getName().equals(EntranceType.PLAY_VIDEO.getName())
//                ) {
//                    mEntranceType.release();
//                    mEntranceType = null;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
