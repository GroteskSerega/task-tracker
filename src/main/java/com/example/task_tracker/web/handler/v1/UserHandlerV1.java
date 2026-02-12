package com.example.task_tracker.web.handler.v1;

import com.example.task_tracker.aop.AuthoriseUsernameForUserUpdateAndDelete;
import com.example.task_tracker.entity.RoleType;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.exception.ValidationException;
import com.example.task_tracker.mapper.v1.UserMapper;
import com.example.task_tracker.services.UserService;
import com.example.task_tracker.web.dto.v1.UserResponse;
import com.example.task_tracker.web.dto.v1.UserUpsertRequest;
import com.example.task_tracker.web.handler.ValidatorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserHandlerV1 {

    private final ValidatorHandler validatorHandler;

    private final UserMapper userMapper;

    private final UserService userService;

    public Mono<ServerResponse> getAllUser(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findAll()
                                .map(userMapper::userToResponse),
                        UserResponse.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findById(
                        serverRequest.pathVariable("id"))
                                .map(userMapper::userToResponse),
                        UserResponse.class);
    }

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return extractRole(serverRequest)
                .flatMap(roleFromParam ->
                        serverRequest.bodyToMono(UserUpsertRequest.class)
                                .flatMap(validatorHandler::validate)
                                .map(request -> {
                                    User user = userMapper.requestToUser(request);
                                    user.setRoles(Collections.singleton(roleFromParam));
                                    return user;
                                })
                                .flatMap(userService::save)
                                .flatMap(user -> {
                                    var location = UriComponentsBuilder.fromPath("/api/v1/user/{id}")
                                            .buildAndExpand(user.getId())
                                            .toUri();

                                    return ServerResponse.created(location)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .build();
                                }));
    }

    @AuthoriseUsernameForUserUpdateAndDelete
    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        return extractRole(serverRequest)
                .flatMap(roleFromParam ->
                        serverRequest.bodyToMono(UserUpsertRequest.class)
                                .flatMap(validatorHandler::validate)
                                .flatMap(request -> userService.update(serverRequest.pathVariable("id"),
                                            request,
                                            roleFromParam)
                                )
                                .flatMap(user -> {
                                    var location =
                                            UriComponentsBuilder.fromPath("/api/v1/user/{id}")
                                                    .buildAndExpand(user.getId())
                                                    .toUri();

                                    return ServerResponse.ok()
                                            .header("Location",
                                                    location.toString())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .build();
                                }));
    }

    @AuthoriseUsernameForUserUpdateAndDelete
    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        return ServerResponse.noContent()
                .build(userService.deleteById(
                        serverRequest.pathVariable("id")
                        )
                );
    }

    private Mono<RoleType> extractRole(ServerRequest request) {
        return Mono.justOrEmpty(request.queryParam("role"))
                .map(RoleType::valueOf)
                .switchIfEmpty(Mono.error(new ValidationException("Query parameter 'role' is required")))
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.error(new ValidationException("Invalid role type: " + e.getMessage())));
    }
}
