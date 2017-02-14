package zjj.network;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import zjj.network.interfaces.IBasePresenter;
import zjj.network.interfaces.IMvpBaseView;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public abstract class BasePresenter<V extends IMvpBaseView> implements IBasePresenter<V> {

    protected V mvpView;
    protected ApiService apiService;

    protected CompositeSubscription compositeSubscription;

    public BasePresenter(V mvpView) {
        attachView(mvpView);
        this.compositeSubscription = new CompositeSubscription();
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
        compositeSubscription.add(HttpHelper.getInstance().sendHttpRequest(observable, getSubscriber(
                tag, false)
        ));
    }

    @Override
    public void subscribe(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    public void unSubscribe() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }

    public abstract void onSuccess(String result, int tag);

    public abstract void onError(String msg, int tag);
}
