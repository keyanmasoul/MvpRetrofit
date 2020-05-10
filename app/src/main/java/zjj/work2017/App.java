package zjj.work2017;

import java.util.ArrayList;

import corall.base.app.CorManager;
import corall.base.app.CorApplication;
import corall.base.app.CorBeanManager;
import corall.base.app.ModuleConfig;

public class App extends CorApplication {
    @Override
    protected CorBeanManager createBeanManager() {
        return null;
    }

    @Override
    public CorManager getMobManager() {
        return null;
    }

    @Override
    protected void initContextConfig() throws Exception {

    }

    @Override
    protected void initMobConfig() throws Exception {

    }

    @Override
    public ArrayList<ModuleConfig> getModulesConfig() {
        return null;
    }
}
