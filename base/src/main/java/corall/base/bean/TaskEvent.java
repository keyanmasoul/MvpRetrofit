package corall.base.bean;

import corall.base.task.CorTaskSign;
import cor.base.R;

public class TaskEvent extends MessageEvent {

    public CorTaskSign getCorTaskSign() {
        return corTaskSign;
    }

    public void setCorTaskSign(CorTaskSign corTaskSign) {
        this.corTaskSign = corTaskSign;
    }

    private CorTaskSign corTaskSign;


    public TaskEvent(CorTaskSign corTaskSign) {
        this.corTaskSign = corTaskSign;
        this.setWhat(R.id.msg_task_event);
    }

}
