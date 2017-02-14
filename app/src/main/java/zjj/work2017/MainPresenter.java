package zjj.work2017;

import android.graphics.Movie;
import android.util.Log;

import java.util.List;

import zjj.network.BasePresenter;
import zjj.network.interfaces.IMvpBaseView;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public class MainPresenter extends BasePresenter<MainPresenter.IMainView, List<Movie>> {

    private IMainView iMainView;

    public MainPresenter(IMainView iMainView) {
        super(iMainView);
        this.iMainView = iMainView;
    }

    @Override
    public void onSuccess(List<Movie> o, int tag) {
        Log.d("result-----", "back");
        iMainView.doSuccess();
    }

    @Override
    public void onError(String msg, int tag) {

    }

    public void getTopMovie() {
        sendHttpRequest(
                apiService.getTopMovie(0, 10),
                0);
    }


    public interface IMainView extends IMvpBaseView {
        void doSuccess();
    }
}
