package fr.saejava;

public abstract class Utilisateur{

    private String pseudo;
    private String motDePasse;
    private String nom;
    private String prenom;
    // private Role role;
    // Envoyer le instanceOf dans la table de la BD utilisateur table;

    public Utilisateur (String nom, String prenom, String pseudo, String motDePasse){
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}