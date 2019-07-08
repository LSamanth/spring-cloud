package com.lavanya.ratingsdataservice.models;

import java.util.List;

public class UserRating {
    List<Rating> userRatings;

    public List<Rating> getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(List<Rating> userRatings) {
        this.userRatings = userRatings;
    }

    public UserRating(List<Rating> userRatings) {
        this.userRatings = userRatings;
    }

    public UserRating() {
    }
}
