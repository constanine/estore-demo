package com.bokesoft.ecomm.estore.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bokesoft.ecomm.estore.web.filter.SessionFilter;

/*
 * @author:gushiming
 * @date:2019/12/27
 * @description:注册bean,将自定义的过滤器添加到过滤器链中
 * */

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<SessionFilter> filterRegistrationBean(){

        FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<SessionFilter>();
        SessionFilter filter = new SessionFilter();

        registrationBean.setFilter(filter);
        //设置过滤器拦截请求
        List<String> urls = new ArrayList<>();
        urls.add("/*");
        registrationBean.setUrlPatterns(urls);

        return registrationBean;
    }

}
