package com.example.task_tracker.services;

import com.example.task_tracker.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<User> save(User user);

    Mono<User> update(User user);

    Mono<Void> deleteById(String id);
}
