package zjj.work2017.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import zjj.network.BasePresenter;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        setContentView(setLayout());
        initUIAndData();
    }

    protected abstract P createPresenter();
    protected abstract int setLayout();
    protected abstract void initUIAndData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }


}
