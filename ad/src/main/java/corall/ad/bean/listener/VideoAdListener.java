package corall.ad.bean.listener;

import corall.ad.bean.RawAd;

public interface VideoAdListener {

    public void onVideoStart(RawAd ad);
    public void onVideoEnd(RawAd ad);
    public void onVideoPregress(RawAd ad, int total, int current);
}
