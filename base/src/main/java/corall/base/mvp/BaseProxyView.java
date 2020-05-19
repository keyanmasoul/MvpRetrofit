package corall.base.mvp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import corall.base.app.CorApplication;
import corall.base.util.StringUtil;


/**
 * desc:
 * date: 2019/2/21
 * author: ancun
 */
public abstract class BaseProxyView<P extends BasePresenter> extends FrameLayout implements IBaseProxyView {

    protected CorApplication imContext;
    protected BaseMVPActivity mHost;
    protected P mPresenter;

    public BaseProxyView(Context context) {
        super(context);
        imContext = CorApplication.getInstance();
        mHost = (BaseMVPActivity) context;
        View root = LayoutInflater.from(context).inflate(getLayoutRes(), this);
        findView(root);
    }

    public void attach(P presenter) {
        mPresenter = presenter;
    }

    @Override
    public BaseMVPActivity host() {
        return mHost;
    }

    @Override
    public P presenter() {
        return mPresenter;
    }

    @Override
    public void detachView() {
        mPresenter = null;
        mHost = null;
        imContext = null;
    }

    public abstract int getLayoutRes();

    public abstract void findView(View view);

    public abstract void flushView(int what, Object object);

    public abstract void onResume();

    public abstract void onPause();

    protected void requestWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //告诉系统 当前使用浅色状态栏背景，让其自动改变字体颜色
            host().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    protected void initCustomToolBar(int stringRes) {

    }

    public boolean onBackPressed() {
        return false;
    }

    protected void initBackIconAndTitleStyle(ImageView back, TextView title) {

    }

    protected void initCustomToolBar() {
        initCustomToolBar(0);
    }

    protected void onBackIconClick(View v) {
        if (host() != null && !host().isFinishing()) {
            host().finish();
        }
    }

    public int color(@ColorRes int res) {
        return ContextCompat.getColor(getContext(), res);
    }

    public String getString(@StringRes int res, Object... formatArgs) {
        return StringUtil.decodeStringRes(host(), res, formatArgs);
    }

}
