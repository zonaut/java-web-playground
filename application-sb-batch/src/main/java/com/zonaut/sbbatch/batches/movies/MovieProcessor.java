package com.zonaut.sbbatch.batches.movies;

import com.zonaut.sbbatch.domain.Movie;
import com.zonaut.sbbatch.domain.MovieGenre;
import org.springframework.batch.item.ItemProcessor;

public class MovieProcessor implements ItemProcessor<Movie, MovieGenre> {

    @Override
    public MovieGenre process(Movie item) {
        return new MovieGenre(item.getTitle(), item.getGenres().toString());
    }

}
