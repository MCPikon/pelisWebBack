package com.mcpikon.pelisWebBack.utils;

import com.mcpikon.pelisWebBack.dtos.MovieDTO;
import com.mcpikon.pelisWebBack.dtos.ReviewDTO;
import com.mcpikon.pelisWebBack.dtos.ReviewSaveDTO;
import com.mcpikon.pelisWebBack.dtos.SeriesDTO;
import com.mcpikon.pelisWebBack.models.Movie;
import com.mcpikon.pelisWebBack.models.Review;
import com.mcpikon.pelisWebBack.models.Series;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DTOMapper {

    private DTOMapper() {}

    // Movies DTOs Converters
    public static Movie dtoToMovie(MovieDTO movieDTO) {
        return Movie.builder()
                .imdbId(movieDTO.imdbId())
                .title(movieDTO.title())
                .overview(movieDTO.overview())
                .duration(movieDTO.duration())
                .director(movieDTO.director())
                .releaseDate(movieDTO.releaseDate())
                .trailerLink(movieDTO.trailerLink())
                .genres(movieDTO.genres())
                .poster(movieDTO.poster())
                .backdrop(movieDTO.backdrop())
                .reviewIds(new ArrayList<>()).build();
    }

    public static Movie dtoToMovieUpdate(Movie movie, MovieDTO movieDTO) {
        return Movie.builder()
                .id(movie.getId())
                .imdbId(movieDTO.imdbId())
                .title(movieDTO.title())
                .overview(movieDTO.overview())
                .duration(movieDTO.duration())
                .director(movieDTO.director())
                .releaseDate(movieDTO.releaseDate())
                .trailerLink(movieDTO.trailerLink())
                .genres(movieDTO.genres())
                .poster(movieDTO.poster())
                .backdrop(movieDTO.backdrop())
                .reviewIds(movie.getReviewIds()).build();
    }

    // Series DTOs Converters
    public static Series dtoToSeries(SeriesDTO seriesDTO) {
        return Series.builder()
                .imdbId(seriesDTO.imdbId())
                .title(seriesDTO.title())
                .overview(seriesDTO.overview())
                .numberOfSeasons(seriesDTO.numberOfSeasons())
                .creator(seriesDTO.creator())
                .releaseDate(seriesDTO.releaseDate())
                .trailerLink(seriesDTO.trailerLink())
                .genres(seriesDTO.genres())
                .seasonList(seriesDTO.seasonList())
                .poster(seriesDTO.poster())
                .backdrop(seriesDTO.backdrop())
                .reviewIds(new ArrayList<>()).build();
    }

    public static Series dtoToSeriesUpdate(Series series, SeriesDTO seriesDTO) {
        return Series.builder()
                .id(series.getId())
                .imdbId(seriesDTO.imdbId())
                .title(seriesDTO.title())
                .overview(seriesDTO.overview())
                .numberOfSeasons(seriesDTO.numberOfSeasons())
                .creator(seriesDTO.creator())
                .releaseDate(seriesDTO.releaseDate())
                .trailerLink(seriesDTO.trailerLink())
                .genres(seriesDTO.genres())
                .seasonList(seriesDTO.seasonList())
                .poster(seriesDTO.poster())
                .backdrop(seriesDTO.backdrop())
                .reviewIds(series.getReviewIds()).build();
    }

    // Review DTOs Converters
    public static Review dtoToReview(ReviewSaveDTO reviewSaveDTO) {
        return Review.builder()
                .title(reviewSaveDTO.title())
                .body(reviewSaveDTO.body())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Review dtoToReviewUpdate(Review review, ReviewDTO reviewDTO) {
        return Review.builder()
                .id(review.getId())
                .title(reviewDTO.title())
                .body(reviewDTO.body())
                .createdAt(review.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
