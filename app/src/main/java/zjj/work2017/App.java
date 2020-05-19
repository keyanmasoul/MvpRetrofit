package zjj.work2017;

import java.util.ArrayList;

import corall.ad.AdsModule;
import corall.base.app.CorApplication;
import corall.base.app.CorBeanManager;
import corall.base.app.ModuleConfig;

public class App extends CorApplication {
    @Override
    protected CorBeanManager createBeanManager() {
        return new CorBeanManager(this);
    }


    @Override
    protected void initContextConfig() throws Exception {

    }

    @Override
    protected void initMobConfig() throws Exception {

    }

    @Override
    public ArrayList<ModuleConfig> getModulesConfig() {
        ArrayList<ModuleConfig> list = new ArrayList<>();

        ModuleConfig adModuleConfig = new ModuleConfig(AdsModule.MODULE_KEY,
                new AdsModule(this, AdsModule.MODULE_KEY));
        adModuleConfig.setDelay(false);
        list.add(adModuleConfig);

        return list;
    }

    public AdsModule getAdsModule(){
        return (AdsModule) getSubModule(AdsModule.MODULE_KEY);
    }
}
