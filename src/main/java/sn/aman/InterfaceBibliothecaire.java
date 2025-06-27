package sn.aman;

import sn.aman.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class InterfaceBibliothecaire extends JFrame {
    private JTable table;

    public InterfaceBibliothecaire() {
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
        btnAjouter.addActionListener(e -> ajouterLivre());

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierLivre());

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerLivre());

        // Bouton Actualiser
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.addActionListener(e -> chargerLivres());

        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnActualiser);

        add(buttonPanel, BorderLayout.SOUTH);

        chargerLivres();
    }

    private void chargerLivres() {
        try {
            table.setModel(DatabaseConnection.Fonctions.getAllLivres());
        } catch (SQLException ex) {
            showError("Erreur lors du chargement des livres: " + ex.getMessage());
        }
    }

    private void ajouterLivre() {
        JTextField titreField = new JTextField();
        JTextField auteurField = new JTextField();
        JTextField annee_publicationField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Titre:"));
        panel.add(titreField);
        panel.add(new JLabel("Auteur:"));
        panel.add(auteurField);
        panel.add(new JLabel("Annee"));
        panel.add(annee_publicationField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Ajouter un nouveau livre",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String titre = titreField.getText();
            String auteur = auteurField.getText();
            int annee = annee_publicationField.getText().length();

            if (titre.isEmpty() || auteur.isEmpty()) {
                showError("Tous les champs doivent être remplis");
                return;
            }

            try {
                DatabaseConnection.Fonctions.addLivre(titre, auteur, annee);
                JOptionPane.showMessageDialog(this, "Livre ajouté avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerLivres();
            } catch (SQLException ex) {
                showError("Erreur lors de l'ajout du livre: " + ex.getMessage());
            }
        }
    }

    private void modifierLivre() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            showWarning("Veuillez sélectionner un livre à modifier");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String titreActuel = (String) table.getValueAt(selectedRow, 1);
        String auteurActuel = (String) table.getValueAt(selectedRow, 2);
        int anneeActuelle = (int) table.getValueAt(selectedRow, 3);

        JTextField titreField = new JTextField(titreActuel);
        JTextField auteurField = new JTextField(auteurActuel);
        JTextField anneeField = new JTextField(String.valueOf(anneeActuelle));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Titre:"));
        panel.add(titreField);
        panel.add(new JLabel("Auteur:"));
        panel.add(auteurField);
        panel.add(new JLabel("Année de publication:"));
        panel.add(anneeField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Modifier le livre",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nouveauTitre = titreField.getText();
            String nouvelAuteur = auteurField.getText();
            String anneeText = anneeField.getText();

            if (nouveauTitre.isEmpty() || nouvelAuteur.isEmpty() || anneeText.isEmpty()) {
                showError("Tous les champs doivent être remplis");
                return;
            }

            try {
                int nouvelleAnnee = Integer.parseInt(anneeText);

                // Validation de l'année (exemple: entre 1000 et année courante + 5)
                int anneeCourante = java.time.Year.now().getValue();
                if (nouvelleAnnee < 1000 || nouvelleAnnee > anneeCourante + 5) {
                    showError("L'année doit être comprise entre 1000 et " + (anneeCourante + 5));
                    return;
                }

                DatabaseConnection.Fonctions.updateLivre(id, nouveauTitre, nouvelAuteur, nouvelleAnnee);
                JOptionPane.showMessageDialog(this, "Livre modifié avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerLivres();
            } catch (NumberFormatException e) {
                showError("L'année doit être un nombre valide");
            } catch (SQLException ex) {
                showError("Erreur lors de la modification du livre: " + ex.getMessage());
            }
        }
    }

    private void supprimerLivre() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            showWarning("Veuillez sélectionner un livre à supprimer");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String titre = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this, "Êtes-vous sûr de vouloir supprimer le livre: " + titre + "?",
                "Confirmer la suppression", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DatabaseConnection.Fonctions.deleteLivre(id);
                JOptionPane.showMessageDialog(this, "Livre supprimé avec succès!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerLivres();
            } catch (SQLException ex) {
                showError("Erreur lors de la suppression du livre: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Avertissement", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfaceBibliothecaire().setVisible(true);
        });
    }
}
