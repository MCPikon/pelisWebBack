package com.mcpikon.pelisWebBack.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mcpikon.pelisWebBack.dtos.MovieDTO;
import com.mcpikon.pelisWebBack.models.Movie;
import com.mcpikon.pelisWebBack.services.MovieService;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Movies", description = "Movies management API endpoints.")
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Operation(summary = "Fetch all movies", description = "fetches all movies and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Movie.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "Empty List")
    })
    @GetMapping("/findAll")
    public ResponseEntity<List<Movie>> findAll() {
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Fetch movie by id", description = "fetch a movie and their data filtering by id key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(schema = @Schema(implementation = Movie.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/findById/{id}")
    public ResponseEntity<Optional<Movie>> findById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(movieService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Fetch movie by ImdbId", description = "fetch a movie and their data filtering by ImdbId key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(schema = @Schema(implementation = Movie.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/findByImdbId/{imdbId}")
    public ResponseEntity<Optional<Movie>> findByImdbId(@PathVariable String imdbId) {
        return new ResponseEntity<>(movieService.findByImdbId(imdbId), HttpStatus.OK);
    }

    @Operation(summary = "Post new movie", description = "Post new movie into the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(schema = @Schema(implementation = Movie.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request (A movie or series with the ImdbId passed already exists)")
    })
    @PostMapping("/save")
    public ResponseEntity<Movie> save(@Valid @RequestBody MovieDTO movieDTO) {
        return new ResponseEntity<>(movieService.save(movieDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete movie by id", description = "Delete movie with the id key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(schema = @Schema(implementation = Json.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request (The movie with the id passed doesn't exists)")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable ObjectId id) {
        return new ResponseEntity<>(movieService.delete(id), HttpStatus.OK);
    }

    @Operation(summary = "Update movie by id", description = "Update a movie with the id key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(schema = @Schema(implementation = Movie.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request (The movie with the id passed doesn't exists)")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Movie> update(@PathVariable ObjectId id, @Valid @RequestBody MovieDTO movieDTO) {
        return new ResponseEntity<>(movieService.update(id, movieDTO), HttpStatus.OK);
    }

    @Operation(summary = "Patch movie by id", description = "Patch a movie with the fields and id key passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = { @Content(schema = @Schema(implementation = Movie.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request (The movie with the id passed doesn't exists)")
    })
    @PatchMapping("/patch/{id}")
    public ResponseEntity<Movie> patch(@PathVariable ObjectId id, @RequestBody JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        return new ResponseEntity<>(movieService.patch(id, jsonPatch), HttpStatus.OK);
    }
}