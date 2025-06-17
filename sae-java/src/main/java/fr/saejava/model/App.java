package fr.saejava.model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    // Classe du model

    ConnexionMySQL connexion;
    Utilisateur utilisateurConnecter;
    MagasinBD magasinConnexion;
    UtilisateurBD utilisateurConnexion;
    VendeurBD vendeurConnexion;
    AdminBD adminConnexion;
    CommandeBD commandeConnexion;
    ClientBD clientConnexion;
    LivreBD livreConnexion;
    Scanner scanner;
    Statement st;
    ResultSet r;
    boolean estConnecteBD;
    boolean estConnecteUtil;
    Commande panier;
    Livre livreSelectionner;

    public App() {

    }

    public void connexionBD(){

    }

    public void connexion(String username, String password) {

    }
}
