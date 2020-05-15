package corall.ad.ui.window;

import corall.ad.bean.CorAdPlace;

/**
 * Created by ChenLi on 2018/1/22.
 */

public interface ISimpleFullScreenADListener {
    public void onADShow(CorAdPlace adPlace);

    public void onADClick(CorAdPlace adPlace);

    public void onADClose(CorAdPlace adPlace);

    public void onADLoadFail(CorAdPlace adPlace);

    //如果返回false则打断广告流程，不再显示
    public boolean beforeADShow(CorAdPlace adPlace);
}
