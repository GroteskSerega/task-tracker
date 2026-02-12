package com.example.task_tracker;

import com.example.task_tracker.entity.RoleType;
import com.example.task_tracker.entity.Task;
import com.example.task_tracker.entity.TaskStatus;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.web.dto.v1.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RegisterReflectionForBinding({
        UserUpsertRequest.class,
        UserResponse.class,
        UserListResponse.class,
        TaskUpsertRequest.class,
        TaskResponse.class,
        TaskObserverIdRequest.class,
        TaskListResponse.class,
        RoleType.class,
        User.class,
        TaskStatus.class,
        Task.class
})
@SpringBootApplication
public class TaskTrackerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerAppApplication.class, args);
    }

}
