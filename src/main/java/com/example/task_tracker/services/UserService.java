package com.example.task_tracker.services;

import com.example.task_tracker.entity.RoleType;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.web.dto.v1.UserUpsertRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<User> findByUsername(String username);

    Mono<User> save(User user);

    Mono<User> update(String userId, UserUpsertRequest request, RoleType roleFromParam);

    Mono<Void> deleteById(String id);
}
