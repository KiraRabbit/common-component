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
public class InlandDataStrategy implements DataStrategy {
    private static String dbUrl = "jdbc:mysql://190.1.1.77:3310/joy_common?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT";
    private static String dbDriver = "com.mysql.jdbc.Driver";
    private static String dbUsername = "root";
    private static String dbPassword = "123456";

    @Override
    public void load(List<IPProviderInfo> ipProviderInfos) throws SQLException, ClassNotFoundException{
        DBUtil.setDbConfig(ipProviderInfos.get(0));
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        String selectSql = "select ip_start_num as ipStartNum,ip_end_num as ipEndNum,ifnull(province_id,-1)  as provinceId,ifnull(city_id,-1) as cityId from tbl_ip_info order by ip_start_num";
        ResultSet selectRes = stmt.executeQuery(selectSql);
        List<IpRange> ipRanges = new ArrayList<IpRange>();
        while (selectRes.next()) { // 循环输出结果集
            IpRange ipRange = new IpRange();
            ipRange.setIpStartNum(Long.parseLong(selectRes.getString("ipStartNum")));
            ipRange.setIpEndNum(Long.parseLong(selectRes.getString("ipEndNum")));
            String provinceId = selectRes.getString("provinceId");
            if (provinceId != null && provinceId.length() > 0) {
                ipRange.setProvinceId(Integer.parseInt(provinceId));
            }
            String cityId = selectRes.getString("cityId");
            if (cityId != null && cityId.length() > 0) {
                ipRange.setCityId(Integer.parseInt(cityId));
            }
            ipRanges.add(ipRange);
        }
        IpTableHolder ipTableHolder = new IpTableHolder();
        ipTableHolder.setIpRangeList(ipRanges);
        IPProvider.ipRangeList.add(ipTableHolder);
        DBUtil.closeConnection(conn, stmt, selectRes);
    }
}
