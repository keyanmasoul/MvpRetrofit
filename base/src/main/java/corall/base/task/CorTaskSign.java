package corall.base.task;

import static corall.base.task.CorTask.TASK_STATUS_START;

public class CorTaskSign {

    private String signName;

    private int taskStatus;

    public CorTaskSign(String signName) {
        this.signName = signName;
        this.taskStatus = TASK_STATUS_START;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
}
