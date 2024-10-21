package org.xiaoxve.conf;

import lombok.Data;
import org.xiaoxve.component.LeftTab;
import org.xiaoxve.itfc.Tab;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
@Data
public class ConfTab {
    private String clazz;
    private String name;
    private String icon;
    private Boolean enabled;

    public Tab generateInstance(LeftTab leftTab){
        try {
            Class<?> tabClass = Class.forName(clazz);
            Constructor<?> constructor = tabClass.getConstructor(String.class, String.class, LeftTab.class);
            return (Tab) constructor.newInstance(name, icon, leftTab);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
