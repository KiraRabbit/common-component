package com.guuidea.component.ip.strategy.impl;

import com.guuidea.component.ip.domain.IpRange;
import com.guuidea.component.ip.strategy.SearchDataStragtegy;
import com.guuidea.component.ip.IPProvider;

import java.util.Collections;
import java.util.List;

/**
 * @Author: ll
 * @Date: 2020/4/14
 * @desc: 单数据源
 */
public class BinarySearch implements SearchDataStragtegy {

    @Override
    public IpRange search(long ip) {
        List<IpRange> ipList = IPProvider.ipRangeList.get(0).getIpRangeList();
        if ((ipList == null) || (ipList.isEmpty())) {
            return null;
        }
        int index = Collections.binarySearch(ipList, new IpRange(ip, ip));
        if (index < 0) {
            return null;
        }
        return ipList.get(index);
    }
}
