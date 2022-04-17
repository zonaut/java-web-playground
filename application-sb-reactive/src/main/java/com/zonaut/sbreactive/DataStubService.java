package com.zonaut.sbreactive;

import com.zonaut.sbreactive.domain.Todo;
import com.zonaut.sbreactive.domain.TodoData;
import com.zonaut.sbreactive.repositories.TodoRepository;
import com.zonaut.sbreactive.types.TodoPriority;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static com.zonaut.common.utils.EnumUtil.randomEnum;

@Service
public class DataStubService implements CommandLineRunner {

    private final TodoRepository todoRepository;

    public DataStubService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(String... args) {
        insertTodos();
    }

    private void insertTodos() {
        Flux.range(1, 100)
                .map(i -> Todo.newBuilder()
                        .withId(UUID.randomUUID())
                        .withTitle(UUID.randomUUID().toString())
                        .withContent(UUID.randomUUID().toString())
                        .withPriority(randomEnum(TodoPriority.class))
                        .withData(TodoData.builder()
                                .title(UUID.randomUUID().toString())
                                .somethingElse(UUID.randomUUID().toString())
                                .build())
                        .withDone(false)
                        .build())
                .flatMap(this.todoRepository::save)
                .doOnComplete(() -> System.out.println("Inserted stub todos"))
                .subscribe();
    }

}
