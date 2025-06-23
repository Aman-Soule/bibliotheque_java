package sn.aman;

import sn.aman.entite.Livre;
import sn.aman.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static sn.aman.config.HibernateUtil.getSessionFactory;

public class App {
    public static void main(String[] args) {
        // Création d'un nouveau livre
        Livre nouveauLivre = new Livre();
        nouveauLivre.setTitre("Adrien Rabiot");
        nouveauLivre.setAuteur("Mandanda");
        nouveauLivre.setAnnee_publication(2006);

        // Obtention de la SessionFactory
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            // Début de la transaction
            transaction = session.beginTransaction();

            // Sauvegarde du livre dans la base de données
            session.save(nouveauLivre);

            // Validation de la transaction
            transaction.commit();

            System.out.println("Livre enregistré avec succès ! ID: " + nouveauLivre.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        // Fermeture de la SessionFactory
        HibernateUtil.shutdown();
    }
}