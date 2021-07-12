package com.mtheus.netflix.controller;

import com.mtheus.netflix.models.Movie;
import com.mtheus.netflix.models.MovieEvent;
import com.mtheus.netflix.repository.NetflixRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/netflix")
public class MovieController {

    private NetflixRepository netflixRepository;


    public MovieController(NetflixRepository netflixRepository) {
        this.netflixRepository = netflixRepository;
    }

    @GetMapping
    public Flux<Movie> getAllMovies(){
        return netflixRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Movie>> getMovie(@PathVariable String id){
        return netflixRepository.findById(id)
                .map(movie -> ResponseEntity.ok(movie)).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Movie> saveMovie(@RequestBody Movie movie){
        return netflixRepository.save(movie);

    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Movie>> updateMovie(@PathVariable( value =  "id")String id, @RequestBody Movie movie){

        return netflixRepository.findById(id).flatMap( existingMovie ->{
            existingMovie.setMovieName(movie.getMovieName());
            existingMovie.setMovieType(movie.getMovieType());
            existingMovie.setPrincipalActor(movie.getPrincipalActor());
            existingMovie.setCreated_at(movie.getCreated_at());
    return  netflixRepository.save(existingMovie);

        }).map(updateMovie -> ResponseEntity.ok(updateMovie)).defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Object>> deleteMovie(@PathVariable( value =  "id")String id){

        return netflixRepository.findById(id)
                .flatMap(existingMovie ->
                netflixRepository.delete(existingMovie))
                .then(Mono.just(ResponseEntity.ok().build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping
    public Mono<Void> deleteAllMovies(){
        return  netflixRepository.deleteAll();
    }


    @GetMapping(value = "/netflix-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieEvent> getMovieEvents(){
        return Flux.interval(Duration.ofSeconds(5))
                .map(val-> new MovieEvent(val, "Evento da NetFlix"));

    }

}
