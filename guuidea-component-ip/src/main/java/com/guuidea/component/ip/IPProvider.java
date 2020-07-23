package com.guuidea.component.ip;

import com.guuidea.component.ip.domain.IPProviderInfo;
import com.guuidea.component.ip.domain.IpRange;
import com.guuidea.component.ip.domain.IpTableHolder;
import com.guuidea.component.ip.strategy.DataStrategy;
import com.guuidea.component.ip.strategy.SearchDataStragtegy;
import com.guuidea.component.ip.util.EnumUtil;
import com.guuidea.component.ip.util.IpUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ll
 * @Date: 2020/4/14
 */
public class IPProvider {
    private DataStrategy dataStrategy;
    private SearchDataStragtegy searchDataStragtegy;
    public static List<IpTableHolder> ipRangeList = new ArrayList<IpTableHolder>();
    private List<IPProviderInfo> ipProviderInfos;

    private IPProvider(EnumUtil.DataSourceStrategyEnum dataSourceEnum, EnumUtil.SearchTypeStrategyEnum searchTypeEnum, List<IPProviderInfo> ipProviderInfo){
        this.dataStrategy = dataSourceEnum.getDataStrategy();
        this.searchDataStragtegy = searchTypeEnum.getSearchDataStragtegy();
        this.ipProviderInfos = ipProviderInfo;
    }


    private IPProvider(){

    }

    private static IPProvider ipProvider;

    public List<IPProviderInfo> getIpProviderInfos() {
        return ipProviderInfos;
    }

    public static void init(EnumUtil.DataSourceStrategyEnum dataSourceEnum, EnumUtil.SearchTypeStrategyEnum searchTypeEnum, List<IPProviderInfo> ipProviderInfos) throws SQLException, ClassNotFoundException{
        ipProvider =  new IPProvider(dataSourceEnum, searchTypeEnum, ipProviderInfos);
        ipProvider.dataStrategy.load(ipProvider.getIpProviderInfos());
    }

    public static IpRange search(Long ip){
        return ipProvider.searchDataStragtegy.search(ip);
    }

    public static Long ip2Long(String ip) throws Exception{
        return IpUtil.ip2Long(ip);
    }

}
