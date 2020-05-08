package corall.base.bean;

import com.liulishuo.okdownload.SpeedCalculator;

import zjj.network.R;


public class DownloadEvent extends MessageEvent {
    private long currentOffset;
    private SpeedCalculator speedCalculator;

    public DownloadEvent() {
        this.setWhat(R.id.msg_download_event);
    }

    public long getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(long currentOffset) {
        this.currentOffset = currentOffset;
    }

    public SpeedCalculator getSpeedCalculator() {
        return speedCalculator;
    }

    public void setSpeedCalculator(SpeedCalculator speedCalculator) {
        this.speedCalculator = speedCalculator;
    }
}
