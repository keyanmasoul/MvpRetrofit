package zjj.work2017;

import android.os.Bundle;

import zjj.network.ApiService;
import zjj.network.BasePresenter;
import zjj.network.HttpHelper;
import zjj.work2017.common.BaseActivity;

public class MainActivity extends BaseActivity implements MainPresenter.IMainView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected BasePresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUIAndData() {
        ((MainPresenter)mPresenter).getTopMovie();
    }

    @Override
    public void doSuccess() {

    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }
}
