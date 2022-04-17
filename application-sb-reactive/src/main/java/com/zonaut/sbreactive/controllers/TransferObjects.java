package com.zonaut.sbreactive.controllers;

import javax.validation.constraints.NotBlank;

public class TransferObjects {

    public static final String VALIDATION_ERROR_TODO_INVALID_TITLE = "todo.invalid.title";
    public static final String VALIDATION_ERROR_TODO_DUPLICATE_TITLE = "todo.duplicate.title";

    // Errors
    record ValidationError(String field, String error) {}

    // Todos
    record CreateTodoTO(@NotBlank(message = VALIDATION_ERROR_TODO_INVALID_TITLE) String title) {}
    record UpdateTodoTO(@NotBlank(message = VALIDATION_ERROR_TODO_INVALID_TITLE) String title) {}

}