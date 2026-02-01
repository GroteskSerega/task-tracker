package com.example.task_tracker.web.handler.v1;

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
import reactor.core.publisher.Mono;

import java.net.URI;

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
        return serverRequest.bodyToMono(UserUpsertRequest.class)
                .flatMap(validatorHandler::validate)
                .map(userMapper::requestToUser)
                .flatMap(userService::save)
                .flatMap(user ->
                        ServerResponse.created(URI.create("/api/v1/user/" +
                                        user.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .build());
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserUpsertRequest.class)
                .flatMap(validatorHandler::validate)
                .map(request ->
                        userMapper.requestToUser(serverRequest.pathVariable("id"),
                                request))
                .flatMap(userService::update)
                .flatMap(user ->
                        ServerResponse.ok()
                                .header("Location", "/api/v1/user/" +
                                        serverRequest.pathVariable("id"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .build());
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        return ServerResponse.noContent()
                .build(userService.deleteById(
                        serverRequest.pathVariable("id")
                        )
                );
    }
}
