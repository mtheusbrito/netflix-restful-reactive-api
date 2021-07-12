package com.mtheus.netflix.repository;

import com.mtheus.netflix.models.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NetflixRepository extends ReactiveMongoRepository<Movie, String> {

}
