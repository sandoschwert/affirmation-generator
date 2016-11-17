package edu.matc.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * This file provides a SessionFactory for use with DAOS using Hibernate
 * @author Todd Kinsman
 * @version 1.0 10/24/16.
 */
public class SessionFactoryProvider {

    private static SessionFactory sessionFactory;

    /**
     * Creates session factory that will be used with Hibernate
     */
    public static void createSessionFactory() {

        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     * Gets session factory and instantiates one if not already created.
     * @return sessionFactory SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createSessionFactory();
        }
        return sessionFactory;

    }

}
