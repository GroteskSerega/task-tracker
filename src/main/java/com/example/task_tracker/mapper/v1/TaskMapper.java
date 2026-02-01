package com.example.task_tracker.mapper.v1;

import com.example.task_tracker.entity.Task;
import com.example.task_tracker.web.dto.v1.TaskListResponse;
import com.example.task_tracker.web.dto.v1.TaskResponse;
import com.example.task_tracker.web.dto.v1.TaskUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task requestToTask(TaskUpsertRequest request);

    @Mapping(source = "taskId", target = "id")
    Task requestToTask(String taskId, TaskUpsertRequest request);

    TaskResponse taskToResponse(Task task);

    default TaskListResponse taskListToTaskListResponse(List<Task> tasks) {
        TaskListResponse response = new TaskListResponse();

        response.setTasks(tasks
                .stream()
                .map(this::taskToResponse)
                .collect(Collectors.toList()));

        return response;
    }
}
