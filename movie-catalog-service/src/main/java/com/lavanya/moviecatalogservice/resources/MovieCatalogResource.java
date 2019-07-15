package com.lavanya.moviecatalogservice.resources;

import com.lavanya.moviecatalogservice.models.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    /*@HystrixCommand(fallbackMethod = "fallbackCatalog")*/
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = getUserRating(userId);

        return userRating.getUserRatings().stream().map( rating -> getCatalogItem(rating) ).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "fallbackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject(
                "http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    @HystrixCommand(fallbackMethod = "fallbackUserRating")
    private UserRating getUserRating(@PathVariable("userId") String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId,
                UserRating.class);
    }

    private UserRating getFallBackUserRating(@PathVariable("userId") String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserRatings( Arrays.asList( new Rating( "0", 0 ) ));
        return userRating;
    }

    private CatalogItem getFallBackCatalogItem(Rating rating) {
       return new CatalogItem("Movie not found", "No desc found", rating.getRating());
    }

    public List<CatalogItem> fallbackCatalog(@PathVariable("userId") String userId) {
        return Arrays.asList(new CatalogItem("No movie", "No desc", 0));
    }
}

//Alternate way to use webclient
/*Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/