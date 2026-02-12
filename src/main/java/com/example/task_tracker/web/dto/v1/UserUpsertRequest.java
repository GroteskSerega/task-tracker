package com.example.task_tracker.web.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.example.task_tracker.web.dto.FieldsSizes.*;
import static com.example.task_tracker.web.dto.RegexpDto.*;
import static com.example.task_tracker.web.dto.v1.UserErrorMessageTemplates.*;

public record UserUpsertRequest (
        @NotBlank(message = VALIDATE_USER_USERNAME_BLANK)
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_USER_USERNAME_INCORRECT_SIZE)
        @Pattern(regexp = LATIN_DIGITS_REGEX, message = VALIDATE_USER_USERNAME_INCORRECT_REGEX)
        String username,

        @NotBlank(message = VALIDATE_USER_PASSWORD_BLANK)
        @Size(min = PASSWORD_SIZE_MIN, max = PASSWORD_SIZE_MAX, message = VALIDATE_USER_PASSWORD_INCORRECT_SIZE)
        String password,

        @NotBlank(message = VALIDATE_USER_EMAIL_BLANK)
        @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_USER_EMAIL_INCORRECT_SIZE)
        @Pattern(regexp = EMAIL_REGEX, message = VALIDATE_USER_EMAIL_INCORRECT_REGEX)
        String email
) {
}
