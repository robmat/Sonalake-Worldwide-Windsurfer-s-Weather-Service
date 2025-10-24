// src/main/java/edu/batodev/windsurf/config/ConcurrencyConfig.java
package edu.batodev.windsurf.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConcurrencyConfig {

    @Bean
    public ExecutorServiceFactory virtualThreadExecutorFactory() {
        return new ExecutorServiceFactory();
    }

    public static class ExecutorServiceFactory implements ObjectFactory<ExecutorService> {
        @Override
        public ExecutorService getObject() throws BeansException {
            return Executors.newVirtualThreadPerTaskExecutor();
        }
    }
}
