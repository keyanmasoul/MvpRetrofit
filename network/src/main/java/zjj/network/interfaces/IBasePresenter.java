package zjj.network.interfaces;

import rx.Subscription;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public interface IBasePresenter<V extends IMvpBaseView> {

    void subscribe(Subscription subscription);

    void unSubscribe();

    void detachView();

}
