package corall.base.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.Map;

import cor.base.BuildConfig;
import corall.base.bean.DownloadEvent;

public class DownloadUtil {

    public static int start(Context context, String url, long id) {
        DownloadTask task = new DownloadTask.Builder(url, downloadParentPath(context), String.valueOf(id))
                .setMinIntervalMillisCallbackProcess(300)
                .setPassIfAlreadyCompleted(false)
                .setPriority(10)
                .setConnectionCount(1)
                .setAutoCallbackToUIThread(true)
                .build();
        task.enqueue(new DownloadListener4WithSpeed() {
            @Override
            public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {

            }

            @Override
            public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
                DownloadEvent downloadEvent = new DownloadEvent(DownloadEvent.STATUS_DOWNLOADING);
                downloadEvent.setCurrentOffset(currentOffset);
                downloadEvent.setSpeedCalculator(taskSpeed);
                EventBus.getDefault().post(downloadEvent);
                if (BuildConfig.DEBUG) {
                    Log.e("progress = ", currentOffset + " speed = " + taskSpeed.getInstantBytesPerSecondAndFlush());
                }
            }

            @Override
            public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {

            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
                DownloadEvent downloadEvent;
                if (cause == EndCause.COMPLETED) {
                    downloadEvent = new DownloadEvent(DownloadEvent.STATUS_DOWNLOAD_COMPLETED);
                } else if (cause == EndCause.CANCELED) {
                    downloadEvent = new DownloadEvent(DownloadEvent.STATUS_DOWNLOAD_CANCEL);
                } else {
                    downloadEvent = new DownloadEvent(DownloadEvent.STATUS_ERROR);
                }
                EventBus.getDefault().post(downloadEvent);
                if (BuildConfig.DEBUG) {
                    Log.e("taskEnd = ", cause + "");
                }
            }

            @Override
            public void taskStart(@NonNull DownloadTask task) {

            }

            @Override
            public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {

            }

            @Override
            public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

            }

            @Override
            public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                DownloadEvent downloadEvent;
                downloadEvent = new DownloadEvent(DownloadEvent.STATUS_FETCH_START);
                EventBus.getDefault().post(downloadEvent);
                if (BuildConfig.DEBUG) {
                    Log.e("fetchStart = ", contentLength + "");
                }
            }


            @Override
            public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                DownloadEvent downloadEvent;
                downloadEvent = new DownloadEvent(DownloadEvent.STATUS_FETCH_END);
                EventBus.getDefault().post(downloadEvent);
                if (BuildConfig.DEBUG) {
                    Log.e("fetchEnd ", "================");
                }
            }

        });
        return task.getId();
    }

    public static boolean hasDownload(Context context) {
        return StatusUtil.isCompleted("", downloadParentPath(context), "");
    }

    public static String downloadParentPath(Context context) {
        String dir = context.getExternalCacheDir() + File.separator + " BuildConfig.DOWNLOAD_PATH" + File.separator;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }
}
