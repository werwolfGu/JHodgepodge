package com.guce.module;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author chengen.gce
 * @DATE 2021/8/12 9:45 下午
 */
@Getter
@Setter
public class SwitchConfigInfo {

    private String id;
    private String sysId;
    private String switchKey;
    private String switchVal;
    private Date startTime;
    private Date endTime;
    private String isValid;
}
