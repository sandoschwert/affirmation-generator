package edu.matc.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *
 * This is a javabean that knows about a Category. You can safely set the
 * properties with the getter/setter methods.
 *
 */
@Table
@Entity(name = "categories")
public class Category {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "categoryId")
    private int catgoryId;

    @Column(name = "categoryName")
    private String categoryName;

    /**
     * The empty constructor for the affirmation
     */
    public Category() {
    }

    /**
     * Sets the value of categoryName.
     * @param categoryName The value to assign categoryName.
     */
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Returns the value of catgoryId.
     */
    public int getCatgoryId() {
        return catgoryId;
    }

    /**
     * Sets the value of catgoryId.
     * @param catgoryId The value to assign catgoryId.
     */
    public void setCatgoryId(int catgoryId) {
        this.catgoryId = catgoryId;
    }

    /**
     * Returns the value of categoryName.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the value of categoryName.
     * @param categoryName The value to assign categoryName.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}


