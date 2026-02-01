package com.example.task_tracker.web.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListResponse {

    private List<TaskResponse> tasks = new ArrayList<>();
}
