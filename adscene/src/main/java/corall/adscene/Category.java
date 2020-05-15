package corall.adscene;

/**
 * desc: 定义广告的分区
 * date: 2019/1/25
 * author: ancun
 */
public enum Category {

    /**
     * 功能分区
     */
    F("F", "功能分区"),

    /**
     * A区广告
     */
    A("A", "A区广告"),

    /**
     * B区广告
     */
    B("B", "B区广告"),

    /**
     * C区广告
     */
    C("C", "C区广告"),

    /**
     * D区广告
     */
    D("D", "D区广告"),

    /**
     * S区广告
     */
    S("S", "S区广告");

    private final String desc;
    private final String name;

    Category(String name, String desc) {
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{" + "desc='" + desc + '\'' + '}';
    }
}



