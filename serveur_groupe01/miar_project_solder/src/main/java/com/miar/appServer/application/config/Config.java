package com.miar.appServer.application.config;

import com.miar.appServer.domain.EventService;
import com.miar.appServer.domain.SpentService;
import com.miar.appServer.domain.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableMongoRepositories( basePackages = "com.miar.appServer.infra.collections" )
public class Config {
    @Bean
    public UserService getUserService() { return new UserService(); }
    @Bean
    public EventService getEventService() { return new EventService(); }
    @Bean
    public SpentService getSpentService() { return new SpentService(); }
}
