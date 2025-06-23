package sn.aman.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        StandardServiceRegistry standardRegistry = null;
        try {
            // Création du registre de services standard
            standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            // Création des métadonnées
            Metadata metadata = new MetadataSources(standardRegistry)
                    .getMetadataBuilder()
                    .build();

            // Construction de la SessionFactory
            SessionFactory factory = metadata.getSessionFactoryBuilder().build();

            logger.info("SessionFactory créée avec succès");
            return factory;

        } catch (Exception ex) {
            logger.error("Échec de la création de la SessionFactory", ex);
            // Assure la destruction du registre de services en cas d'échec
            StandardServiceRegistryBuilder.destroy(standardRegistry);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("SessionFactory fermée avec succès");
        }
    }
}