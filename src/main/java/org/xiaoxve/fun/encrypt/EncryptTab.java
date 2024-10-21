package org.xiaoxve.fun.encrypt;

import org.xiaoxve.component.LeftTab;
import org.xiaoxve.fun.BaseTab;
import org.xiaoxve.itfc.Fun;

public class EncryptTab extends BaseTab {
    private EncryptFun encryptTabFun;
    public EncryptTab(String name, String icon, LeftTab leftTab) {
        super(name, icon, leftTab);
        encryptTabFun =  new EncryptFun();
    }

    @Override
    public Fun getFun() {
        return encryptTabFun;
    }
}
