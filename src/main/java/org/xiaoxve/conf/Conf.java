package org.xiaoxve.conf;

import lombok.Data;

import java.util.List;

/**
 * 配置类
 */
@Data
public class Conf {
    private List<ConfTab> tabs;
}
