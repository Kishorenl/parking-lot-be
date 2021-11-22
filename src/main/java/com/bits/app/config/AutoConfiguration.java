package com.bits.app.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

import com.bits.app.entity.Book;

@Configuration
@EntityScan(basePackageClasses = { Book.class })
@EnableAutoConfiguration
@ConditionalOnProperty(value = "spring.cloud.gcp.firestore.enabled", matchIfMissing = true)
public class AutoConfiguration {

}
