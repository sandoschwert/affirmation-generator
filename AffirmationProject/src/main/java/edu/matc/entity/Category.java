package edu.matc.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by toddkinsman on 10/18/16.
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


    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCatgoryId() {
        return catgoryId;
    }

    public void setCatgoryId(int catgoryId) {
        this.catgoryId = catgoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}


