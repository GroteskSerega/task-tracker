package com.example.task_tracker.web.dto.v1;

public record UserResponse (
        String id,
        String username,
        String email
) {
}
