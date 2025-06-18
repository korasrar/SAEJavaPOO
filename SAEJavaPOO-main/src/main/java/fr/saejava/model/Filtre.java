package fr.saejava.model;

public enum Filtre {
    isbn("ISBN"),
    auteur("Auteur"),
    titre("Titre");

    public final String label;

    private Filtre(String label) {
        this.label = label;
    }

}
