package com.lavanya.ratingsdataservice.resources;

import com.lavanya.ratingsdataservice.models.Rating;
import com.lavanya.ratingsdataservice.models.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {

    @RequestMapping("/movies/{movieId}")
    public Rating getMovieRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }

    @RequestMapping("/users/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId) {
        List<Rating> ratings = Arrays.asList(
                new Rating("123", 4),
                new Rating("456", 5));
        UserRating userRating = new UserRating();
        userRating.setUserRatings(ratings);
        return userRating;
    }
}
