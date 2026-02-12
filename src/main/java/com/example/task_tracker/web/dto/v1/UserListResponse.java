package com.example.task_tracker.web.dto.v1;

import java.util.List;

public record UserListResponse (List<UserResponse> users) {
}
