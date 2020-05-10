package corall.base.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.StringRes;

import corall.base.util.StringUtil;
import cor.base.R;

abstract public class BaseDialog {

    private CorDialog dialogPlus;
    private DialogPlusBuilder builder;
    protected View root;
    protected Activity activity;

    public BaseDialog(Activity activity, int layoutId) {
        this.activity = activity;
        builder = CorDialog.newDialog(activity)
                .setCancelable(setCancelAble())
                .setContentHolder(new ViewHolder(layoutId))
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.color.base_trans);
    }

    public CorDialog getDialogPlus() {
        return dialogPlus;
    }

    public DialogPlusBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(DialogPlusBuilder builder) {
        this.builder = builder;
    }

    public CorDialog create() {
        dialogPlus = this.builder.create();
        root = dialogPlus.getHolderView();
        initView();
        return dialogPlus;
    }

    public void show() {
        if (dialogPlus == null) {
            dialogPlus = create();
        }
        dialogPlus.show();
    }

    public void dismiss() {
        if (dialogPlus != null) {
            dialogPlus.dismiss();
        }
    }

    protected abstract void initView();

    protected String getStr(int resId) {
        return StringUtil.decodeStringRes(activity, resId);
    }

    protected String getStr(@StringRes int res, Object... formatArgs) {
        return StringUtil.decodeStringRes(activity, res, formatArgs);
    }

    protected boolean setCancelAble() {
        return false;
    }
}
