package com.example.task_tracker.web.dto.v1;

import com.example.task_tracker.entity.TaskStatus;
import com.example.task_tracker.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus status;

//    private String authorId;

//    private String assigneeId;

//    private Set<String> observerIds;

    private User author;

    private User assignee;

    private Set<User> observers;
}
