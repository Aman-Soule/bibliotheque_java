package sn.aman.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Membre extends JFrame {

    private JTable table;

    public Membre() {
        setTitle("Liste des Membres");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }
    private void chargerMembre() {
        try {
            table.setModel(Fonctions.getAllMembre()); //affiche la liste des membres
        } catch (SQLException ex) {
            System.out.println("Erreur lors du chargement des livres: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Membre().setVisible(true);
        });
    }
}