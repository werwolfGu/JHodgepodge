package com.guce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2022/4/9 7:02 PM
 */
@Component
public class ServiceB {

    @Autowired
    private ServiceA a;
}
