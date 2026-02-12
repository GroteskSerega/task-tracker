package com.example.task_tracker.web.dto.v1;

import com.example.task_tracker.entity.TaskStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.example.task_tracker.web.dto.FieldsSizes.*;
import static com.example.task_tracker.web.dto.RegexpDto.*;
import static com.example.task_tracker.web.dto.v1.TaskErrorMessageTemplates.*;

public record TaskUpsertRequest (
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_NAME_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_NAME_INCORRECT_REGEX)
        String name,

        @Size(min = BIG_TEXT_SIZE_MIN, max = BIG_TEXT_SIZE_MAX, message = VALIDATE_DESCRIPTION_INCORRECT_SIZE)
        @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_DESCRIPTION_INCORRECT_REGEX)
        String description,

        TaskStatus status,

        @Pattern(regexp = LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ASSIGNEE_ID_INCORRECT_REGEX)
        String assigneeId
) {
}
