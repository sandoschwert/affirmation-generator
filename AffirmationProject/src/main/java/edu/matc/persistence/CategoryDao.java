package edu.matc.persistence;

import edu.matc.entity.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.*;

/**
 * Created by toddkinsman on 10/18/16.
 */
public class CategoryDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public List<Category> getAllCategories() {

        List<Category> categoryList = new ArrayList<Category>();
        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        categoryList = session.createCriteria(Category.class).list();

        return categoryList;
    }


    public Category getACategoryWithId(int categoryId) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Category category = (Category) session.get(Category.class, categoryId);
        return category;

    }


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

}
