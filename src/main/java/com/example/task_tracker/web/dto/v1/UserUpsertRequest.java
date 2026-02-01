package com.example.task_tracker.web.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.task_tracker.web.dto.FieldsSizes.NAME_SIZE_MAX;
import static com.example.task_tracker.web.dto.FieldsSizes.NAME_SIZE_MIN;
import static com.example.task_tracker.web.dto.RegexpDto.*;
import static com.example.task_tracker.web.dto.v1.UserErrorMessageTemplates.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpsertRequest {

    @NotBlank(message = VALIDATE_USER_USERNAME_BLANK)
    @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_USER_USERNAME_INCORRECT_SIZE)
    @Pattern(regexp = LATIN_DIGITS_REGEX, message = VALIDATE_USER_USERNAME_INCORRECT_REGEX)
    private String username;

    @NotBlank(message = VALIDATE_USER_EMAIL_BLANK)
    @Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = VALIDATE_USER_EMAIL_INCORRECT_SIZE)
    @Pattern(regexp = EMAIL_REGEX, message = VALIDATE_USER_EMAIL_INCORRECT_REGEX)
    private String email;
}
