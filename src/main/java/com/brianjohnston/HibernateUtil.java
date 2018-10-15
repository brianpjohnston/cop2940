package com.brianjohnston;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * StudentLabTracker
 * 6/7/16 4:07 PM
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml

            return new Configuration().configure()
                                      .buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        if(!getSessionFactory().isClosed()) {
            getSessionFactory().close();
        }

    }

    public static <T> T transactionalFunction(Function<Session, T> function) {
        T t = null;
        try(final Session session = getSessionFactory().openSession()){
            t = runWithSession(session, function);
        }catch (HibernateException e) {
            System.out.println("no current session");
        }
        return t;
    }

    public static void transactionalConsumer(Consumer<Session> consumer){
        try(final Session session = getSessionFactory().openSession()){
            runWithSession(session, consumer);
        }catch (HibernateException e) {
            System.out.println("no current session");
        }
    }

    private static <T> T runWithSession(Session session, Function<Session, T> consumer){
        session.beginTransaction();
        final T t = consumer.apply(session);
        session.getTransaction().commit();
        return t;
    }

    private static void runWithSession(Session session, Consumer<Session> consumer){
        session.beginTransaction();
        consumer.accept(session);
        session.getTransaction().commit();
    }

}
