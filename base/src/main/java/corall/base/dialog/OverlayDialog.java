package corall.base.dialog;

import android.app.Activity;
import android.widget.TextView;

import corall.base.util.OverlaySetting;
import zjj.network.R;

public class OverlayDialog extends BaseDialog {

    public OverlayDialog(Activity activity) {
        super(activity, R.layout.dialog_request_overlay);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected boolean setCancelAble() {
        return true;
    }

    @Override
    protected void initView() {
        root.findViewById(R.id.tv_setting).setOnClickListener(view -> {
            //弹出设置界面
            OverlaySetting.start(activity);
            dismiss();
        });
        TextView tvTitle = root.findViewById(R.id.tv_desc);
        tvTitle.setText(getStr(R.string.overlay_request_tip));
    }

}
