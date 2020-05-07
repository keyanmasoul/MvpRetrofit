package corall.base.dialog;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

public interface HolderAdapter extends Holder {

    void setAdapter(@NonNull BaseAdapter adapter);

    void setOnItemClickListener(OnHolderListener listener);
}
