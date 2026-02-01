package com.example.task_tracker.web.dto.v1;

public class TaskErrorMessageTemplates {

    public static final String VALIDATE_NAME_INCORRECT_SIZE =
            "Поле Наименование должно содержать от {min} до {max} символов";
    public static final String VALIDATE_NAME_INCORRECT_REGEX =
            "Поле Наименование должно содержать символы латиницы, кириллицы, цифры и знаки препинаний";

    public static final String VALIDATE_DESCRIPTION_BLANK =
            "Поле Описание должно быть заполнено";
    public static final String VALIDATE_DESCRIPTION_INCORRECT_SIZE =
            "Поле Описание должно содержать от {min} до {max} символов";
    public static final String VALIDATE_DESCRIPTION_INCORRECT_REGEX =
            "Поле Описание должно содержать символы латиницы, кириллицы, цифры и знаки препинаний";

    public static final String VALIDATE_STATUS_INCORRECT_ENUM =
            "Поле Статус должно содержать значения: TODO|IN_PROGRESS|DONE";

    public static final String VALIDATE_AUTHOR_ID_INCORRECT_REGEX =
            "Поле ID Автора должно содержать символы латиницы, цифры, знак -";

    public static final String VALIDATE_ASSIGNEE_ID_INCORRECT_REGEX =
            "Поле ID Исполнитель должно содержать символы латиницы, цифры, знак -";

    public static final String VALIDATE_OBSERVER_ID_INCORRECT_REGEX =
            "Поле ID Наблюдателя должно содержать символы латиницы, цифры, знак -";
}
