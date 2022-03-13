package com.guce.bootstarp;

import com.guce.anntation.AnnDemoScan;
import com.guce.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class SpringBootstrap implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(SpringBootstrap.class);

        application.run(args);

    }

    @Autowired
    private IDemoService demoService;

    @Override
    public void run(String... args) throws Exception {
        demoService.helloWorld();
    }
}
