package fr.saejava;

public enum Role {
    admin("admin"),
    vendeur("vendeur"),
    client("client");

    public final String role;

    private Role(String role) {
        this.role = role;
    }
}
