package corall.adscene.strategy;

import corall.ad.bean.CorAdPlace;

/**
 * desc:
 * date: 2018/4/19
 * author: ancun
 */

public interface IStrategyExecutor {

    /**
     * 根据 HmAdPlace 的广告配置，检查是否符合配置要求
     *
     * @param hmNativeAd
     * @return 是否符合配置要求
     */
    boolean check(CorAdPlace hmNativeAd);

    /**
     * 注意：在广告展示时必须调用此方法
     * 注意：在广告展示时必须调用此方法
     * 注意：在广告展示时必须调用此方法
     */
    void record();

    /**
     * 重置时间或计数等
     */
    void reset();

    /**
     * 注意：在广告请求时调用此方法
     * 注意：在广告请求时调用此方法
     * 注意：在广告请求时调用此方法
     */
    void recordOnRequest();


}
