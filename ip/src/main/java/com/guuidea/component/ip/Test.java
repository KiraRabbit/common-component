package com.guuidea.component.ip;

import com.guuidea.component.ip.domain.IPProviderInfo;
import com.guuidea.component.ip.domain.IpRange;
import com.guuidea.component.ip.util.EnumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ll
 * @Date: 2020/4/14
 */
public class Test {
    public static void main(String[] args) throws Exception{
        IPProviderInfo info = new IPProviderInfo();
        info.setDbUrl("jdbc:mysql://190.1.1.77:3310/joy_common?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        info.setDbDriver("com.mysql.jdbc.Driver");
        info.setDbUsername("root");
        info.setDbPassword("123456");
//        IPProvider.getInstance(1,0 , info);
        System.out.println("111111");
        List<IPProviderInfo> list = new ArrayList<IPProviderInfo>();
        list.add(info);
        IPProvider.init(EnumUtil.DataSourceStrategyEnum.INLAND,EnumUtil.SearchTypeStrategyEnum.BINARY , list);


        IpRange i = IPProvider.search( 16910592L);
        System.out.println("i = " + i.getCityId());

        System.out.println(IPProvider.ip2Long("115.196.222.161"));
    }


}
