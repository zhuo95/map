package com.zz.map;

import com.zz.map.controller.common.SessionExpireFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class MapApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapApplication.class, args);
	}

  @PostConstruct
  void setDefaultTimezone() {
	TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
  }

	//配置fliter
	@Bean
	public FilterRegistrationBean httpFilter(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		//设置filter
		registrationBean.setFilter(new SessionExpireFilter());
		//定义拦截url
		registrationBean.addUrlPatterns("/");
		return registrationBean;
	}
}
