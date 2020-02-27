package com.grvyframework.bootstrap;

import com.grvyframework.spring.autoconfigure.GrvyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author chengen.gu
 * @date 2020-01-20 15:16
 * @description
 */
@SpringBootApplication
@Import(GrvyAutoConfiguration.class)
public class SpringbootStartup {

    public static void main(String[] args) {

        SpringApplication.run(SpringbootStartup.class, args);

    }
}
