package com.bokesoft;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class StartApplication {
    private static ApplicationContext applicationContext;
    public static void main(String[] args) {
         applicationContext = SpringApplication.run(StartApplication.class, args);
    }

    @RequestMapping("/")
    public String login(HttpServletRequest request) {
    	return "redirect:/authority";
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
