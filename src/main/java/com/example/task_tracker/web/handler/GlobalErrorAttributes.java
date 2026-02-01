package com.example.task_tracker.web.handler;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                  ErrorAttributeOptions options) {
        Map<String, Object> map =
                super.getErrorAttributes(request, options);

        Throwable error = getError(request);

        if (error instanceof ResponseStatusException ex) {
            map.put("status", ex.getStatusCode().value());
            map.put("error", "Validation error");
            map.put("message", ex.getReason());
        }

        return map;
    }
}
