package com.zonaut.sbreactive.controllers;

import com.zonaut.sbreactive.common.exceptions.DuplicateFieldException;
import com.zonaut.sbreactive.controllers.TransferObjects.CreateTodoTO;
import com.zonaut.sbreactive.controllers.TransferObjects.UpdateTodoTO;
import com.zonaut.sbreactive.domain.Todo;
import com.zonaut.sbreactive.repositories.TodoRepository;
import com.zonaut.sbreactive.types.TodoPriority;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

import static com.zonaut.sbreactive.controllers.TodoController.API_V_1_TODOS;
import static com.zonaut.sbreactive.controllers.TransferObjects.VALIDATION_ERROR_TODO_DUPLICATE_TITLE;
import static com.zonaut.sbreactive.domain.Todo.TITLE;
import static org.springframework.http.HttpStatus.CREATED;

@Log4j2
@RestController
@RequestMapping(API_V_1_TODOS)
public class TodoController {

    public static final String API_V_1_TODOS = "/api/v1/todos";

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping()
    public Flux<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Todo>> getTodoById(@PathVariable("id") UUID id) {
        return todoRepository.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public Mono<Todo> createTodo(@Valid @RequestBody CreateTodoTO createTodoTO) {
        Todo todo = Todo.newBuilder()
            .withId(UUID.randomUUID())
            .withTitle(createTodoTO.title())
            .withContent(createTodoTO.title())
            .withPriority(TodoPriority.NORMAL)
            .withDone(false)
            .build();

        return todoRepository.save(todo)
            .onErrorMap(throwable -> {
                if (throwable instanceof DataIntegrityViolationException) {
                    throw new DuplicateFieldException(TITLE, VALIDATION_ERROR_TODO_DUPLICATE_TITLE);
                }
                throw new RuntimeException(throwable);
            });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Todo>> updateTodo(@Valid @RequestBody UpdateTodoTO updateTodoTO, @PathVariable UUID id) {
        return todoRepository.findById(id)
            .flatMap(existingTodo -> {
                existingTodo.setTitle(updateTodoTO.title());
                existingTodo.setDone(true);
                return todoRepository.save(existingTodo);
            })
            .map(ResponseEntity::ok)
            .onErrorMap(throwable -> {
                if (throwable instanceof DataIntegrityViolationException) {
                    throw new DuplicateFieldException(TITLE, VALIDATION_ERROR_TODO_DUPLICATE_TITLE);
                }
                throw new RuntimeException(throwable);
            })
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTodo(@PathVariable UUID id) {
        return todoRepository.findById(id)
            .flatMap(todo -> todoRepository.delete(todo)
                .then(Mono.just(todo)))
            .map(r -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
