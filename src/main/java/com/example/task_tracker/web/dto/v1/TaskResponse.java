package com.example.task_tracker.web.dto.v1;

import com.example.task_tracker.entity.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskResponse (
        String id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        TaskStatus status,
        UserResponse author,
        UserResponse assignee,
        Set<UserResponse> observers
) {
}
