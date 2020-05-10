package corall.base.task;

public interface ICorTaskResult {
    void onError(String errorMessage);
    void onComplete(int taskStatus);
}
