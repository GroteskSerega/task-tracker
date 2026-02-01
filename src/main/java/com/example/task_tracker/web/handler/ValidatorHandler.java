package com.example.task_tracker.web.handler;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ValidatorHandler {

    private final Validator validator;

    public <T> Mono<T> validate(T body) {
        var violations = validator.validate(body);

        if (violations.isEmpty()) {
            return Mono.just(body);
        }

        String msg = violations
                .stream()
                .map(v ->
                        v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, msg));
    }
}
