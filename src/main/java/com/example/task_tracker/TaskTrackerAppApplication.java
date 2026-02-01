package com.example.task_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class TaskTrackerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerAppApplication.class, args);
    }

}
