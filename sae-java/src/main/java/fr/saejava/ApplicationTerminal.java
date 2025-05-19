package fr.saejava;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.spi.DirStateFactory.Result;

public class ApplicationTerminal {
    void CréeCommande(){
        //crée la commande en cour
    }
    void commander(){
        //ajoute a la commande en cour le livre*qte
    }
    void finaliseCommande(){
        //ajoute la commande en cour a la BD
    }
    
    public static void startConnexionTest(){
        try{
            // Connexion
        ConnexionMySQL connexion = new ConnexionMySQL();
        connexion.connecter("servinfo-maria", "Librairie", "maubert", "maubert");

            // Partage de la connexion aux autres classes
        VendeurBD vendeurConnexion = new VendeurBD(connexion);
        AdminBD adminConnexion = new AdminBD(connexion);
        CommandeBD commandeConnexion = new CommandeBD(connexion);
        ClientBD clientConnexion = new ClientBD(connexion);

            // Test d'une requête
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("select * from CLIENT");
        while (r.next()){
            System.out.println(r.getString("nomcli"));
        }

            // Fermeture de la connexion
        if (connexion.isConnecte()){
            connexion.close();
        }
        }
        catch (Exception e){
            System.out.println("Erreur a la connexion");
        }
        // Commencer a implémenter des tests dans cette classe et créer des classes de test unitaire Junit
    }

    public static void main(String[] args) {
        startConnexionTest();
    }
}
