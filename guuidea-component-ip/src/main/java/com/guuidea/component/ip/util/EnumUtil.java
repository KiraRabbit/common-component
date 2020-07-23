package com.guuidea.component.ip.util;

import com.guuidea.component.ip.strategy.DataStrategy;
import com.guuidea.component.ip.strategy.SearchDataStragtegy;
import com.guuidea.component.ip.strategy.impl.AbroadDataStrategy;
import com.guuidea.component.ip.strategy.impl.BinarySearch;
import com.guuidea.component.ip.strategy.impl.InlandDataStrategy;

/**
 * @Author: ll
 * @Date: 2020/4/15
 */
public class EnumUtil {
    /**
     * 数据源
     */
    public enum DataSourceStrategyEnum {
        ABROAD(0, "国外单一数据源", new AbroadDataStrategy()),
        INLAND(1, "国内单一数据源", new InlandDataStrategy()),
        ;

        private int code;
        private String desc;
        private DataStrategy dataStrategy;

        DataSourceStrategyEnum(int code, String desc, DataStrategy dataStrategy) {
            this.code = code;
            this.desc = desc;
            this.dataStrategy = dataStrategy;
        }

        public int getCode() {
            return code;
        }

        public DataStrategy getDataStrategy() {
            return dataStrategy;
        }

        public String getDesc() {
            return desc;
        }

        public static DataSourceStrategyEnum getDataSourceEnum(int code) {
            for(DataSourceStrategyEnum e : DataSourceStrategyEnum.values()) {
                if(e.getCode() == code) {
                    return e;
                }
            }
            return INLAND;
        }
    }

    /**
     * 检索方式
     */
    public enum SearchTypeStrategyEnum {
        BINARY(0, "二分查找单一源", new BinarySearch()),
        ;

        private int code;
        private String desc;
        private SearchDataStragtegy searchDataStragtegy;

        SearchTypeStrategyEnum(int code, String desc, SearchDataStragtegy searchDataStragtegy) {
            this.code = code;
            this.desc = desc;
            this.searchDataStragtegy = searchDataStragtegy;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public SearchDataStragtegy getSearchDataStragtegy() {
            return searchDataStragtegy;
        }

        public static SearchTypeStrategyEnum getSearchTypeEnum(int code) {
            for(SearchTypeStrategyEnum e : SearchTypeStrategyEnum.values()) {
                if(e.getCode() == code) {
                    return e;
                }
            }
            return BINARY;
        }
    }
}
