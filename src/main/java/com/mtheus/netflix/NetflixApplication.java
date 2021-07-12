package com.mtheus.netflix;

import com.mtheus.netflix.models.Movie;
import com.mtheus.netflix.repository.NetflixRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class NetflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixApplication.class, args);
	}


	@Bean
	CommandLineRunner init(ReactiveMongoOperations operations, NetflixRepository repository){
		return args ->{
			Flux<Movie> movieFlux = Flux.just(
					new Movie(null , "Matrix" , "Açao",  "Keanu Reeves", 1995)
			).flatMap(repository::save);
			movieFlux.thenMany(repository.findAll()).subscribe(System.out::println);
		};
	}

}
