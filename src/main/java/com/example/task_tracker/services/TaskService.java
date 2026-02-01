package com.example.task_tracker.services;

import com.example.task_tracker.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Flux<Task> findAll();

    Mono<Task> findById(String id);

    Mono<Task> save(Task task);

    Mono<Task> update(Task task);

    Mono<Task> addObserver(String taskId, String observerId);

    Mono<Void> deleteById(String id);
}
