package com.mcpikon.pelisWebBack.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mcpikon.pelisWebBack.dtos.ReviewDTO;
import com.mcpikon.pelisWebBack.dtos.ReviewSaveDTO;
import com.mcpikon.pelisWebBack.models.Review;
import com.mcpikon.pelisWebBack.services.ReviewService;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Reviews", description = "Reviews management API endpoints.")
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Fetch all reviews", description = "fetches all reviews and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Review.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "Empty List")
    })
    @GetMapping("/findAll")
    public ResponseEntity<List<Review>> findAll() {
        return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Fetch all reviews by ImdbId", description = "fetches all reviews and their data from movie or series with the ImdbId key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Review.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "Empty List")
    })
    @GetMapping("/findAllByImdbId/{imdbId}")
    public ResponseEntity<List<Review>> findAllByImdbId(@PathVariable String imdbId) {
        return new ResponseEntity<>(reviewService.findAllByImdbId(imdbId), HttpStatus.OK);
    }

    @Operation(summary = "Fetch review by id", description = "fetch a review and their data filtering by id key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(schema = @Schema(implementation = Review.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/findById/{id}")
    public ResponseEntity<Optional<Review>> findById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(reviewService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Post review by ImdbId key", description = "Post a review and their data to the movie or series with the ImdbId key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(schema = @Schema(implementation = Review.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request (A series or movie with the ImdbId passed doesn't exists)")
    })
    @PostMapping("/save")
    public ResponseEntity<Review> save(@RequestBody ReviewSaveDTO reviewSaveDTO) {
        return new ResponseEntity<>(reviewService.save(reviewSaveDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete review by id", description = "Delete a review and the id related in movies or series reviewIds array with the id key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(schema = @Schema(implementation = Json.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request (The review with the id passed doesn't exists)")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable ObjectId id) {
        return new ResponseEntity<>(reviewService.delete(id), HttpStatus.OK);
    }

    @Operation(summary = "Update review by id", description = "Update a review with the id key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(schema = @Schema(implementation = Review.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request (The api can't parse the String id to ObjectId)"),
            @ApiResponse(responseCode = "400", description = "Bad Request (The review with the id passed doesn't exists)")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Review> update(@PathVariable ObjectId id, @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.update(id, reviewDTO), HttpStatus.OK);
    }

    @Operation(summary = "Patch review by id", description = "Patch a review with the fields and id key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = {@Content(schema = @Schema(implementation = Review.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request (The review with the id passed doesn't exists)")
    })
    @PatchMapping("/patch/{id}")
    public ResponseEntity<Review> patch(@PathVariable ObjectId id, @RequestBody JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        return new ResponseEntity<>(reviewService.patch(id, jsonPatch), HttpStatus.OK);
    }
}
