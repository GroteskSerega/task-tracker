package com.example.task_tracker.configuration;

import com.example.task_tracker.web.handler.v1.UserHandlerV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRouters(UserHandlerV1 userHandlerV1) {
        return RouterFunctions.route()
                .GET("/api/v1/user", userHandlerV1::getAllUser)
                .GET("/api/v1/user/{id}", userHandlerV1::findById)
                .POST("/api/v1/user", userHandlerV1::createUser)
                .PUT("/api/v1/user/{id}", userHandlerV1::updateUser)
                .DELETE("/api/v1/user/{id}", userHandlerV1::deleteUser)
                .build();
    }
}
