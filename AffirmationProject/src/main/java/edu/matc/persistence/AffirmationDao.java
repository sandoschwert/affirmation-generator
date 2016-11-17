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
 *  This is the data access object for the affirmation table.
 *
 *  @author Todd Kinsman
 *  @since 10/18/16
 */
public class AffirmationDao {

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     * Gets all affirmations from the affirmation table
     *
     * @return affirmations
     * Returns the list of Affirmations.
     */
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

    /**
     * Gets all non-nsfw affirmations from the affirmation table
     *
     * @return affirmations
     * Returns the list of Affirmations.
     */
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

    /**
     * Gets an affirmations from the affirmation table based on id
     *
     * @param affirmationId the id of affirmation to retrieve
     * @return affirmation
     * Returns an Affirmation.
     */
    public Affirmation getAffirmationWithId(int affirmationId) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Affirmation affirmation = (Affirmation) session.get(Affirmation.class, affirmationId);
        session.close();
        return affirmation;

    }


    /**
     * Adds an affirmation object to the affirmation table
     *
     * @param  affirmation the affirmation to add
     * @return id of added affirmation
     * Returns the list of Affirmations.
     */
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

    /**
     * Updates the rating of an affirmation in the affirmation table by increments of 1
     *
     * @param affirmationId the id of the affirmation to update
     * @return rating of the updated affirmation
     *
     */
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

    /**
     * Updates the rating of an affirmation in the affirmation table by decrements of 1
     *
     * @param  affirmationId the id of affirmation to upvote
     * @return rating of the updated affirmation
     *
     */
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

    /**
     * Updates  an affirmation in the affirmation table
     * @param affirmation the affirmation to update
     * @return id of the updated affirmation
     *
     */
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

    /**
     * Deletes the affirmation in the affirmation table
     * @param affirmation object to delete
     * @return int showing success of 1 or failure with 0
     *
     */
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

    /**
     * Gets all affirmations from the affirmation table based on category
     *
     * @param categoryName String of category name
     * @return affirmations
     *
     * Returns the list of Affirmations by category.
     */
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

    /**
     * Gets all affirmations from the affirmation table sorted by most popular ratings
     *
     * @param categoryName String for name of category to retrieve
     * @param howManyToGet int of how many affirmation objects to get back
     * @return affirmations
     * Returns the list of Affirmations.
     */
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
