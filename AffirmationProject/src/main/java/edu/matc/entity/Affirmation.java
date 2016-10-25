package edu.matc.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by toddkinsman on 10/18/16.
 */
@Table
@Entity(name = "affirmations")
public class Affirmation {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "affirmationId")
    private int affirmationId;

    @Column(name = "phrase")
    private String phrase;

    @Column(name = "rating")
    private int rating;

    @Column(name = "categoryId")
    private int categoryId;


    public Affirmation() {
    }

    public Affirmation(String phrase, int rating, int categoryId) {
        this.phrase = phrase;
        this.rating = rating;
        this.categoryId = categoryId;
    }

    public int getAffirmationId() {
        return affirmationId;
    }

    public void setAffirmationId(int affirmationId) {
        this.affirmationId = affirmationId;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


}
