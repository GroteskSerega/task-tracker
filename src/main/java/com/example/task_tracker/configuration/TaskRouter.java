package com.example.task_tracker.configuration;

import com.example.task_tracker.web.handler.v1.TaskHandlerV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TaskRouter {

    @Bean
    public RouterFunction<ServerResponse> taskRouters(TaskHandlerV1 taskHandlerV1) {
        return RouterFunctions.route()
                .GET("/api/v1/task", taskHandlerV1::getAllTask)
                .GET("/api/v1/task/{id}", taskHandlerV1::findById)
                .POST("/api/v1/task", taskHandlerV1::createTask)
                .PUT("/api/v1/task/{id}", taskHandlerV1::updateTask)
                .POST("/api/v1/task/add-observer/{id}", taskHandlerV1::addObserver)
                .DELETE("api/v1/task/{id}", taskHandlerV1::deleteTask)
                .build();
    }
}
