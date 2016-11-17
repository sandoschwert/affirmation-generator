package edu.matc.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *
 * This is a javabean that knows about an Affirmation. You can safely set the
 * properties with the getter/setter methods.
 *
 *
 *  @author Todd Kinsman
 *  @since 10/18/16
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

    /**
     * The empty constructor for the affirmation
     */
    public Affirmation() {
    }

    /**
     * A constructor to help set the entity properties
     */
    public Affirmation(String phrase, int rating, int categoryId) {
        this.phrase = phrase;
        this.rating = rating;
        this.categoryId = categoryId;
    }

    /**
     * Returns the value of affirmationId.
     */
    public int getAffirmationId() {
        return affirmationId;
    }

    /**
     * Sets the value of affirmationId.
     * @param affirmationId The value to assign affirmationId.
     */
    public void setAffirmationId(int affirmationId) {
        this.affirmationId = affirmationId;
    }

    /**
     * Returns the value of affirmationId.
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Sets the value of phrase.
     * @param phrase The value to assign affirmationId.
     */
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    /**
     * Returns the value of rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the value of rating.
     * @param rating The value to assign rating.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Returns the value of categoryId.
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the value of categoryId.
     * @param categoryId The value to assign categoryId.
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


}
