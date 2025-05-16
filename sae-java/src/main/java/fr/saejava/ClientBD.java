package fr.saejava;

import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public ClientBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    public void consulterCatalogue(){
        // List de livre, requete dans la table livre de tout les livres, les afficher peut etre 5 par 5
    }

    public void passerCommande(Commande commande) throws SQLException{
        //ajoute la commande a la BD
        st = connexion.createStatement();
        // Demander les informations au client
        st.executeUpdate("");
    }

    public Commande getDerniereCommande(){
        return null;
        // A implémenter
    }0

    public Set<Livre> getHistoriqueLivresClients(){
        return null;
        // A implementer
    }

    public Set<Livre> onVousRecommande(Client client){
        // liste de tt les clients du magasin?, faire une copi de la liste et enlever le client en paramettre

        // recuperer la liste de commandes de chaques client de la liste et du client passe en paramettre

        // comparer les listes

        // faire une liste avec les livres qui ne sont pas en commun pour chaque client qui a au moins un 
        // livre en commun avec la liste du client passe en paramettre   
        return null;
    }

    public void editerFacture(Commande commande){
        // Afficher la facture dans le terminal
    }
}
