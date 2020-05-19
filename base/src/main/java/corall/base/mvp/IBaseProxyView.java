package corall.base.mvp;

import android.os.Bundle;

/**
 * Created by dell on 2018/10/26.
 * 根视图
 */

public interface IBaseProxyView<P extends IBasePresenter> {

    BaseMVPActivity host();

    P presenter();

    void initView(Bundle savedInstanceState);

    void detachView();

}
