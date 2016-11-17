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
 * Created by toddkinsman on 10/18/16.
 */
public class AffirmationDao {

    private final Logger log = Logger.getLogger(this.getClass());


    public List<Affirmation> getAllAffirmations() {
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        CategoryDao categoryDao = new CategoryDao();

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();

        Transaction trns = null;

        try {

            trns = session.beginTransaction();
            String hql = "FROM affirmations ORDER BY rating desc";
            Query query = session.createQuery(hql);
            affirmationList = query.list();
            session.getTransaction().commit();


        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            log.info("There was a runtime exception to delete affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }

        return affirmationList;
    }

    //Get all affirmations
    public List<Affirmation> getAllGoodAffirmations() {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        CategoryDao categoryDao = new CategoryDao();

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();

        Transaction trns = null;

        try {

            trns = session.beginTransaction();
            String hql = "FROM affirmations af WHERE af.categoryId != :categoryId";
            Query query = session.createQuery(hql);
            query.setInteger("categoryId", 10);
            affirmationList = query.list();
            session.getTransaction().commit();


        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            log.info("There was a runtime exception to delete affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }

        return affirmationList;

    }


    public Affirmation getAffirmationWithId(int affirmationId) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Affirmation affirmation = (Affirmation) session.get(Affirmation.class, affirmationId);
        session.close();
        return affirmation;

    }


    public int addAffirmation(Affirmation affirmation) {

        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(affirmation);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to add affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }
        int id = affirmation.getAffirmationId();

        return id;
    }

    public int downVoteAffirmation(int affirmationId) {

        Affirmation affirmation = getAffirmationWithId(affirmationId);
        int affdownCount;


        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            affdownCount = affirmation.getRating() - 1;
            affirmation.setRating(affdownCount);
            session.update(affirmation);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }

            affdownCount = affirmation.getRating();

            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to update affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }

        return affdownCount;
    }

    public int upVoteAffirmation(int affirmationId) {

        int affUpCount;
        Affirmation affirmation = getAffirmationWithId(affirmationId);

        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            affUpCount = affirmation.getRating() + 1;
            affirmation.setRating(affUpCount);
            session.update(affirmation);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }

            affUpCount = affirmation.getRating();

            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to update affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }

        return affUpCount;
    }

    public int updateAffirmation(Affirmation affirmation) {

        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.update(affirmation);
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to update affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }
        int id = affirmation.getAffirmationId();

        return id;
    }

    public int deleteAffirmation(Affirmation affirmation) {

        int confirm;
        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.delete(affirmation);
            session.getTransaction().commit();
            confirm = 1;

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            //Todo Take this print out!
            e.printStackTrace();
            log.info("There was a runtime exception to delete affirmation: " + e);
            confirm = 0;
        } finally {
            session.flush();
            session.close();
        }

        return confirm;
    }

    public List<Affirmation> getAllAffirmationsFromCategory(String categoryName) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        CategoryDao categoryDao = new CategoryDao();

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        int categoryId = categoryDao.getCategoryIdOfCategory(categoryName);

        Transaction trns = null;

        try {

            trns = session.beginTransaction();
            String hql = "FROM affirmations af WHERE af.categoryId = :categoryId";
            Query query = session.createQuery(hql);
            query.setInteger("categoryId",categoryId);
            affirmationList = query.list();
            session.getTransaction().commit();


        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            log.info("There was a runtime exception to delete affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }

        return affirmationList;

    }

    //get the most popular affirmation by category
    public List<Affirmation> getMostPopularAffirmations(String categoryName, int howManyToGet) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        CategoryDao categoryDao = new CategoryDao();
        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        int categoryId = categoryDao.getCategoryIdOfCategory(categoryName);
        Transaction trns = null;

        try {

            trns = session.beginTransaction();
            String hql = "FROM affirmations af WHERE af.categoryId = :categoryId ORDER BY af.rating desc";
            Query query = session.createQuery(hql);
            query.setInteger("categoryId",categoryId);
            query.setFirstResult(0);
            query.setMaxResults(howManyToGet);
            affirmationList = query.list();
            session.getTransaction().commit();


        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            log.info("There was a runtime exception to delete affirmation: " + e);

        } finally {
            session.flush();
            session.close();
        }

        return affirmationList;

    }



}
