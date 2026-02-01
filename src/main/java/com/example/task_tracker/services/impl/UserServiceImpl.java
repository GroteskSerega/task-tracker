package com.example.task_tracker.services.impl;

import com.example.task_tracker.entity.User;
import com.example.task_tracker.repository.UserRepository;
import com.example.task_tracker.services.UserService;
import com.example.task_tracker.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
//                .switchIfEmpty(Mono.error(
//                        new ResponseStatusException(HttpStatus.NOT_FOUND,
//                                "Entity not found!")));
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<User> update(User user) {
        return findById(user.getId())
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Entity not found!")))
                .flatMap(userForUpdate -> {
                    BeanUtils.copyNonNullProperties(user,
                            userForUpdate);

                    return save(userForUpdate);
                });
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
