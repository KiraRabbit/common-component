package com.guuidea.component.ip.strategy.impl;

import com.guuidea.component.ip.domain.IpRange;
import com.guuidea.component.ip.domain.IpTableHolder;
import com.guuidea.component.ip.strategy.DataStrategy;
import com.guuidea.component.ip.util.DBUtil;
import com.guuidea.component.ip.domain.IPProviderInfo;
import com.guuidea.component.ip.IPProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ll
 * @Date: 2020/4/14
 */
public class AbroadDataStrategy implements DataStrategy {
    private static String dbUrl = "jdbc:mysql://190.1.1.77:3308/joy_common_abroad?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private static String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static String dbUsername = "root";
    private static String dbPassword = "123456";

    @Override
    public void load(List<IPProviderInfo> ipProviderInfos) throws SQLException, ClassNotFoundException{
        DBUtil.setDbConfig(ipProviderInfos.get(0));
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        String selectSql = "select ip_start_num as ipStartNum,ip_end_num as ipEndNum,country_id as countryId from tbl_ip_info order by ip_start_num";
        ResultSet selectRes = stmt.executeQuery(selectSql);
        List<IpRange> ipRanges = new ArrayList<IpRange>();
        while (selectRes.next()) { // 循环输出结果集
            IpRange ipRange = new IpRange();
            ipRange.setIpStartNum(Long.parseLong(selectRes.getString("ipStartNum")));
            ipRange.setIpEndNum(Long.parseLong(selectRes.getString("ipEndNum")));
            String countryId = selectRes.getString("countryId");
            if (countryId != null && countryId.length() > 0) {
                ipRange.setCountryId(Integer.parseInt(countryId));
            }
            ipRanges.add(ipRange);
        }
        IpTableHolder ipTableHolder = new IpTableHolder();
        ipTableHolder.setIpRangeList(ipRanges);
        IPProvider.ipRangeList.add(ipTableHolder);
        DBUtil.closeConnection(conn, stmt, selectRes);
    }
}
