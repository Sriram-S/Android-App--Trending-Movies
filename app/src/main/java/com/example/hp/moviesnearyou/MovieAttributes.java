package com.example.hp.moviesnearyou;

/**
 * Created by HP on 08-01-2017.
 */

public class MovieAttributes {
    private String name;
    private String Overview;
    private String poster;
    private String release;
    private int rating;
    private int toatlvotes;
    private int id;

    public MovieAttributes(String name, String overview, String poster, String release, int rating, int toatlvotes, int id) {
        this.name = name;
        Overview = overview;
        this.poster = poster;
        this.release = release;
        this.rating = rating;
        this.toatlvotes = toatlvotes;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getName() {return name;}

    public String getOverview() {
        return Overview;
    }

    public String getRelease() {
        return release;
    }

    public int getRating() {
        return rating;
    }

    public int getToatlvotes() {
        return toatlvotes;
    }
}
