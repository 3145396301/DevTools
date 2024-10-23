package org.xiaoxve.fun.project;

import org.xiaoxve.component.LeftTab;
import org.xiaoxve.fun.BaseTab;
import org.xiaoxve.itfc.Fun;

public class ProjectToMdTab extends BaseTab {
    private ProjectToMdFun fun;
    public ProjectToMdTab(String name, String icon, LeftTab leftTab) {
        super(name, icon, leftTab);
        fun = new ProjectToMdFun();
    }

    @Override
    public Fun getFun() {
        return fun;
    }
}
