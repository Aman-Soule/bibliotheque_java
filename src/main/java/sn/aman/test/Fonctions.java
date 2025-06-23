package sn.aman.test;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Fonctions {
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
        String query = "SELECT idEmprunt, date_emprunt, date_retour   FROM emprunt";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Date Emprunt");
            model.addColumn("Date Retour");


            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("idEmprunt"),
                        rs.getDate("date_emprunt"),
                        rs.getDate("date_retour")
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
}
