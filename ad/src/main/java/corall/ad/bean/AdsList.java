package corall.ad.bean;

import java.util.ArrayList;

public class AdsList {
    private int chance;
    private int frozen;
    private ArrayList<RawAd> mRawAds;

    public AdsList(){

    }

    public int getChance() {
        return chance;
    }

    public int getFrozen() {
        return frozen;
    }

    public ArrayList<RawAd> getRawAds() {
        return mRawAds;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public void setFrozen(int frozen) {
        this.frozen = frozen;
    }

    public void setRawAds(ArrayList<RawAd> mRawAds) {
        this.mRawAds = mRawAds;
    }


}
