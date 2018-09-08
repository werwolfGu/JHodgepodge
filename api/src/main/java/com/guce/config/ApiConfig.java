package com.guce.config;

import com.guce.example.ExampleDemo;
import com.guce.AppDemo;
import com.guce.aop.MyAnnTestAnalysis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean("exampleDemo")
    public ExampleDemo exampleDemo(){
        return new ExampleDemo();
    }

    @Bean("appDemo")
    public AppDemo appDemo(){
        return new AppDemo();
    }

    @Bean("myAnnTestAnalysis")
    public MyAnnTestAnalysis myAnnTestAnalysis(){
        return new MyAnnTestAnalysis();
    }

}
