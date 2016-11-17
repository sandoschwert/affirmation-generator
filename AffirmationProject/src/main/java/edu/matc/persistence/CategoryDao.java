package edu.matc.persistence;

import edu.matc.entity.Affirmation;
import edu.matc.entity.Category;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.*;

/**
 *
 *  This is the data access object for the category table.
 *
 *  @author Todd Kinsman
 *  @since 10/18/16
 */
public class CategoryDao {

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     * Gets all categories from the affirmation table
     *
     * @return affirmations
     * Returns the list of Affirmations.
     */
    public List<Category> getAllCategories() {

        List<Category> categoryList = new ArrayList<Category>();
        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        categoryList = session.createCriteria(Category.class).list();

        session.close();
        return categoryList;
    }

    /**
     * Gets an category from the catgory table based on id
     *
     * @param categoryId the id of affirmation to retrieve
     * @return category
     * Returns a Category.
     */
    public Category getACategoryWithId(int categoryId) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Category category = (Category) session.get(Category.class, categoryId);
        session.close();
        return category;

    }

    /**
     * Adds an Category object to the category table
     *
     * @param  category the Category to add
     * @return id of added category
     * Returns the list of categories.
     */
    public int addCategory(Category category) {

        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(category);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to add category loc: " + e);

        } finally {
            session.flush();
            session.close();
        }
        int id = category.getCatgoryId();

        return id;
    }

    /**
     * Updates the category in the category table
     *
     * @param category the category to update
     * @return id int of the updated category
     *
     */
    public int updateCategory(Category category) {

        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.update(category);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to add category loc: " + e);

        } finally {
            session.flush();
            session.close();
        }
        int id = category.getCatgoryId();

        return id;
    }

    /**
     * Deletes the category in the category table
     * @param category object to delete
     * @return int showing success of 1 or failure with 0
     *
     */
    public int deleteCategory(Category category) {

        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        int sucInt;

        try {
            trns = session.beginTransaction();
            session.delete(category);
            session.getTransaction().commit();
            sucInt = 1;

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            sucInt = 0;
            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to add category loc: " + e);

        } finally {
            session.flush();
            session.close();
        }

        return sucInt;
    }

    /**
     * Gets an categoryid from the catgory table based on category
     *
     * @param categoryName to get retrieve id from
     * @return categoryId
     * Returns a categoryId.
     */
    public int getCategoryIdOfCategory(String categoryName) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        List<Category> categoryList = new ArrayList<Category>();
        String hql = "FROM categories c WHERE c.categoryName = :categoryName";
        Query query = session.createQuery(hql);

        Category category = new Category();
        query.setString("categoryName",categoryName);
        categoryList = query.list();

        category = categoryList.get(0);

        log.info("Category from id name success: " + category.getCategoryName() + " List size of cat " + categoryList);

        int catId = category.getCatgoryId();


        session.close();

        return catId;
    }

}
