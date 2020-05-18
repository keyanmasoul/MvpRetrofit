package zjj.work2017;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import corall.base.BaseActivity;
import corall.base.bean.DownloadEvent;
import corall.base.bean.GlobalMessageEvent;
import corall.base.bean.MessageEvent;
import corall.base.bean.TaskEvent;
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
    }

    private void sendGlobalMessage() {
        EventBus.getDefault().post(new GlobalMessageEvent());
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
