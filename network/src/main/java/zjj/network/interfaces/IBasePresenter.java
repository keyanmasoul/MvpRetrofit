package zjj.network.interfaces;


import io.reactivex.disposables.Disposable;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public interface IBasePresenter<V extends IMvpBaseView> {

    void subscribe(Disposable disposable);

    void unSubscribe();

    void detachView();

}
