package com.guce.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;


public class SpringApplicationStarter {

    private static Logger logger = LoggerFactory.getLogger(SpringApplicationStarter.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationContainerBootstrap.class,args);

    }
}
