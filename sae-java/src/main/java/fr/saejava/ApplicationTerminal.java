package fr.saejava;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ApplicationTerminal {

    Utilisateur utilisateurConnecter;
    UtilisateurBD utilisateurConnexion;
    VendeurBD vendeurConnexion; 
    AdminBD adminConnexion;
    CommandeBD commandeConnexion; 
    ClientBD clientConnexion;
    boolean estConnecte;
    Scanner scanner;

    void CréeCommande(){
        //crée la commande en cour
    }
    void commander(){
        //ajoute a la commande en cour le livre*qte
    }
    void finaliseCommande(){
        //ajoute la commande en cour a la BD
    }

    public ApplicationTerminal() {
        this.estConnecte = false;
        this.scanner = new Scanner(System.in);
    }

    public void menuConnexionBD() throws SQLException, ClassNotFoundException, NoSuchElementException {
            // Récupère les infos pour se connecter
        System.out.println("---- APPLICATION LIBRAIRIE ----");
        System.out.println("|                             |");
        System.out.println("| > Connexion Base de Données |");
        System.out.println("|                             |");
        System.out.println("-------------------------------");
        System.out.println("Entrez le nom du serveur : ");
        String adresse = scanner.nextLine();
        System.out.println("Entrez le nom de la base : ");
        String nomBD = scanner.nextLine();
        System.out.println("Entrez le nom d'utilisateur : ");
        String nomUtilisateur = scanner.nextLine();
        System.out.println("Entrez le mot de passe : ");
        String motDePasse = scanner.nextLine();
        
            // Connexion
        ConnexionMySQL connexion = new ConnexionMySQL();
        connexion.connecter(adresse, nomBD, nomUtilisateur, motDePasse);
        
        // Vérifier si la connexion a réussi
        if (connexion.isConnecte()) {
            System.out.println("Connexion à la base de données réussie !");
            this.estConnecte = true;
            
            // Partage de la connexion aux autres classes
            utilisateurConnexion = new UtilisateurBD(connexion);
            vendeurConnexion = new VendeurBD(connexion);
            adminConnexion = new AdminBD(connexion);
            commandeConnexion = new CommandeBD(connexion);
            clientConnexion = new ClientBD(connexion);
            System.out.println("Partage de la connexion réussie ! ");
        } else {
            System.out.println("Échec de la connexion à la base de données. Veuillez réessayer.");
        }

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

    public Utilisateur menuConnexionUtilisateur() throws SQLException{
        Utilisateur util = null;
        System.out.println("---- MENU CONNEXION UTILISATEUR ----");
        System.out.println("|                                  |");
        System.out.println("| > Se connecter                   |");
        System.out.println("| > S'inscrire en tant que Client  |");
        System.out.println("| > Quitter                        |");
        System.out.println("|                                  |");
        System.out.println("------------------------------------");
        System.out.print("Veuillez choisir une option (1-3) : ");
        String choix = scanner.nextLine();
        switch (choix) {
            // Connexion, trouve si ADMIN, VENDEUR, CLIENT
            case "1":
                System.out.println("Veuillez votre email/username : ");
                String username = scanner.nextLine();
                System.out.println("Veuillez votre mot de passe : ");
                String motDePasse = scanner.nextLine();
                System.out.println("Connexion en cours...");
                try{
                util = utilisateurConnexion.getUtilisateur(username, motDePasse);
                }
                catch(UtilisateurIntrouvableException e){
                    System.out.println("Utilisateur Introuvable");
                }
                catch(MotDePasseIncorrectException e){
                    System.out.println("Mot de passe incorrect");
                }
                catch(VendeurSansMagasinException e){
                    System.out.println("Votre compte Vendeur n'a pas de magasin associé");
                }
                break;
            case "2":
                System.out.println("Inscription en cours...");
                break;
            case "3":
                System.out.println("Au revoir !");
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                menuConnexionUtilisateur();
        }
        return util;
    }

    public void menuAdminMain() {
        // TO DO
    }

    public void menuVendeurMain() {
        // TO DO
    }

    public void menuClientMain() {
        // TO DO
    }


    public void main(){
        System.out.println("Bienvenue dans l'application de gestion de librairie !");

        // Connexion à la base de données
        while(!estConnecte){
            try{
                menuConnexionBD();
                if (!estConnecte) {
                    System.out.println("\nVoulez-vous réessayer ? (o/n) : ");
                    String reponse = scanner.nextLine().toLowerCase();
                    if (!reponse.equals("o") && !reponse.equals("oui")) {
                        System.out.println("Au revoir !");
                        break;
                    }
                }
            }
            catch (SQLException e){
                System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
                System.out.println("Veuillez vérifier vos paramètres de connexion.\n");
            }
            catch (ClassNotFoundException e){
                System.out.println("Erreur de driver de base de données : " + e.getMessage());
                System.out.println("Veuillez vérifier que le driver MariaDB est disponible.\n");
            }
            catch (NoSuchElementException e){
                System.out.println("Erreur de saisie : " + e.getMessage());
                System.out.println("Veuillez réessayer.\n");
            }
        }
        if(estConnecte){
        // test 
        

        

        
        // Afficher le menu de connexion utilisateur
        try{
        utilisateurConnecter = menuConnexionUtilisateur();
        }
        catch(SQLException e){
            System.out.println("Voici le message d'erreur : "+ e.getMessage());
        }
        }
        // mettre les menus en fontion du role
    }

    public static void main(String[] args) {
        ApplicationTerminal app = new ApplicationTerminal();
        app.main();
    }
}
