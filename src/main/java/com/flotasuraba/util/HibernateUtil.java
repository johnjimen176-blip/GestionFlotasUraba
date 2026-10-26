package com.flotasuraba.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    static {
        try {
            // Crea el registro del servicio estándar desde hibernate.cfg.xml
            registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            // Crea las fuentes de metadatos
            MetadataSources sources = new MetadataSources(registry);

            // Construye los metadatos
            Metadata metadata = sources.getMetadataBuilder().build();

            // Construye la factoría de sesiones
            sessionFactory = metadata.getSessionFactoryBuilder().build();
            System.out.println(">>> [HibernateUtil] SessionFactory inicializada exitosamente.");
        } catch (Throwable ex) {
            System.err.println(">>> [HibernateUtil] Error al inicializar SessionFactory: " + ex.getMessage());
            ex.printStackTrace();
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println(">>> [HibernateUtil] SessionFactory cerrada correctamente.");
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable ignored) {
        }
    }
}
