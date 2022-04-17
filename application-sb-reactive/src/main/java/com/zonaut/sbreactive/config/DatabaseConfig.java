package com.zonaut.sbreactive.config;

import com.zonaut.common.database.GenericJsonbCodec;
import com.zonaut.sbreactive.config.database.PostgresqlR2dbcProperties;
import com.zonaut.sbreactive.config.database.converters.JsonToTodoDataConverter;
import com.zonaut.sbreactive.config.database.converters.TodoDataToJsonConverter;
import com.zonaut.sbreactive.domain.TodoData;
import com.zonaut.sbreactive.types.TodoPriority;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Configuration
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    private final PostgresqlR2dbcProperties properties;

    public DatabaseConfig(PostgresqlR2dbcProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Override
    @NonNull
    public ConnectionFactory connectionFactory() {
        PostgresqlConnectionFactory connectionFactory = getConnectionFactory();
        ConnectionPoolConfiguration configuration = getConnectionPoolConfiguration(connectionFactory);
        return new ConnectionPool(configuration);
    }

    @Bean
    @Override
    @NonNull
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();

        // enum types
        converters.add(new TodoPriorityConverter());

        // json data types
        converters.add(new TodoDataToJsonConverter());
        converters.add(new JsonToTodoDataConverter());

        return new R2dbcCustomConversions(getStoreConversions(), converters);
    }

    // @formatter:off
    static class TodoPriorityConverter extends EnumWriteSupport<TodoPriority> {}
    // @formatter:on

    private PostgresqlConnectionFactory getConnectionFactory() {
        return new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(properties.getHost())
                .port(properties.getPort())
                .database(properties.getDatabase())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .codecRegistrar(
                    EnumCodec.builder()
                        // Important, enum types need to be written lower case here.
                        // The names must match exactly as found under object types in your PostgreSQL instance.
                        .withEnum(TodoPriority.OBJECT_TYPE, TodoPriority.class)
                        .build()
                )
                .codecRegistrar(
                    GenericJsonbCodec.builder()
                        .withJsonDataType(TodoData.class)
                        .build()
                )
                .build()
        );
    }

    private ConnectionPoolConfiguration getConnectionPoolConfiguration(PostgresqlConnectionFactory connectionFactory) {
        return ConnectionPoolConfiguration.builder(connectionFactory)
            .maxIdleTime(Duration.ofMinutes(30))
            .initialSize(10)
            .maxSize(10)
            .maxCreateConnectionTime(Duration.ofSeconds(1))
            .build();
    }
}
