package com.example.hp.moviesnearyou;

/**
 * Created by HP on 15-01-2017.
 */

public class MovieDetailsAttributes {
    private String key;
    private String name;

    public MovieDetailsAttributes(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
