package com.mcpikon.pelisWebBack.servicesImpl;

import com.mcpikon.pelisWebBack.entities.Movie;
import com.mcpikon.pelisWebBack.entities.Review;
import com.mcpikon.pelisWebBack.entities.Series;
import com.mcpikon.pelisWebBack.models.ErrorException;
import com.mcpikon.pelisWebBack.models.Errors;
import com.mcpikon.pelisWebBack.repositories.MovieRepository;
import com.mcpikon.pelisWebBack.repositories.ReviewRepository;
import com.mcpikon.pelisWebBack.repositories.SeriesRepository;
import com.mcpikon.pelisWebBack.services.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private SeriesRepository seriesRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Review> findAll() throws ErrorException {
        log.info("GET reviews /findAll executed");
        List<Review> reviews = reviewRepo.findAll();
        if (reviews.isEmpty()) throw new ErrorException(Errors.EMPTY, HttpStatus.NO_CONTENT);
        return reviews;
    }

    @Override
    public List<Review> findAllByImdbId(String imdbId) throws ErrorException {
        log.info("GET reviews /findAllByImdbId executed");
        List<Review> reviews;

        if (movieRepo.existsByImdbId(imdbId)) reviews = movieRepo.findByImdbId(imdbId).orElseThrow().getReviewIds();
        else if (seriesRepo.existsByImdbId(imdbId)) reviews = seriesRepo.findByImdbId(imdbId).orElseThrow().getReviewIds();
        else throw new ErrorException(Errors.NOT_EXISTS, HttpStatus.BAD_REQUEST);

        if (reviews.isEmpty()) throw new ErrorException(Errors.EMPTY, HttpStatus.NO_CONTENT);
        return reviews;
    }

    @Override
    public Optional<Review> findById(ObjectId id) throws ErrorException {
        log.info("GET reviews /findById executed");
        return Optional.ofNullable(reviewRepo.findById(id).orElseThrow(() -> new ErrorException(Errors.NOT_EXISTS, HttpStatus.NOT_FOUND)));
    }

    @Override
    public Review save(String title, String body, String imdbId) {
        log.info("POST reviews /save executed");
        Review review;
        if (movieRepo.existsByImdbId(imdbId)) {
            review = reviewRepo.insert(new Review(title, body, LocalDateTime.now(), LocalDateTime.now()));
            mongoTemplate.update(Movie.class)
                    .matching(Criteria.where("imdbId").is(imdbId))
                    .apply(new Update().push("reviewIds").value(review)).first();
        } else if (seriesRepo.existsByImdbId(imdbId)) {
            review = reviewRepo.insert(new Review(title, body, LocalDateTime.now(), LocalDateTime.now()));
            mongoTemplate.update(Series.class)
                    .matching(Criteria.where("imdbId").is(imdbId))
                    .apply(new Update().push("reviewIds").value(review)).first();
        } else throw new ErrorException(Errors.NOT_EXISTS, HttpStatus.BAD_REQUEST);
        return review;
    }

    @Override
    public Map<String, String> delete(ObjectId id) {
        log.info("DELETE reviews /delete executed");
        Review reviewToDelete = reviewRepo.findById(id).orElseThrow(() -> new ErrorException(Errors.NOT_EXISTS, HttpStatus.BAD_REQUEST));
        reviewRepo.delete(reviewToDelete);
        return Map.of("message", String.format("Review with id: '%s' was successfully deleted", id));
    }

    @Override
    public Review update(ObjectId id, String title, String body) throws ErrorException {
        log.info("PUT reviews /update executed");
        Review reviewToUpdate = reviewRepo.findById(id).orElseThrow(() -> new ErrorException(Errors.NOT_EXISTS, HttpStatus.BAD_REQUEST));
        reviewToUpdate.setUpdatedAt(LocalDateTime.now());
        reviewToUpdate.setTitle(title);
        reviewToUpdate.setBody(body);
        return reviewRepo.save(reviewToUpdate);
    }

    @Override
    public Review patch(ObjectId id, Map<String, String> fields) throws ErrorException {
        log.info("PATCH reviews /patch executed");
        Review reviewToPatch = reviewRepo.findById(id).orElseThrow(() -> new ErrorException(Errors.NOT_EXISTS, HttpStatus.BAD_REQUEST));
        fields.forEach((key, value) -> {
            if (key.equalsIgnoreCase("id") || key.equalsIgnoreCase("imdbId"))
                throw new ErrorException(Errors.ID_CANNOT_CHANGE, HttpStatus.BAD_REQUEST);
            Field field = ReflectionUtils.findField(Review.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, reviewToPatch, value);
            }
        });
        reviewToPatch.setUpdatedAt(LocalDateTime.now());
        return reviewRepo.save(reviewToPatch);
    }
}
