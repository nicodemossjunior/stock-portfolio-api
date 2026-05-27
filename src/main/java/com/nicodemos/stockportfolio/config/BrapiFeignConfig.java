package com.nicodemos.stockportfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import feign.RequestInterceptor;

@Configuration
public class BrapiFeignConfig {

	@Value("${brapi.token:}")
	private String brapiToken;

	@Bean
	public RequestInterceptor brapiRequestInterceptor() {
		return template -> {
			if (StringUtils.hasText(brapiToken)) {
				template.header("Authorization", "Bearer " + brapiToken);
			}
		};
	}

}
