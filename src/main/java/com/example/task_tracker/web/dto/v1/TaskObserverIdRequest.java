package com.example.task_tracker.web.dto.v1;

import jakarta.validation.constraints.Pattern;

import static com.example.task_tracker.web.dto.RegexpDto.LATIN_DIGITS_REGEX;
import static com.example.task_tracker.web.dto.v1.TaskErrorMessageTemplates.VALIDATE_OBSERVER_ID_INCORRECT_REGEX;

public record TaskObserverIdRequest (
        @Pattern(regexp = LATIN_DIGITS_REGEX, message = VALIDATE_OBSERVER_ID_INCORRECT_REGEX)
        String observerId
) {
}
