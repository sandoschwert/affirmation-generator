package edu.matc.persistence;

import edu.matc.entity.Affirmation;
import edu.matc.entity.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toddkinsman on 11/9/16.
 */
public class DatabaseSetupDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public void clearAllDataFromCategoryTable() {
        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.createQuery("DELETE FROM categories ").executeUpdate();
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            log.info("There was a runtime exception to clear category table: " + e);

        } finally {
            session.flush();
            session.close();
        }

    }

    public void clearAllDataFromAffirmationTable() {

        Transaction trns = null;
        Session session = SessionFactoryProvider.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.createQuery("DELETE FROM affirmations").executeUpdate();
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            log.info("There was a runtime exception to clear affirmation table: " + e);

        } finally {
            session.flush();
            session.close();
        }

    }

    public void clearAllDatabaseTables() {

        clearAllDataFromAffirmationTable();
        clearAllDataFromCategoryTable();

    }

}
