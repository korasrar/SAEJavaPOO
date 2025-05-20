package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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



    public static void menuConnectionBD() throws SQLException, ClassNotFoundException {
            // Récupère les infos pour se connecter
        System.out.println("---- APPLICATION LIBRAIRIE ----");
        System.out.println("|                             |");
        System.out.println("| > Connexion Base de Données |");
        System.out.println("|                             |");
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le nom du serveur : ");
        String adresse = sc.nextLine();
        System.out.println("Entrez le nom de la base : ");
        String nomBD = sc.nextLine();
        System.out.println("Entrez le nom d'utilisateur : ");
        String nomUtilisateur = sc.nextLine();
        System.out.println("Entrez le mot de passe : ");
        String motDePasse = sc.nextLine();
        sc.close();
            // Connexion
        ConnexionMySQL connexion = new ConnexionMySQL();
        connexion.connecter(adresse, nomBD, nomUtilisateur, motDePasse);
        
            // Partage de la connexion aux autres classes
        VendeurBD vendeurConnexion = new VendeurBD(connexion);
        AdminBD adminConnexion = new AdminBD(connexion);
        CommandeBD commandeConnexion = new CommandeBD(connexion);
        ClientBD clientConnexion = new ClientBD(connexion);

            // Test d'une requête
        //Statement st = connexion.createStatement();
        //ResultSet r = st.executeQuery("select * from CLIENT");
        //while (r.next()){
        //    System.out.println(r.getString("nomcli"));

        //    // Fermeture de la connexion
        //if (connexion.isConnecte()){
        //    connexion.close();
        //}
        //}
    }
    
    public static void main(){
        while()
        try{
        menuConnectionBD();
        }
        catch (SQLException e){
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
        catch (ClassNotFoundException e){
           System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        main();
    }
}
