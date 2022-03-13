package com.guce.interceptor;

import com.guce.cache.AbstraceSwitchCacheInterceptor;
import com.guce.module.SwitchConfigInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/8/12 10:20 下午
 */
@Component
public class SwitchCacheInterceptor extends AbstraceSwitchCacheInterceptor {

    @Override
    public List<SwitchConfigInfo> invoke(String key) {

        List<SwitchConfigInfo> list = new ArrayList<>();

        SwitchConfigInfo val = new SwitchConfigInfo();
        val.setSwitchVal("test");
        val.setEndTime(DateUtils.addDays(new Date(), -1));
        list.add(val);
        if (StringUtils.equals("key1", key)) {
            val = new SwitchConfigInfo();
            val.setSwitchVal("val1");
            list.add(val);
        }
        if (StringUtils.equals("key2", key)) {
            val = new SwitchConfigInfo();
            val.setSwitchVal("val2,1;2,3|4");
            list.add(val);
        }
        if (StringUtils.equals("key3", key)) {
            val = new SwitchConfigInfo();
            val.setSwitchVal("val3,12,3;4,5,6");
            list.add(val);
        }
        if (StringUtils.equals("key4", key)) {
            val = new SwitchConfigInfo();
            //val.setSwitchVal("val4");
            list.add(val);
        }

        if (StringUtils.equals("key7", key)) {
            return null;
        }

        return list;
    }
}
