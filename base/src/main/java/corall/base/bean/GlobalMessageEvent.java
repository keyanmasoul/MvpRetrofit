package corall.base.bean;

import androidx.annotation.NonNull;


public class GlobalMessageEvent extends MessageEvent{

    private Object object;
    private int what;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{ msg = " + (object == null ? "null" : object.toString()) + " }";
    }
}
