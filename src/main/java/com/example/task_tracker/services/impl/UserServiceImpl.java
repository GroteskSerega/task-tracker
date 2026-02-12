package com.example.task_tracker.services.impl;

import com.example.task_tracker.entity.RoleType;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.mapper.v1.UserMapper;
import com.example.task_tracker.repository.UserRepository;
import com.example.task_tracker.services.UserService;
import com.example.task_tracker.web.dto.v1.UserUpsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
//                .switchIfEmpty(Mono.error(
//                        new ResponseStatusException(HttpStatus.NOT_FOUND,
//                                "Entity not found!")));
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<User> save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<User> update(String userId, UserUpsertRequest request, RoleType roleFromParam) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Entity not found!")))
                .flatMap(existingUser -> {
                    userMapper.updateUser(request, existingUser);

                    existingUser.setRoles(Collections.singleton(roleFromParam));

                    if (request.password() != null && !request.password().isBlank()) {
                        existingUser.setPassword(passwordEncoder.encode(request.password()));
                    }

                    return userRepository.save(existingUser);
                });
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
