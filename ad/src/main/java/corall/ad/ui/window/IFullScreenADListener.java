package corall.ad.ui.window;

import corall.ad.bean.CorAdPlace;

/**
 * Created by ChenLi on 2017/12/14.
 */

public interface IFullScreenADListener {

    public void onADShow(CorAdPlace adPlace);

    public void onADClose(CorAdPlace adPlace);

}
