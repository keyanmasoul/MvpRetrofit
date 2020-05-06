package corall.base;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class RxAsyncTask<Param,Result> {

    private final String TAG = this.getClass().getSimpleName();

    public RxAsyncTask() {

    }

    private void rxTask(final Param... params) {
        Flowable.create(new FlowableOnSubscribe<Result>() {
            @Override
            public void subscribe(FlowableEmitter<Result> emitter) throws Exception {
                emitter.onNext(RxAsyncTask.this.call(params));
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Result result) {
                        RxAsyncTask.this.onResult(result);
                    }

                    @Override
                    public void onError(Throwable t) {
                        RxAsyncTask.this.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        RxAsyncTask.this.onComplete();
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
