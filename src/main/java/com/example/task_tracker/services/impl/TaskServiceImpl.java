package com.example.task_tracker.services.impl;

import com.example.task_tracker.entity.Task;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.repository.TaskRepository;
import com.example.task_tracker.services.TaskService;
import com.example.task_tracker.services.UserService;
import com.example.task_tracker.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    @Override
    public Flux<Task> findAll() {
        return taskRepository.findAll()
                .flatMap(task ->
                        Mono.zip(fetchUser(task.getAuthorId()),
                                fetchUser(task.getAssigneeId()),
                                fetchObservers(task.getObserverIds())
                                )
                        .map(tuple -> {
                            task.setAuthor(tuple.getT1().orElse(null));
                            task.setAssignee(tuple.getT2().orElse(null));
                            task.setObservers(tuple.getT3());

                            return task;
                        })
                );
    }

    @Override
    public Mono<Task> findById(String id) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Entity not found!")))
                .flatMap(task ->
                        Mono.zip(fetchUser(task.getAuthorId()),
                                fetchUser(task.getAssigneeId()),
                                fetchObservers(task.getObserverIds())
                        )
                        .map(tuple -> {
                            task.setAuthor(tuple.getT1().orElse(null));
                            task.setAssignee(tuple.getT2().orElse(null));
                            task.setObservers(tuple.getT3());

                            return task;
                        })
        );
    }

    @Override
    public Mono<Task> save(Task task) {
        userService.findById(task.getAuthorId());
        return taskRepository.save(task);
    }

    @Override
    public Mono<Task> update(Task task) {
        return findById(task.getId())
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Entity not found!")))
                .flatMap(taskForUpdate -> {
                    BeanUtils.copyNonNullProperties(task,
                            taskForUpdate);

                    return save(taskForUpdate);
                });
    }

    @Override
    public Mono<Task> addObserver(String taskId, String observerId) {
        return findById(taskId)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Entity not found!")))
                .flatMap(task -> {
                    Set<String> ids = task.getObserverIds();
                    if (ids != null && ids.contains(observerId)) {
                        return Mono.empty();
                    }

                    Set<String> updatedIds = (ids == null) ? new HashSet<>() : ids;
                    updatedIds.add(observerId);
                    task.setObserverIds(updatedIds);

                    return save(task);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    private Mono<Optional<User>> fetchUser(String userId) {
        if (userId == null) {
            return Mono.just(Optional.empty());
        }
        return userService.findById(userId)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
    }

    private Mono<Set<User>> fetchObservers(Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.just(Collections.emptySet());
        }

        return Flux.fromIterable(ids)
                .flatMap(userService::findById)
                .collect(Collectors.toSet());
    }
}
