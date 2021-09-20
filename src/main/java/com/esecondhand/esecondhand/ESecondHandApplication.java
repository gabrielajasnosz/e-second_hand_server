package com.esecondhand.esecondhand;

import com.esecondhand.esecondhand.security.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ESecondHandApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESecondHandApplication.class, args);
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new JwtFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singleton("/api/hello/*"));
        return filterRegistrationBean;
    }
}
