package org.xiaoxve.fun.json;

import org.xiaoxve.component.LeftTab;
import org.xiaoxve.fun.BaseTab;
import org.xiaoxve.itfc.Fun;

public class JsonTab extends BaseTab {

    private JsonFun jsonFun;

    public JsonTab(String name, String icon, LeftTab leftTab) {
        super(name, icon, leftTab);
        jsonFun = new JsonFun();
    }

    @Override
    public Fun getFun() {
        return jsonFun;
    }
}
