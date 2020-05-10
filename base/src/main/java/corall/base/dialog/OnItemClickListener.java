package corall.base.dialog;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * It is used to propagate click events for {@link ListHolder} and {@link GridHolder}
 *
 * <p>For each item click, {@link #onItemClick(CorDialog, Object, View, int)} will be invoked</p>
 */
public interface OnItemClickListener {

    void onItemClick(@NonNull CorDialog dialog, @NonNull Object item, @NonNull View view, int position);

}
