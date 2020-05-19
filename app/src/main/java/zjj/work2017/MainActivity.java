package zjj.work2017;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import corall.adscene.EntranceType;
import corall.base.BaseActivity;
import corall.base.bean.AdEvent;
import corall.base.app.CorApplication;
import corall.base.bean.DownloadEvent;
import corall.base.bean.GlobalMessageEvent;
import corall.base.bean.MessageEvent;
import corall.base.task.CorTask;
import corall.base.task.CorTaskSign;
import corall.base.task.ICorTaskResult;
import corall.base.util.DownloadUtil;

public class MainActivity extends BaseActivity implements ICorTaskResult {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void checkContext() {

    }


    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmptyMessageDelay(2, 2000);
            }
        });

        findViewById(R.id.btn_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTest();
            }
        });

        findViewById(R.id.btn_test3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTest();
            }
        });

        findViewById(R.id.btn_test4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGlobalMessage();
            }
        });

        findViewById(R.id.btn_test5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadInterstitalAd();
            }
        });
        if (((App) CorApplication.getInstance()).getAdsModule().isSdkReady()) {
            findViewById(R.id.btn_test5).setEnabled(true);
        } else {
            findViewById(R.id.btn_test5).setEnabled(false);
        }
    }

    private void sendGlobalMessage() {
        EventBus.getDefault().post(new GlobalMessageEvent());
    }

    private void loadInterstitalAd() {
        EntranceType.TRIGGER.load();
    }

    private void asyncTest() {
        new TestCorTask(new TestCorTaskSign(), this).execute();
    }

    private void downloadTest() {
        DownloadUtil.start(getBaseContext(), "http://media.supercall.xyz/media/10002.mp4", 10002);
    }

    @Override
    protected void subHandleMessage(MessageEvent messageEvent) {
        switch (messageEvent.getWhat()) {
            case 2:
                TextView textView = findViewById(R.id.tv_text);
                textView.setText("post event success!");
                break;
        }
    }

    @Override
    protected void receiveTaskResult(CorTaskSign corTaskSign) {
        if (corTaskSign instanceof TestCorTaskSign) {
            TextView textView = findViewById(R.id.tv_text);
            if (corTaskSign.getTaskStatus() == CorTask.TASK_STATUS_PASS) {
                textView.setText("asyncTest success!");
            }
        }
    }

    @Override
    protected void receiveAdEvent(int eventId, String name) {
        if (eventId == R.id.poster_msg_ad_sdk_init_finish) {
            findViewById(R.id.btn_test5).setEnabled(true);
        } else if (eventId == R.id.poster_msg_ad_interstitial_loaded && name.equals(EntranceType.TRIGGER.getName())) {
            EntranceType.TRIGGER.show();
        }
    }

    @Override
    public void subHandleDownloadMessage(DownloadEvent event) {
        String downloadStr = "current = " + event.getCurrentOffset() + " speed = " +
                event.getSpeedCalculator().getInstantBytesPerSecondAndFlush();
        TextView textView = findViewById(R.id.tv_text);
        textView.setText(downloadStr);
    }

    @Override
    public void onError(String errorMessage) {
        TextView textView = findViewById(R.id.tv_text);
        textView.setText(errorMessage);
    }

    @Override
    public void onComplete(int taskStatus) {

    }
}
