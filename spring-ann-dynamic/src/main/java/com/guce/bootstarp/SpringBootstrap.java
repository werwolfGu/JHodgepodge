package com.guce.bootstarp;

import com.guce.anntation.AnnDemoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author chengen.gce
 * @DATE 2021/7/18 1:19 下午
 */
@SpringBootApplication
@ComponentScan("com.guce")
@AnnDemoScan(basePackages = {"com.guce"})
public class SpringBootstrap {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(SpringBootstrap.class);
        application.run(args);

    }
}
