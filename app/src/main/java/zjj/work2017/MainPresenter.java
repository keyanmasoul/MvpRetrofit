package zjj.work2017;

import android.util.Log;

import zjj.network.BasePresenter;
import zjj.network.interfaces.IMvpBaseView;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public class MainPresenter extends BasePresenter<MainPresenter.IMainView> {

    private IMainView iMainView;

    public MainPresenter(IMainView iMainView) {
        super(iMainView);
        this.iMainView = iMainView;
    }

    private static final int GET_TOP_MOVIE = 0;

    @Override
    public void onSuccess(String result, int tag) {
        Log.d("result-----", result);
        switch (tag) {
            case GET_TOP_MOVIE:
                //// TODO: 2017/2/14 string to jsonobject
                break;
            default:
        }
        iMainView.doSuccess();
    }

    @Override
    public void onError(String msg, int tag) {

    }

    public void getTopMovie() {
        sendHttpRequest(
                apiService.getTopMovie(0, 10),
                GET_TOP_MOVIE);
    }


    public interface IMainView extends IMvpBaseView {
        void doSuccess();
    }
}
