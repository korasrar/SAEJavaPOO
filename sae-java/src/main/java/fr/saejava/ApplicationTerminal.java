package fr.saejava;

import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ApplicationTerminal {

    Utilisateur utilisateurConnecter;
    UtilisateurBD utilisateurConnexion;
    VendeurBD vendeurConnexion; 
    AdminBD adminConnexion;
    CommandeBD commandeConnexion; 
    ClientBD clientConnexion;
    LivreBD livreConnexion;
    Scanner scanner;

    boolean estConnecteBD;
    boolean estConnecteUtil;
    

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
        this.estConnecteBD = false;
        this.estConnecteUtil = false;
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
            this.estConnecteBD = true;
            
            // Partage de la connexion aux autres classes
            utilisateurConnexion = new UtilisateurBD(connexion);
            vendeurConnexion = new VendeurBD(connexion);
            adminConnexion = new AdminBD(connexion);
            commandeConnexion = new CommandeBD(connexion);
            clientConnexion = new ClientBD(connexion);
            livreConnexion = new LivreBD(connexion);
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
                System.out.println("Veuillez entrer votre email/username : ");
                String username = scanner.nextLine();
                System.out.println("Veuillez entrer votre mot de passe : ");
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
        System.out.println("------ MENU CLIENT ------");
        System.out.println("|                       |");
        System.out.println("| > Rechercher un livre |");
        System.out.println("| > Catalogue           |");
        System.out.println("| > Mes recommandations |");
        System.out.println("| > Voir panier         |");
        System.out.println("| > Mes commandes       |");
        System.out.println("| > Mon profil          |");
        System.out.println("| > Se déconnecter      |");
        System.out.println("|                       |");
        System.out.println("-------------------------");
        System.out.print("Veuillez choisir une option (1-7) : ");
        String choix = scanner.nextLine();
        switch (choix) {
            case "1":
                menuRechercherLivre();
                break;
            case "2":
                menuCatalogue();
                break;
            case "3":
                menuPanier();
                break;
            case "4":
                menuPanier();
                break;
            case "5":
                menuMesCommandes();
                break;
            case "6":
                menuProfil();
                break;
            case "7":
                System.out.println("Déconnexion...");
                estConnecteUtil = false;
                utilisateurConnecter = null;
                try{
                    menuConnexionUtilisateur();
                }
                catch(SQLException e){
                    System.out.println("Erreur lors de la connexion : " + e.getMessage());
                }
                
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                menuClientMain();
        }

    }

    public void menuRechercherLivre(){
        Map<Livre, Boolean> livres;
        System.out.println("------ RECHERCHER UN LIVRE ------");
        System.out.println("|                               |");
        System.out.println("| > Rechercher par titre        |");
        System.out.println("| > Rechercher par auteur       |");
        System.out.println("| > Rechercher par ISBN         |");
        System.out.println("| > Retour au menu principal    |");
        System.out.println("|                               |");
        System.out.println("---------------------------------");
        System.out.print("Veuillez choisir une option (1-4) : ");
        String choix = scanner.nextLine();
        switch (choix) {
            case "1":
                System.out.print("Entrez le titre à rechercher : ");
                String titre = scanner.nextLine();
                try{
                    livres = livreConnexion.rechercherLivre(Filtre.titre,"" , titre ,"" , vendeurConnexion);
                    afficherLivre(livres);
                    System.out.print("Appuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                }
                catch(SQLException e){
                    System.out.println("Erreur lors de la recherche du livre : " + e.getMessage());
                }
                break;
            case "2":
                System.out.print("Entrez le nom de l'auteur à rechercher : ");
                String auteur = scanner.nextLine();
                try{
                    livres = livreConnexion.rechercherLivre(Filtre.auteur,"" , "" , auteur , vendeurConnexion);
                    afficherLivre(livres);
                    System.out.print("Appuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                }
                catch(SQLException e){
                    System.out.println("Erreur lors de la recherche du livre : " + e.getMessage());
                }
                break;
            case "3":
                System.out.print("Entrez l'ISBN du livre à rechercher : ");
                String isbn = scanner.nextLine();
                try{
                    livres = livreConnexion.rechercherLivre(Filtre.isbn, isbn , "" , "" , vendeurConnexion);
                    afficherLivre(livres);
                    System.out.print("Appuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                }
                catch(SQLException e){
                    System.out.println("Erreur lors de la recherche du livre : " + e.getMessage());
                }
                break;
            case "4":
                menuClientMain();
                return;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                break;
        }
        menuRechercherLivre();
    }

    public void menuCatalogue(){
        // TO DO
    }

    public void menuPanier(){
        // TO DO
        // Voir l'état actuel du panier
        // Pouvoir supprimer un livre
        // Changer la quantité d'un livre
        // Finaliser la commande (peut etre faire une autre méthode ?)
    }

    public void menuMesRecommandations(){
        // TO DO
        // La méthode onVousRecommande() de ClientBD
    }

    public void menuMesCommandes(){
        // TO DO
        // Pouvoir créer la facture d'une commande
        // Voir l'historique des commandes
        // Voir les détails d'une commande
    }

    public void menuProfil(){
       // TO DO
       // Voir ces infos (Les afficher par défaut)
       // Pouvoir modiofier ces informations (Créer la méthode pour dans UtilisateurBD peut etre ?)
    }

    public void afficherLivre(Map<Livre, Boolean> livres) {
        if (livres.isEmpty()) {
            System.out.println("Aucun livre trouvé.");
        } else {
            System.out.println("------ LIVRES TROUVÉS ------");
            System.out.println("|                          |");
            for (Map.Entry<Livre, Boolean> entry : livres.entrySet()) {
                Livre livre = entry.getKey();
                boolean disponible = entry.getValue();
                if(disponible==true){
                    System.out.println("| " + livre + " | Disponible : Oui |");
                }
                else{
                    System.out.println("| " + livre + " | Disponible : Non |");
                }
            }
            System.out.println("|                          |");
            System.out.println("----------------------------");
        }
    }

    public void main(){
        System.out.println("Bienvenue dans l'application de gestion de librairie !");

        // Connexion à la base de données
        while(!estConnecteBD){
            try{
                menuConnexionBD();
                if (!estConnecteBD) {
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
        // Si connexion BD réussie
        if(estConnecteBD){
        // Afficher le menu de connexion utilisateur
            while(!estConnecteUtil){
                try{
                utilisateurConnecter = menuConnexionUtilisateur();
                estConnecteUtil=true;

                }
                catch(SQLException e){
                    System.out.println("Voici le message d'erreur : "+ e.getMessage());
                }
            }
        }
        if (utilisateurConnecter instanceof Admin) {
            System.out.println("Bienvenue, Admin " + utilisateurConnecter.getNom() + " !");
            menuAdminMain();
        } 
        else if (utilisateurConnecter instanceof Vendeur) {
            System.out.println("Bienvenue, Vendeur " + utilisateurConnecter.getNom() + " !");
            menuVendeurMain();
        } 
        else if (utilisateurConnecter instanceof Client) {
            System.out.println("Bienvenue, Client " + utilisateurConnecter.getNom() + " !");
            menuClientMain();
        }
        // Si connexion utilisateur réussie

        // mettre les menus en fontion du role
    }

    public static void main(String[] args) {
        ApplicationTerminal app = new ApplicationTerminal();
        app.main();
    }
}
