package corall.base.dialog;

import android.view.View;

import androidx.annotation.NonNull;

public interface OnHolderListener {

    void onItemClick(@NonNull Object item, @NonNull View view, int position);

}
