package com.tyagiabhinav.popularmovie.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhinavtyagi on 04/02/16.
 */
public class Movie implements Parcelable {

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
    private String popularity;
    private int isFavourite;

    public Movie(){
    }

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

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public int isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeString(this.duration);
        dest.writeString(this.userRating);
        dest.writeString(this.plot);
        dest.writeString(this.userVote);
        dest.writeString(this.trailerPath);
        dest.writeString(this.review);
        dest.writeString(this.popularity);
        dest.writeInt(this.isFavourite);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    protected Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.duration = in.readString();
        this.userRating = in.readString();
        this.plot = in.readString();
        this.userVote = in.readString();
        this.trailerPath = in.readString();
        this.review = in.readString();
        this.popularity = in.readString();
        this.isFavourite = in.readInt();
    }
}
