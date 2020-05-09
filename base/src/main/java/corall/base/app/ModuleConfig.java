package corall.base.app;

import java.util.ArrayList;

/**
 * 存储模块相关信息
 * Created by linlin_91 on 2015/6/24.
 */
public class ModuleConfig {

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块的类名
     */
    private AModule aModule;

    /**
     * 是否延迟加载
     */
    private boolean delay;

    /**
     * 依赖的模块名称集合
     */
    private ArrayList<String> dependModules = new ArrayList<String>();

    public ModuleConfig(String moduleName, AModule module) {
        this.moduleName = moduleName;
        this.aModule = module;
    }

    public ArrayList<String> getDependModules() {
        return dependModules;
    }

    public void setDependModules(ArrayList<String> dependModules) {
        this.dependModules = dependModules;
    }

    public void addDependModule(String module) {
        dependModules.add(module);
    }

    public AModule getModule() {
        return aModule;
    }

    public void setModuleClass(AModule module) {
        this.aModule = module;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public boolean isDelay() {
        return delay;
    }

    public void setDelay(boolean delay) {
        this.delay = delay;
    }

//    @Override
//    public String toString() {
//        return "ModuleConfig{" +
//                "moduleName='" + moduleName + '\'' +
//                ", className='" + aModule.getClass().getName() + '\'' +
//                ", delay=" + delay +
//                ", dependModules=" + dependModules +
//                '}';
//    }

}
