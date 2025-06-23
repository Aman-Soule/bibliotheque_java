package sn.aman.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ListeLivresSimple extends JFrame {

    private JTable table;

    public ListeLivresSimple() {
        setTitle("Liste des Livres");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Bouton Ajouter
        JButton btnAjouter = new JButton("Ajouter un livre");
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterLivre();
            }
        });

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierLivre();
            }
        });

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerLivre();
            }
        });

        // Bouton Actualiser
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerLivres();
            }
        });

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnActualiser);

        add(buttonPanel, BorderLayout.SOUTH);

        chargerLivres();
    }

    private void chargerLivres() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false",
                    "root", "");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, titre, auteur FROM livre");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Titre");
            model.addColumn("Auteur");

            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("auteur")
                });
            }

            table.setModel(model);

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur: " + ex.getMessage(),
                    "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterLivre() {
        JTextField titreField = new JTextField();
        JTextField auteurField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Titre:"));
        panel.add(titreField);
        panel.add(new JLabel("Auteur:"));
        panel.add(auteurField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Ajouter un nouveau livre",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String titre = titreField.getText();
            String auteur = auteurField.getText();

            if (titre.isEmpty() || auteur.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Tous les champs doivent être remplis",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false",
                        "root", "");

                String sql = "INSERT INTO livre (titre, auteur) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, titre);
                pstmt.setString(2, auteur);

                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Livre ajouté avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                pstmt.close();
                conn.close();

                chargerLivres();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur: " + ex.getMessage(),
                        "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifierLivre() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un livre à modifier",
                    "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String titreActuel = (String) table.getValueAt(selectedRow, 1);
        String auteurActuel = (String) table.getValueAt(selectedRow, 2);

        JTextField titreField = new JTextField(titreActuel);
        JTextField auteurField = new JTextField(auteurActuel);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Titre:"));
        panel.add(titreField);
        panel.add(new JLabel("Auteur:"));
        panel.add(auteurField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Modifier le livre",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nouveauTitre = titreField.getText();
            String nouvelAuteur = auteurField.getText();

            if (nouveauTitre.isEmpty() || nouvelAuteur.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Tous les champs doivent être remplis",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false",
                        "root", "");

                String sql = "UPDATE livre SET titre = ?, auteur = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nouveauTitre);
                pstmt.setString(2, nouvelAuteur);
                pstmt.setInt(3, id);

                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Livre modifié avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                pstmt.close();
                conn.close();

                chargerLivres();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur: " + ex.getMessage(),
                        "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void supprimerLivre() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un livre à supprimer",
                    "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String titre = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer le livre: " + titre + "?",
                "Confirmer la suppression",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false",
                        "root", "");

                String sql = "DELETE FROM livre WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);

                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Livre supprimé avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                pstmt.close();
                conn.close();

                chargerLivres();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur: " + ex.getMessage(),
                        "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ListeLivresSimple().setVisible(true);
        });
    }

    public static class CRUDSwingApp extends JFrame {
        private JTextField idField, nomField, prenomField, emailField;
        private JButton createButton, readButton, updateButton, deleteButton;
        private JTextArea outputArea;
        private JPanel content;

        public CRUDSwingApp() {
            setTitle("Application CRUD avec Swing");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            initUI();
        }

        private void initUI() {
            // Panel pour les champs de formulaire
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

            idField = new JTextField();
            nomField = new JTextField();
            prenomField = new JTextField();
            emailField = new JTextField();

            formPanel.add(new JLabel("ID Livre :"));
            formPanel.add(idField);
            formPanel.add(new JLabel("Titre :"));
            formPanel.add(nomField);
            formPanel.add(new JLabel("Auteur :"));
            formPanel.add(prenomField);
            formPanel.add(new JLabel("Année de publication :"));
            formPanel.add(emailField);

            // Panel pour les boutons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

            createButton = new JButton("Ajouter");
            readButton = new JButton("Lister");
            updateButton = new JButton("Mettre à jour");
            deleteButton = new JButton("Supprimer");

            buttonPanel.add(createButton);
            buttonPanel.add(readButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);

            // Zone de sortie
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputArea);

            // Ajout des composants au frame
            setLayout(new BorderLayout(10, 10));
            add(formPanel, BorderLayout.NORTH);
            add(buttonPanel, BorderLayout.CENTER);
            add(scrollPane, BorderLayout.SOUTH);

            // Ajout des écouteurs d'événements
            addEventListeners();
        }

        public void ajoutLivre() {
            // Implémentation à venir
        }

        public void modifierLivre() {
            // Implémentation à venir
        }

        public void listeLivre() {
            try {
                // 1. Récupérer la connexion
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/bibliotheque?useSSL=false&serverTimezone=UTC",
                        "root", "");

                // 2. Exécuter la requête
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, titre, auteur FROM livre");

                // 3. Créer un modèle pour stocker les données
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("id");
                model.addColumn("titre");
                model.addColumn("auteur");


                // 4. Remplir le modèle avec les résultats
                while(rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("auteur")
                    });
                }

                // 5. Afficher dans un JTable
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                // 6. Ajouter le tableau à votre interface
                // Supposons que vous avez un JPanel appelé contentPanel
                content.removeAll(); // Efface le contenu précédent
                content.add(scrollPane, BorderLayout.CENTER);
                content.revalidate();
                content.repaint();



                // 7. Fermer les ressources
                rs.close();
                stmt.close();
                conn.close();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur lors du chargement des livres: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public void supprimerLivre() {
            // Implémentation à venir
        }

        private void addEventListeners() {
            createButton.addActionListener(e -> ajoutLivre());
            readButton.addActionListener(e -> listeLivre());
            updateButton.addActionListener(e -> modifierLivre());
            deleteButton.addActionListener(e -> supprimerLivre());
        }



        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                CRUDSwingApp app = new CRUDSwingApp();
                app.setVisible(true);
            });
        }
    }
}