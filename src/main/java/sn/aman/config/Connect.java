package sn.aman.config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connect {
    public static List<String> getLivres() {
        List<String> livres = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC",
                "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT titre FROM livre")) {

            while (rs.next()) {
                livres.add(rs.getString("titre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }
}