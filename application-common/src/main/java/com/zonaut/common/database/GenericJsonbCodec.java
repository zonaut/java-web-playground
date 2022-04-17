package com.zonaut.common.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.r2dbc.postgresql.client.Parameter;
import io.r2dbc.postgresql.codec.Codec;
import io.r2dbc.postgresql.extension.CodecRegistrar;
import io.r2dbc.postgresql.message.Format;
import io.r2dbc.postgresql.util.ByteBufUtils;
import org.postgresql.core.Oid;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

import static com.zonaut.common.Common.OBJECT_MAPPER;

public final class GenericJsonbCodec<T extends JsonDataType> implements Codec<T> {

    private final ByteBufAllocator byteBufAllocator;
    private final Class<T> type;
    private final int oid;

    public GenericJsonbCodec(ByteBufAllocator byteBufAllocator, Class<T> type, int oid) {
        this.byteBufAllocator = byteBufAllocator;
        this.type = type;
        this.oid = oid;
    }

    @Override
    public boolean canDecode(int dataType, @NonNull Format format, Class<?> type) {
        return type.isAssignableFrom(this.type) && dataType == this.oid;
    }

    @Override
    public boolean canEncode(@NonNull Object value) {
        return this.type.isInstance(value);
    }

    @Override
    public boolean canEncodeNull(@NonNull Class<?> type) {
        return this.type.equals(type);
    }

    @Override
    public T decode(ByteBuf buffer, int dataType, @NonNull Format format, @NonNull Class<? extends T> type) {
        if (buffer == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(ByteBufUtils.decode(buffer), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    @NonNull
    public Parameter encode(@NonNull Object value) {
        return new Parameter(Format.FORMAT_BINARY, this.oid, Mono.fromSupplier(() -> {
            try {
                return ByteBufUtils.encode(this.byteBufAllocator, OBJECT_MAPPER.writeValueAsString(value));
            } catch (JsonProcessingException e) {
                throw new RuntimeException();
            }
        }));
    }

    @Override
    @NonNull
    public Parameter encodeNull() {
        return new Parameter(Format.FORMAT_BINARY, this.oid, Parameter.NULL_VALUE);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Set<Class<? extends JsonDataType>> mappings = new HashSet<>();

        public Builder withJsonDataType(Class<? extends JsonDataType> type) {
            this.mappings.add(type);
            return this;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public CodecRegistrar build() {
            return (connection, allocator, registry) -> {
                mappings.forEach(type -> {
                    registry.addLast(new GenericJsonbCodec(allocator, type, Oid.JSONB));
                });
                return Mono.empty();
            };
        }

    }

}
