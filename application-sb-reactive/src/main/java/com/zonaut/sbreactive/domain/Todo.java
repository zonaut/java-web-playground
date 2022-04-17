package com.zonaut.sbreactive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zonaut.common.view.ViewObject;
import com.zonaut.sbreactive.types.TodoPriority;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Table(Todo.TABLE_NAME)
@Getter
public class Todo extends ViewObject implements Persistable<UUID> {

    public static final String TABLE_NAME = "todos";

    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATA = "data";
    public static final String PRIORITY = "priority";
    public static final String DONE = "done";

    @Id
    @Column(ID)
    private UUID id;

    @Column(CREATED_AT)
    private Instant createdAt;

    @Column(TITLE)
    private String title;

    @Column(CONTENT)
    private String content;

    @Column(DATA)
    private TodoData data;

    @Column(PRIORITY)
    private TodoPriority priority;

    @Column(DONE)
    private boolean done;

    private Todo() {
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return this.createdAt == null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    private Todo(Builder builder) {
        id = builder.id;
        createdAt = builder.createdAt;
        title = builder.title;
        content = builder.content;
        data = builder.data;
        priority = builder.priority;
        done = builder.done;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Todo todo = (Todo) o;
        return id.equals(todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Todo{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", done=" + done +
            '}';
    }

    public static final class Builder {
        private UUID id;
        private Instant createdAt;
        private String title;
        private String content;
        private TodoData data;
        private TodoPriority priority;
        private boolean done;

        private Builder() {
        }

        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder withCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withData(TodoData data) {
            this.data = data;
            return this;
        }

        public Builder withPriority(TodoPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder withDone(boolean done) {
            this.done = done;
            return this;
        }

        public Todo build() {
            return new Todo(this);
        }
    }
}
