package com.example.task_tracker.web.dto.v1;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.task_tracker.web.dto.RegexpDto.LATIN_DIGITS_REGEX;
import static com.example.task_tracker.web.dto.v1.TaskErrorMessageTemplates.VALIDATE_OBSERVER_ID_INCORRECT_REGEX;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskObserverIdRequest {

    @Pattern(regexp = LATIN_DIGITS_REGEX, message = VALIDATE_OBSERVER_ID_INCORRECT_REGEX)
    private String observerId;
}
