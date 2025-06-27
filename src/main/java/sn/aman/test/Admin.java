package sn.aman.test;

import sn.aman.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Admin extends JFrame {

    private JTable table;
    private JTable table2;
    private JTable table3;

    public Admin() {
        setTitle("Liste des Livres");
        setSize(1200, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        table = new JTable();
        table2 = new JTable();
        table3 = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(table2);
        JScrollPane scrollPane3 = new JScrollPane(table3);
        add(scrollPane, BorderLayout.CENTER);
        add(scrollPane2, BorderLayout.SOUTH);
        add(scrollPane3, BorderLayout.EAST);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));

        // Bouton Ajouter
        JButton btnAjouter = new JButton("Ajouter un livre");
        btnAjouter.addActionListener(e -> ajouterLivre());

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierLivre());

        JButton btnEmprunter = new JButton("Emprunter un livre");
        btnEmprunter.addActionListener(e -> emprunterLivre());

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerLivre());

        // Bouton Actualiser
        JButton btnActualiser = new JButton("Livres");
        btnActualiser.addActionListener(e -> chargerLivres());

        JButton btnActualiser2 = new JButton("Emprunts");
        btnActualiser2.addActionListener(e -> chargerEmprunt());

        JButton btnActualiser3 = new JButton("Membres");
        btnActualiser3.addActionListener(e -> chargerMembres());
        JButton btnFaireEmprunt = new JButton("Faire Emprunt");
        btnFaireEmprunt.addActionListener(e -> faireEmpruntForm());
        buttonPanel.add(btnFaireEmprunt);



        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnActualiser);
        buttonPanel.add(btnActualiser2);
        buttonPanel.add(btnActualiser3);
        buttonPanel.add(btnEmprunter);

        add(buttonPanel, BorderLayout.SOUTH);
        chargerEmprunt();
        chargerLivres();
        chargerMembres();
    }

    private void chargerMembres() {
        try {
            table.setModel(DatabaseConnection.Fonctions.getAllMembre());
        } catch (SQLException ex) {
            showError("Erreur lors du chargement des Membres: " + ex.getMessage());
        }
    }
    private void chargerEmprunt() {
        try {
            table.setModel(DatabaseConnection.Fonctions.getEmprunt());
        } catch (SQLException ex) {
            showError("Erreur lors du chargement des Emprunts: " + ex.getMessage());
        }
    }

    private void chargerLivres() {
        try {
            table.setModel(DatabaseConnection.Fonctions.getAllLivres()); //affiche la liste des livres sous forme de tableau
        } catch (SQLException ex) {
            showError("Erreur lors du chargement des livres: " + ex.getMessage());
        }
    }

    private void emprunterLivre() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            showWarning("Veuillez sélectionner un livre à emprunter");
            return;
        }

        int idLivre = (int) table.getValueAt(selectedRow, 0);
        String titre = (String) table.getValueAt(selectedRow, 1);

        try {
            // Générer un ID aléatoire (à remplacer par votre logique de génération d'ID)
            int idEmprunt = (int) (Math.random() * 1000) + 1;
            // Date d'aujourd'hui
            String dateEmprunt = new java.sql.Date(System.currentTimeMillis()).toString();
            // Date de retour dans 14 jours
            String dateRetour = new java.sql.Date(System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000)).toString();

            DatabaseConnection.Fonctions.faireEmpruntSimple(idEmprunt, dateEmprunt, dateRetour, idLivre);

            JOptionPane.showMessageDialog(
                    this,
                    "Emprunt enregistré!\n" +
                            "ID Emprunt: " + idEmprunt + "\n" +
                            "Date retour: " + dateRetour,
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
            );

//            chargerLivresDisponibles();
//            chargerEmpruntsMembre();

        } catch (SQLException ex) {
            showError("Erreur lors de l'enregistrement de l'emprunt: " + ex.getMessage());
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
    private void faireEmpruntForm() {
        // Création des champs du formulaire
        JTextField idMembreField = new JTextField();
        JTextField idLivreField = new JTextField();

        // Si un livre est sélectionné dans la table, pré-remplir son ID
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            idLivreField.setText(String.valueOf(table.getValueAt(selectedRow, 0)));
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("ID Membre:"));
        panel.add(idMembreField);
        panel.add(new JLabel("ID Livre:"));
        panel.add(idLivreField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Nouvel Emprunt",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int idMembre = Integer.parseInt(idMembreField.getText());
                int idLivre = Integer.parseInt(idLivreField.getText());

                // Générer un ID d'emprunt
                int idEmprunt = (int) (Math.random() * 1000) + 1;

                // Dates d'emprunt et de retour
                String dateEmprunt = new java.sql.Date(System.currentTimeMillis()).toString();
                String dateRetour = new java.sql.Date(System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000)).toString();

                // Enregistrer l'emprunt
                DatabaseConnection.Fonctions.faireEmprunt(idEmprunt, dateEmprunt, dateRetour, idLivre, idMembre);

                JOptionPane.showMessageDialog(
                        this,
                        "Emprunt enregistré!\n" +
                                "ID Emprunt: " + idEmprunt + "\n" +
                                "Date retour: " + dateRetour,
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                chargerEmprunt(); // Actualiser la liste des emprunts

            } catch (NumberFormatException e) {
                showError("Veuillez entrer des IDs valides (nombres)");
            } catch (SQLException ex) {
                showError("Erreur lors de l'enregistrement de l'emprunt: " + ex.getMessage());
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
            new Admin().setVisible(true);
        });
    }
}