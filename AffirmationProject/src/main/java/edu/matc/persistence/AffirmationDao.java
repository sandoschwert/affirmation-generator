package edu.matc.persistence;

import edu.matc.entity.Affirmation;
import edu.matc.entity.Category;
import org.apache.log4j.Logger;
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

        List<Affirmation> affirmationList = new ArrayList<Affirmation>();
        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        affirmationList = session.createCriteria(Affirmation.class).list();

        return affirmationList;
    }


    public Affirmation getAffirmationWithId(int affirmationId) {

        Session session = SessionFactoryProvider.getSessionFactory().openSession();
        Affirmation affirmation = (Affirmation) session.get(Affirmation.class, affirmationId);
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

}
