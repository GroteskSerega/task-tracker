package com.example.task_tracker.web.dto.v1;

import com.example.task_tracker.entity.TaskStatus;
import com.example.task_tracker.web.constraint.ValueOfEnum;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.task_tracker.web.dto.FieldsSizes.*;
import static com.example.task_tracker.web.dto.RegexpDto.*;
import static com.example.task_tracker.web.dto.v1.TaskErrorMessageTemplates.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpsertRequest {

    private String id;

    @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_NAME_INCORRECT_SIZE)
    @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_NAME_INCORRECT_REGEX)
    private String name;

    @Size(min = BIG_TEXT_SIZE_MIN, max = BIG_TEXT_SIZE_MAX, message = VALIDATE_DESCRIPTION_INCORRECT_SIZE)
    @Pattern(regexp = CYRILLIC_LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_DESCRIPTION_INCORRECT_REGEX)
    private String description;

    @ValueOfEnum(enumClass = TaskStatus.class, message = VALIDATE_STATUS_INCORRECT_ENUM)
    private String status;

    @Pattern(regexp = LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_AUTHOR_ID_INCORRECT_REGEX)
    private String authorId;

    @Pattern(regexp = LATIN_DIGITS_SIGNS_REGEX, message = VALIDATE_ASSIGNEE_ID_INCORRECT_REGEX)
    private String assigneeId;
}
