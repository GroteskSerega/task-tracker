package com.example.task_tracker.mapper.v1;

import com.example.task_tracker.entity.Task;
import com.example.task_tracker.web.dto.v1.TaskListResponse;
import com.example.task_tracker.web.dto.v1.TaskResponse;
import com.example.task_tracker.web.dto.v1.TaskUpsertRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    Task requestToTask(TaskUpsertRequest request);

    @Mapping(source = "taskId", target = "id")
    Task requestToTask(String taskId, TaskUpsertRequest request);

    TaskResponse taskToResponse(Task task);

    default TaskListResponse taskListToTaskListResponse(List<Task> tasks) {
        return new TaskListResponse(tasks
                .stream()
                .map(this::taskToResponse)
                .toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "observerIds", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "observers", ignore = true)
    void updateTask(TaskUpsertRequest request, @MappingTarget Task task);
}
