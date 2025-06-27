package sn.aman.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

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

    public static class Fonctions {
        private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }

        public static DefaultTableModel getAllLivres() throws SQLException {
            String query = "SELECT id, titre, auteur, annee_publication FROM livre";
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Titre");
                model.addColumn("Auteur");
                model.addColumn("Annee publication");

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("auteur"),
                            rs.getInt("annee_publication")
                    });
                }
                return model;
            }
        }

        public static DefaultTableModel getEmprunt() throws SQLException {
            String query = "SELECT idEmprunt, date_emprunt, date_retour, idLivreF, idMembreF   FROM emprunt";
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID Emprunt");
                model.addColumn("Date Emprunt");
                model.addColumn("Date Retour");
                model.addColumn("ID Livre");
                model.addColumn("ID Membre");


                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("idEmprunt"),
                            rs.getDate("date_emprunt"),
                            rs.getDate("date_retour"),
                            rs.getInt("idLivreF"),
                            rs.getInt("idMembreF")
                    });
                }
                return model;
            }
        }

        public static DefaultTableModel getAllMembre() throws SQLException {
            String query = "SELECT idMembre, nom, prenom, email, tel, idEmpruntF  FROM membre";
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Nom");
                model.addColumn("Prenom");
                model.addColumn("Email");
                model.addColumn("Tel");
                model.addColumn("Emprunt");

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("idMembre"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("tel"),
                            rs.getInt("idEmpruntF")
                    });
                }
                return model;
            }
        }

        public static void addLivre(String titre, String auteur, int annee_publication) throws SQLException {
            String sql = "INSERT INTO livre (titre, auteur, annee_publication) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, titre);
                pstmt.setString(2, auteur);
                pstmt.setInt(3, annee_publication);
                pstmt.executeUpdate();
            }
        }

        public static void updateLivre(int id, String titre, String auteur, int annee_publication) throws SQLException {
            String sql = "UPDATE livre SET titre = ?, auteur = ?, annee_publication = ? WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, titre);
                pstmt.setString(2, auteur);
                pstmt.setInt(3, annee_publication);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
            }
        }

        public static void deleteLivre(int id) throws SQLException {
            String sql = "DELETE FROM livre WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        }



        // Dans la classe Fonctions
        public static void faireEmpruntSimple(int idEmprunt, String date_emprunt, String date_retour, int idLivreF) throws SQLException {
            String sql = "INSERT INTO emprunt (idEmprunt,  date_emprunt, date_retour, idLivreF) VALUES (?, ?, ?, ?)";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idEmprunt);
                pstmt.setString(2, date_emprunt);
                pstmt.setString(3, date_retour);
                pstmt.setInt(4, idLivreF);
                pstmt.executeUpdate();
            }
        }

        public static void faireEmprunt(int idEmprunt, String date_emprunt, String date_retour, int idLivreF, int idMembreF) throws SQLException {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                // 1. Établir la connexion à la base de données
                connection = getConnection();


                String sql = "INSERT INTO emprunt (idEmprunt, date_emprunt, date_retour, idLivreF, idMembreF) VALUES (?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(sql);


                statement.setInt(1, idEmprunt);
                statement.setString(2, date_emprunt);
                statement.setString(3, date_retour);
                statement.setInt(4, idLivreF);
                statement.setInt(5, idMembreF);


                int rowsAffected = statement.executeUpdate();


                if (rowsAffected == 0) {
                    throw new SQLException("L'emprunt n'a pas pu être enregistré.");
                }

//                String updateSql = "UPDATE livre SET disponible = false WHERE idLivre = ?";
//                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
//                    updateStatement.setInt(1, idLivreF);
//                    updateStatement.executeUpdate();
//                }

            } finally {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        }
    }
}