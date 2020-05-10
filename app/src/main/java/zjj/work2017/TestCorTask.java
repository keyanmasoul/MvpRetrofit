package zjj.work2017;

import corall.base.task.CorTask;
import corall.base.task.CorTaskSign;
import corall.base.task.ICorTaskResult;

public class TestCorTask extends CorTask<String,String> {

    public TestCorTask(CorTaskSign corTaskSign, ICorTaskResult iCorTaskResult) {
        super(corTaskSign, iCorTaskResult);
    }

    @Override
    protected String call(String... strings) {
        String a = null;
        a.split("1");
        return null;
    }
}
