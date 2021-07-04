package com.github.akazver.poc.config;

import com.github.akazver.poc.aop.ServiceAspect;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.Aspects;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Log4j2
@Configuration
public class ApplicationConfig {

    @Bean
    @SneakyThrows
    public ApplicationRunner applicationRunner() {
        return args -> {
            URI uri = Objects.requireNonNull(ApplicationConfig.class.getResource("/aop.xml")).toURI();
            String content = String.join("\n", Files.readAllLines(Paths.get(uri)));
            log.info("Content of aop.xml:\n{}", content);
        };
    }

    @Bean
    public ServiceAspect serviceAspect() {
        return Aspects.aspectOf(ServiceAspect.class);
    }

}
