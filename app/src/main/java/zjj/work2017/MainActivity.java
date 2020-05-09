package zjj.work2017;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import corall.base.BaseActivity;
import corall.base.bean.DownloadEvent;
import corall.base.bean.MessageEvent;
import corall.base.util.DownloadUtil;

public class MainActivity extends BaseActivity {

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
    }

    private void downloadTest() {
        DownloadUtil.start(getBaseContext(),"http://media.supercall.xyz/media/10002.mp4",10002);
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
    public void subHandleDownloadMessage(DownloadEvent event) {
        Log.e("progress==", event.getCurrentOffset() + "");
    }

}
