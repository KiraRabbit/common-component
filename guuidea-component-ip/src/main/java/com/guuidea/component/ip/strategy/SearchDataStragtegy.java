package com.guuidea.component.ip.strategy;

import com.guuidea.component.ip.domain.IpRange;

/**
 * @Author: ll
 * @Date: 2020/4/14
 */
public interface SearchDataStragtegy {
    IpRange search(long ip);
}
