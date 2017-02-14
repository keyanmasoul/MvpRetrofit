package zjj.network;

import android.util.Log;

import rx.Subscriber;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public abstract class HttpSubscriber<T> extends Subscriber<T> {
    private int tag;

    public HttpSubscriber(int tag) {
        this.tag = tag;
    }

    @Override
    public void onCompleted() {
        if (HttpHelper.getInstance().isDebug()) {
            Log.d("HttpSubscriber", "onCompleted");
        }
    }

    @Override
    public void onError(Throwable e) {
        onError(e.getMessage(), tag);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t, this.tag);
    }

    public abstract void onSuccess(T t, int tag);

    public abstract void onError(String msg, int tag);
}
