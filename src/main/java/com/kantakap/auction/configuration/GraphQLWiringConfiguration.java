package com.kantakap.auction.configuration;

import com.kantakap.auction.configuration.scalar.DateTimeScalarConfiguration;
import com.kantakap.auction.configuration.scalar.MultipartFileScalarConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLWiringConfiguration {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(DateTimeScalarConfiguration.dateTimeScalar())
                .scalar(MultipartFileScalarConfiguration.fileScalar());
    }
}
