package com.zonaut.sbbatch;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.zonaut.common.stubs.StubGeneratorUtil.*;

public class MovieStubGenerator {

    public static final int NUMBER_OF_OBJECTS = 100;
    public static final boolean PRETTY_PRINT = false;

    public static void main(String[] args) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        generateFile(jsonFactory, getAbsoluteFilePathToMoviesJsonFile());
    }

    private static void generateFile(JsonFactory jsonFactory, String filePath) throws IOException {
        try (OutputStream fos = new FileOutputStream(filePath, false);
             JsonGenerator generator = jsonFactory.createGenerator(fos, JsonEncoding.UTF8)) {

            generator.setPrettyPrinter(PRETTY_PRINT ? new DefaultPrettyPrinter() : null);

            generator.writeStartArray();
            for (int x = 0; x < NUMBER_OF_OBJECTS; x++) {
                writeObject(generator);
            }
            generator.writeEndArray();
        }
    }

    private static void writeObject(JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        writeString(generator, "title");
        writeNumber(generator, "year");
        writeArray(generator, "cast");
        writeArray(generator, "genres");
        generator.writeEndObject();
    }

    private static void writeString(JsonGenerator generator, String field) throws IOException {
        generator.writeStringField(field, randomWordsAsString(1));
    }

    private static void writeNumber(JsonGenerator generator, String field) throws IOException {
        generator.writeNumberField(field, randomIntBetween(1970, 2022));
    }

    private static void writeArray(JsonGenerator generator, String genres) throws IOException {
        generator.writeFieldName(genres);
        generator.writeStartArray();
        for (String arg : randomWords(3)) {
            generator.writeString(arg);
        }
        generator.writeEndArray();
    }

    private static String getAbsoluteFilePathToMoviesJsonFile() {
        String ps = File.separator;
        String baseFilePath = String.format("application-sb-batch%ssrc%smain%sresources%s", ps, ps, ps, ps);
        Path relativePath = Paths.get(baseFilePath, "movies.json");
        return relativePath.toAbsolutePath().toString();
    }

}
