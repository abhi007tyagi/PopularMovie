package com.tyagiabhinav.popularmovie.Model;

import java.io.Serializable;

/**
 * Created by abhinavtyagi on 04/02/16.
 */
public class Movie implements Serializable {

    private String id;
    private String title;
    private String posterPath;
    private String releaseDate;
    private String duration;
    private String userRating;
    private String userVote;
    private String plot;
    private String trailerPath;
    private String review;
//    private int icon;

//    public Movie(int img){
//        this.icon = img;
//    }

//    public int getIcon() {
//        return icon;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserVote() {
        return userVote;
    }

    public void setUserVote(String userVote) {
        this.userVote = userVote;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public void setTrailerPath(String trailerPath) {
        this.trailerPath = trailerPath;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
