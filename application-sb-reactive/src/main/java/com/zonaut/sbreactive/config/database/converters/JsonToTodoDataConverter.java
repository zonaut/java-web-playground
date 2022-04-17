package com.zonaut.sbreactive.config.database.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zonaut.sbreactive.domain.TodoData;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.io.IOException;

import static com.zonaut.common.Common.OBJECT_MAPPER;

@Log4j2
@ReadingConverter
public class JsonToTodoDataConverter implements Converter<Json, TodoData> {

    @Override
    public TodoData convert(Json source) {
        try {
            return OBJECT_MAPPER.readValue(source.asString(), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Problem while parsing JSON: {}", source, e);
            throw new RuntimeException(e);
        }
    }
}
