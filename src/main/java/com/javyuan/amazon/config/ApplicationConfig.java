package com.javyuan.amazon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ApplicationConfig {
	
	public static final String ENCODING = "UTF-8";
	
	@Bean(name="amazonExecutor")
	public ThreadPoolTaskExecutor getExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		return executor;
	}
}
