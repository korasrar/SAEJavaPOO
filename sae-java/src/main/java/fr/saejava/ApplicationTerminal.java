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
        ConnexionMySQL connexion = new ConnexionMySQL();
        connexion.connecter("servinfo-maria", "Librairie", "maubert", "maubert");
        VendeurBD vendeurConnexion = new VendeurBD(connexion);
        AdminBD adminConnexion = new AdminBD(connexion);
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("select * from CLIENT");
        while (r.next()){
            System.out.println(r.getString("nomcli"));
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
