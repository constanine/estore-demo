package com.bokesoft.ecomm.estore;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class StartApplication {
    private static ApplicationContext applicationContext;
    public static void main(String[] args) {
         applicationContext = SpringApplication.run(StartApplication.class, args);
    }

    public static <T> T getBean (Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean (Class<T> clazz,String beanName) {
        return applicationContext.getBean(clazz,beanName);
    }

    public static <T> Map<String,T> getBeanMap (Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }
}
