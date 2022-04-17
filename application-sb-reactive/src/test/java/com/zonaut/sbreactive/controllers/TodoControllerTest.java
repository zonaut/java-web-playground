package com.zonaut.sbreactive.controllers;

import com.zonaut.sbreactive.controllers.TransferObjects.CreateTodoTO;
import com.zonaut.sbreactive.controllers.TransferObjects.UpdateTodoTO;
import com.zonaut.sbreactive.controllers.TransferObjects.ValidationError;
import com.zonaut.sbreactive.domain.Todo;
import com.zonaut.sbreactive.extensions.FixedPortDatabaseTestContainerExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.zonaut.sbreactive.controllers.TodoController.API_V_1_TODOS;
import static com.zonaut.sbreactive.controllers.TransferObjects.VALIDATION_ERROR_TODO_DUPLICATE_TITLE;
import static com.zonaut.sbreactive.controllers.TransferObjects.VALIDATION_ERROR_TODO_INVALID_TITLE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(FixedPortDatabaseTestContainerExtension.class)
public class TodoControllerTest {

    public static final String API_V_1_TODOS_ON_ID = API_V_1_TODOS + "/{id}";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAllTodos() {
        Todo createdTodo = createTodo(new CreateTodoTO(UUID.randomUUID().toString()));

        webTestClient.get().uri(API_V_1_TODOS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Todo.class)
                .contains(createdTodo);
    }

    @Test
    public void getTodoOnId() {
        Todo createdTodo = createTodo(new CreateTodoTO(UUID.randomUUID().toString()));

        webTestClient.get()
                .uri(API_V_1_TODOS_ON_ID, createdTodo.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class)
                .consumeWith(response -> {
                    Assertions.assertThat(response.getResponseBody()).isNotNull();
                    Assertions.assertThat(response.getResponseBody().getId()).isEqualTo(createdTodo.getId());
                    Assertions.assertThat(response.getResponseBody().getTitle()).isEqualTo(createdTodo.getTitle());
                });
    }

    @Test
    public void updateTodo() {
        Todo createdTodo = createTodo(new CreateTodoTO(UUID.randomUUID().toString()));
        String newTitle = UUID.randomUUID().toString();
        UpdateTodoTO updateTodoTO = new UpdateTodoTO(newTitle);

        webTestClient.put()
                .uri(API_V_1_TODOS_ON_ID, createdTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateTodoTO), UpdateTodoTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class)
                .consumeWith(updateResponse -> {
                    Assertions.assertThat(updateResponse.getResponseBody()).isNotNull();
                    Assertions.assertThat(updateResponse.getResponseBody().getTitle()).isEqualTo(newTitle);
                    Assertions.assertThat(updateResponse.getResponseBody().isDone()).isEqualTo(true);
                });
    }

    @Test
    public void updateTodo_withInvalidTitle_expectErrors() {
        Todo createdTodo = createTodo(new CreateTodoTO(UUID.randomUUID().toString()));
        UpdateTodoTO invalidUpdateTodoTO = new UpdateTodoTO("");

        webTestClient.put()
                .uri(API_V_1_TODOS_ON_ID, createdTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidUpdateTodoTO), UpdateTodoTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBodyList(ValidationError.class)
                .consumeWith(errors -> {
                    Assertions.assertThat(errors.getResponseBody()).isNotNull();
                    Assertions.assertThat(errors.getResponseBody().size()).isEqualTo(1);
                    Assertions.assertThat(errors.getResponseBody().get(0).field()).isEqualTo(Todo.TITLE);
                    Assertions.assertThat(errors.getResponseBody().get(0).error()).isEqualTo(VALIDATION_ERROR_TODO_INVALID_TITLE);
                });
    }

    @Test
    public void updateTodo_withDuplicateTitle_expectError() {
        Todo createdTodo = createTodo(new CreateTodoTO(UUID.randomUUID().toString()));

        String anotherTitle = UUID.randomUUID().toString();
        createTodo(new CreateTodoTO(anotherTitle));

        UpdateTodoTO updateTodoTO = new UpdateTodoTO(anotherTitle);

        webTestClient.put()
                .uri(API_V_1_TODOS_ON_ID, createdTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateTodoTO), UpdateTodoTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBodyList(ValidationError.class)
                .consumeWith(errors -> {
                    Assertions.assertThat(errors.getResponseBody()).isNotNull();
                    Assertions.assertThat(errors.getResponseBody().size()).isEqualTo(1);
                    Assertions.assertThat(errors.getResponseBody().get(0).field()).isEqualTo(Todo.TITLE);
                    Assertions.assertThat(errors.getResponseBody().get(0).error()).isEqualTo(VALIDATION_ERROR_TODO_DUPLICATE_TITLE);
                });
    }

    @Test
    public void deleteTodo() {
        Todo createdTodo = createTodo(new CreateTodoTO(UUID.randomUUID().toString()));

        webTestClient.delete()
                .uri(API_V_1_TODOS_ON_ID, createdTodo.getId())
                .exchange()
                .expectStatus().isOk();
        webTestClient.get()
                .uri(API_V_1_TODOS_ON_ID, createdTodo.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createTodo_withInvalidTitle_expectErrors() {
        CreateTodoTO invalidCreateTodoTO = new CreateTodoTO("");

        webTestClient.post().uri(API_V_1_TODOS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidCreateTodoTO), CreateTodoTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBodyList(ValidationError.class)
                .consumeWith(errors -> {
                    Assertions.assertThat(errors.getResponseBody()).isNotNull();
                    Assertions.assertThat(errors.getResponseBody().size()).isEqualTo(1);
                    Assertions.assertThat(errors.getResponseBody().get(0).field()).isEqualTo(Todo.TITLE);
                    Assertions.assertThat(errors.getResponseBody().get(0).error()).isEqualTo(VALIDATION_ERROR_TODO_INVALID_TITLE);
                });
    }

    @Test
    public void createTodo_withDuplicateTitle_expectError() {
        CreateTodoTO createTodoTO = new CreateTodoTO(UUID.randomUUID().toString());

        createTodo(createTodoTO);

        webTestClient.post().uri(API_V_1_TODOS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(createTodoTO), CreateTodoTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBodyList(ValidationError.class)
                .consumeWith(errors -> {
                    Assertions.assertThat(errors.getResponseBody()).isNotNull();
                    Assertions.assertThat(errors.getResponseBody().size()).isEqualTo(1);
                    Assertions.assertThat(errors.getResponseBody().get(0).field()).isEqualTo(Todo.TITLE);
                    Assertions.assertThat(errors.getResponseBody().get(0).error()).isEqualTo(VALIDATION_ERROR_TODO_DUPLICATE_TITLE);
                });
    }

    private Todo createTodo(CreateTodoTO createTodoTO) {
        return webTestClient.post().uri(API_V_1_TODOS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(createTodoTO), CreateTodoTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Todo.class)
                .consumeWith(response -> {
                    Assertions.assertThat(response.getResponseBody()).isNotNull();
                    Assertions.assertThat(response.getResponseBody().getId()).isNotNull();
                    Assertions.assertThat(response.getResponseBody().getTitle()).isEqualTo(createTodoTO.title());
                })
                .returnResult()
                .getResponseBody();
    }

}
