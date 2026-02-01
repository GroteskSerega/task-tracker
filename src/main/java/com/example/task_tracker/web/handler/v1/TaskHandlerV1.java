package com.example.task_tracker.web.handler.v1;

import com.example.task_tracker.mapper.v1.TaskMapper;
import com.example.task_tracker.services.TaskService;
import com.example.task_tracker.web.dto.v1.TaskObserverIdRequest;
import com.example.task_tracker.web.dto.v1.TaskResponse;
import com.example.task_tracker.web.dto.v1.TaskUpsertRequest;
import com.example.task_tracker.web.handler.ValidatorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskHandlerV1 {

    private final ValidatorHandler validatorHandler;

    private final TaskMapper taskMapper;

    private final TaskService taskService;

    public Mono<ServerResponse> getAllTask(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskService.findAll()
                        .map(taskMapper::taskToResponse),
                        TaskResponse.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskService.findById(
                        serverRequest.pathVariable("id"))
                        .map(taskMapper::taskToResponse),
                        TaskResponse.class);
    }

    public Mono<ServerResponse> createTask(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TaskUpsertRequest.class)
                .flatMap(validatorHandler::validate)
                .map(taskMapper::requestToTask)
                .flatMap(taskService::save)
                .flatMap(task ->
                        ServerResponse.created(URI.create("/api/v1/task/" + task.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .build());
    }

    public Mono<ServerResponse> updateTask(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TaskUpsertRequest.class)
                .flatMap(validatorHandler::validate)
                .map(request ->
                        taskMapper.requestToTask(serverRequest.pathVariable("id"),
                                request))
                .flatMap(taskService::update)
                .flatMap(task ->
                        ServerResponse.ok()
                                .header("Location", "/api/v1/task/" +
                                        serverRequest.pathVariable("id"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .build());
    }

    public Mono<ServerResponse> addObserver(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TaskObserverIdRequest.class)
                .flatMap(validatorHandler::validate)
                .map(TaskObserverIdRequest::getObserverId)
                .flatMap(observerId ->
                        taskService.addObserver(serverRequest.pathVariable("id"), observerId))
                .flatMap(task ->
                        ServerResponse.ok()
                                .header("Location", "/api/v1/task/" +
                                        serverRequest.pathVariable("id"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .build());
    }

    public Mono<ServerResponse> deleteTask(ServerRequest serverRequest) {
        return ServerResponse.noContent()
                .build(taskService.deleteById(
                        serverRequest.pathVariable("id")
                ));
    }
}
