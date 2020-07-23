package com.guuidea.component.ip.strategy;

import com.guuidea.component.ip.domain.IPProviderInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author: ll
 * @Date: 2020/4/14
 */
public interface DataStrategy {
    void load(List<IPProviderInfo> ipProviderInfos) throws SQLException, ClassNotFoundException;
}
