package com.example.hp.moviesnearyou;

/**
 * Created by HP on 22-01-2017.
 */

public class ReviewAttributes {
    private String authorname;
    private String reviewdesc;

    public ReviewAttributes(String authorname, String reviewdesc) {
        this.authorname = authorname;
        this.reviewdesc = reviewdesc;
    }

    public String getAuthorname() {
        return authorname;
    }

    public String getReviewdesc() {
        return reviewdesc;
    }

}
