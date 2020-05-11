package corall.base.bean;

import com.liulishuo.okdownload.SpeedCalculator;

import cor.base.R;


public class DownloadEvent extends MessageEvent {
    private long currentOffset;
    private SpeedCalculator speedCalculator;

    private int downloadStatus;

    public final static int STATUS_FETCH_START = 0;
    public final static int STATUS_DOWNLOADING = 1;
    public final static int STATUS_FETCH_END = 2;
    public final static int STATUS_DOWNLOAD_COMPLETED = 3;
    public final static int STATUS_ERROR = 4;
    public final static int STATUS_DOWNLOAD_CANCEL = 5;

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public DownloadEvent(int status) {
        this.downloadStatus = status;
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
