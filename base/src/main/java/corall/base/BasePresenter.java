package corall.base;

import corall.base.interfaces.IBaseView;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import corall.base.interfaces.IBasePresenter;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    protected V mvpView;
    protected ApiService apiService;

    protected CompositeDisposable compositeSubscription;

    public BasePresenter(V mvpView) {
        attachView(mvpView);
        this.compositeSubscription = new CompositeDisposable();
        this.apiService = HttpHelper.getInstance().getApiService();
    }

    private void attachView(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        this.mvpView = null;
        unSubscribe();
    }

    protected HttpSubscriber getSubscriber(int tag, boolean showLoading) {
        if (showLoading) {
            mvpView.showLoadingDialog();
        }
        return new HttpSubscriber<String>(tag) {
            @Override
            public void onSubscribe(Disposable d) {
                compositeSubscription.add(d);
            }

            @Override
            public void onSuccess(String o, int tag) {
                BasePresenter.this.onSuccess(o, tag);
            }

            @Override
            public void onError(String msg, int tag) {
                BasePresenter.this.onError(msg, tag);
            }
        };
    }

    public void sendHttpRequest(Observable observable, int tag) {
        HttpHelper.getInstance().sendHttpRequest(observable, getSubscriber(tag, false));
    }

    @Override
    public void subscribe(Disposable disposable) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeDisposable();
        }
        compositeSubscription.add(disposable);
    }

    @Override
    public void unSubscribe() {
        if (compositeSubscription != null && !compositeSubscription.isDisposed())
            compositeSubscription.dispose();
    }

    public abstract void onSuccess(String result, int tag);

    public abstract void onError(String msg, int tag);
}
