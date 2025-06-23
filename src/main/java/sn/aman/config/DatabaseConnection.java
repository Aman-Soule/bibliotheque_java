package sn.aman.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static SessionFactory sessionFactory;

    static {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            Metadata metadata = new MetadataSources(standardRegistry)
                    .getMetadataBuilder()
                    .build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();

            logger.info("SessionFactory créée avec succès");
        } catch (Exception ex) {
            logger.error("Échec de la création de SessionFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            throw new IllegalStateException("SessionFactory non disponible");
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("SessionFactory fermée avec succès");
        }
    }
}