package zjj.network;

/**
 * ${Filename}
 * Created by zjj on 2017/2/13.
 */

public class BaseHttpResponse<T> {

    private String title;
    private T subjects;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getSubjects() {
        return subjects;
    }

    public void setSubjects(T subjects) {
        this.subjects = subjects;
    }
}
