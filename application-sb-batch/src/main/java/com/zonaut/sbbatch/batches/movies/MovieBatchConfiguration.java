package com.zonaut.sbbatch.batches.movies;

import com.zonaut.sbbatch.domain.Movie;
import com.zonaut.sbbatch.domain.MovieGenre;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

import static com.zonaut.common.Common.OBJECT_MAPPER;

@Log4j2
@Configuration
@EnableBatchProcessing
public class MovieBatchConfiguration {

    public static final int READ_CHUNK_SIZE = 20;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MovieStepListener movieStepListener;

    @Value("/movies.json")
    private Resource inputResource;

    public MovieBatchConfiguration(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory,
                                   MovieStepListener movieStepListener) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.movieStepListener = movieStepListener;
    }

    @Bean
    public Job readMovieJsonResourceJob() {
        return jobBuilderFactory
                .get("readMovieJsonResourceJob")
                .incrementer(new RunIdIncrementer())
                .flow(movieStep())
                .end()
                .build();
    }

    @Bean
    public Step movieStep() {
        return stepBuilderFactory
                .get("movieStep")
                .<Movie, MovieGenre>chunk(READ_CHUNK_SIZE)
                .reader(jsonItemReader())
                .processor(movieListItemProcessor())
                .writer(consoleWriter())
                .listener(movieStepListener)
                .build();
    }

    @Bean
    public JsonItemReader<Movie> jsonItemReader() {
        JacksonJsonObjectReader<Movie> jsonObjectReader = new JacksonJsonObjectReader<>(Movie.class);
        jsonObjectReader.setMapper(OBJECT_MAPPER);

        return new JsonItemReaderBuilder<Movie>()
                .jsonObjectReader(jsonObjectReader)
                .resource(inputResource)
                .name("jsonItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<Movie, MovieGenre> movieListItemProcessor() {
        return new MovieProcessor();
    }

    @Bean
    public ConsoleItemWriter<MovieGenre> consoleWriter() {
        return new ConsoleItemWriter<>();
    }

    @Log4j2
    static class ConsoleItemWriter<T> implements ItemWriter<T> {

        @Override
        public void write(List<? extends T> items) {
            log.info("Chunk starts ...");
            for (T item : items) {
                log.info(item.toString());
            }
            log.info("Chunk ends");

        }
    }

}
