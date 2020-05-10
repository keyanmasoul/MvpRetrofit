package corall.base.task;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import corall.base.bean.TaskEvent;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class CorTask<Param,Result> {

    private final String TAG = this.getClass().getSimpleName();
    private ICorTaskResult iCorTaskResult;

    public static final int TASK_STATUS_START = -1;
    public static final int TASK_STATUS_PASS = 0;
    public static final int TASK_STATUS_ERROR = 1;
    private CorTaskSign corTaskSign;

    public CorTask(CorTaskSign corTaskSign,ICorTaskResult iCorTaskResult) {
        this.corTaskSign = corTaskSign;
        this.iCorTaskResult = iCorTaskResult;
    }

    private void rxTask(final Param... params) {
        Flowable.create((FlowableOnSubscribe<Result>) emitter -> {
            emitter.onNext(CorTask.this.call(params));
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Result result) {
                        CorTask.this.onResult(result);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CorTask.this.onError(t);
                        if (iCorTaskResult != null) {
                            iCorTaskResult.onError(t.getMessage());
                            iCorTaskResult.onComplete(TASK_STATUS_ERROR);
                        }
                        corTaskSign.setTaskStatus(TASK_STATUS_ERROR);
                        EventBus.getDefault().post(new TaskEvent(corTaskSign));
                    }

                    @Override
                    public void onComplete() {
                        CorTask.this.onComplete();
                        iCorTaskResult.onComplete(TASK_STATUS_PASS);
                        corTaskSign.setTaskStatus(TASK_STATUS_PASS);
                        EventBus.getDefault().post(new TaskEvent(corTaskSign));
                    }
                });
    }

    protected abstract Result call(Param... params);

    protected void onPreExecute() {
    }

    protected void onResult(Result result) {
    }

    protected void onComplete() {
    }

    protected void onError(Throwable e) {

    }

    public final void execute(Param... params) {
        onPreExecute();
        rxTask(params);
    }
}
