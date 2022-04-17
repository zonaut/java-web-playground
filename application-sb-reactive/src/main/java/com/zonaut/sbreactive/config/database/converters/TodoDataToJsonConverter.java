package com.zonaut.sbreactive.config.database.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zonaut.sbreactive.domain.TodoData;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

import static com.zonaut.common.Common.OBJECT_MAPPER;

@Log4j2
@WritingConverter
public class TodoDataToJsonConverter implements Converter<TodoData, Json> {

    @Override
    public Json convert(@NonNull TodoData source) {
        try {
            return Json.of(OBJECT_MAPPER.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            log.error("Error occurred while serializing map to JSON: {}", source, e);
            throw new RuntimeException(e);
        }
    }
}
