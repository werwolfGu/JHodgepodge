package com.guce.allocation;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author chengen.gce
 * @DATE 2022/5/12 19:51
 */
public class TradeDataEntity<D> {
    @Getter
    @Setter
    protected Long clusterIdentifierCode;

    @Setter
    @Getter
    protected String threadIdentifierCode;

    @Setter
    @Getter
    protected String businessName;

    @Setter
    @Getter
    private D data;

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
