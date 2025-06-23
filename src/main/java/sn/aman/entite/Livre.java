package sn.aman.entite;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "livre")
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "auteur")
    private String auteur;

    @Column(name = "annee_publication")
    private Integer annee_publication;

    // Getters et setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Integer getAnnee_publication() {
        return annee_publication;
    }

    public void setAnnee_publication(Integer annee_publication) {
        this.annee_publication = annee_publication;
    }
}