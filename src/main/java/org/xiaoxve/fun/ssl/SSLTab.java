package org.xiaoxve.fun.ssl;

import org.xiaoxve.component.LeftTab;
import org.xiaoxve.fun.BaseTab;
import org.xiaoxve.itfc.Fun;

public class SSLTab extends BaseTab {
    private SSLFun sslFun;
    public SSLTab(String name, String icon, LeftTab leftTab) {
        super(name, icon, leftTab);
        sslFun = new SSLFun();
    }

    @Override
    public Fun getFun() {
        return sslFun;
    }
}
