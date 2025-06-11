package fr.saejava;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class ApplicationTerminal {

    ConnexionMySQL connexion;
    Utilisateur utilisateurConnecter;
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
    

    void créeCommande(Client client,Date date,Magasin magasin) throws SQLException{
        this.panier=new Commande(this.commandeConnexion.getDerniereIdCommande()+1,date,client,magasin);
    }
    void commander(Livre livre){
        // Vérifier si le livre est dans le magasin de la commande, si oui
        // si oui : continuer la commande/ajouter le detailCommande
        // si non : vérfifier que le livre est dispo
        //ajoute a la commande en cour le livre*qte
        if(this.panier.contientLivre(livre)){
            DetailCommande detail=this.panier.livreDansCommande(livre);
            detail.setQte(detail.getQte()+1);
        }
        else{this.panier.ajouterDetailCommande(new DetailCommande(livre,this.panier));
}
    }
    
    void commander(Livre livre,int qte){
        //ajoute a la commande en cour le livre*qte
    if(this.panier.contientLivre(livre)){
            DetailCommande detail=this.panier.livreDansCommande(livre);
            detail.setQte(detail.getQte()+qte);
        }
        else{this.panier.ajouterDetailCommande(new DetailCommande(qte,livre,this.panier));
}
    }
    
    void finaliseCommande() throws SQLException{
        String ajout1="insert into COMMANDE(numcom, datecom, enligne, livraison, idcli, idmag) values\n";
        ajout1+=ajout1+"("+this.panier.getNumcom()+","+panier.getDateCommande()+","+"O"+","+"C"+","+panier.getClient().getNum()+","+panier.getMagasin().getId()+")\n";
        ajout1=ajout1+"insert into DETAILCOMMANDE(numcom, numlig, isbn, qte, prixvente) values;\n";
        int numlig=0;
        String ajout2="";
        for(DetailCommande detail:this.panier.contenue){
            ajout2=ajout2+"("+this.panier.numcom+","+numlig+","+detail.getLivre().getIsbn().toString()+","+detail.getLivre().getPrix()+")";
            if(numlig+1!=this.panier.getContenue().size()){
                ajout2+=ajout2+",\n";
            }
            else{
                ajout2=ajout2+";";
            }
            numlig+=1;  
        }
        this.st = connexion.createStatement();
        this.st.executeUpdate(ajout1);
        this.st.executeUpdate(ajout2);
        this.payer();
    }

    public ApplicationTerminal() {
        this.estConnecteBD = false;
        this.estConnecteUtil = false;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Plus Utile car Docker !
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws NoSuchElementException
     */
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
        connexion = new ConnexionMySQL();
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
        while(util==null){
            System.out.println("---- MENU CONNEXION UTILISATEUR ----");
            System.out.println("|                                  |");
            System.out.println("| > Se connecter                   |");
            System.out.println("| > S'inscrire en tant que Client  |");
            //System.out.println("| > S'inscrire en tant que Vendeur |");
            System.out.println("| > Quitter                        |");
            System.out.println("|                                  |");
            System.out.println("------------------------------------");
            System.out.print("Veuillez choisir une option (1-4) : ");
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
                    System.out.println("test");
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
                    System.out.println("Veuillez indiquer votre nom :");
                    String nom = scanner.nextLine();
                    System.out.println("Veuillez indiquer votre prenom :");
                    String prenom = scanner.nextLine();
                    System.out.println("Veuillez indiquer votre nomDUtilisateur :");
                    String nomDUtilisateur = scanner.nextLine();
                    System.out.println("Veuillez indiquer votre mot de passe :");
                    String mdp = scanner.nextLine();
                    System.out.println("Veuillez indiquer votre adresse :");
                    String adresse = scanner.nextLine();
                    System.out.println("Veuillez indiquer votre ville :");
                    String ville = scanner.nextLine();
                    System.out.println("Veuillez indiquer votre codePostal :");
                    String codePostal = scanner.nextLine();
                    //this.clientConnexion.creeCompteClient();
                    break;
                case "3":
                    System.out.println("Au revoir !");
                    connexion.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
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
        boolean continuer = true;
        // Test de méthode
        //try {
        //    System.out.println(commandeConnexion.getDerniereCommande((Client) utilisateurConnecter));
        //} catch (SQLException e) {
        //    System.out.println("Erreur lors des tests " + e.getMessage());
        //}	
        
        // Fin des tests
        while(continuer) {
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
                menuMesRecommandations();
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
                continuer = false;
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                break;
        }
        }

    }

    public void menuRechercherLivre(){
        boolean continuer = true;
        Map<Livre, Boolean> livres;
        while(continuer) {
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
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }

    public void menuCatalogue(){
        // TO DO
    }

    public void menuPanier(){
        int nbLignes=0;
        Map<Livre, Boolean> livres;
        System.out.println("------------- PANIER ------------");
        System.out.println("|                               |");

        System.out.println("| > Retour au menu principal    |");
        System.out.println("|                               |");
        System.out.println("---------------------------------");
        System.out.print("Veuillez choisir une option (1-"+nbLignes+") : ");
        String choix = scanner.nextLine();
        switch (choix) {
            case "1":
                System.out.print("Entrez le titre à rechercher : ");
                String titre = scanner.nextLine();
            
                break;
            case "2":
                System.out.print("Entrez le nom de l'auteur à rechercher : ");
                String auteur = scanner.nextLine();
            
                break;
            case "3":
                System.out.print("Entrez l'ISBN du livre à rechercher : ");
                String isbn = scanner.nextLine();
            
                break;
            case "4":
                menuClientMain();
                return;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                break;
        }
        menuRechercherLivre();
        // TO DO
        // Voir l'état actuel du panier
        // Pouvoir supprimer un livre
        // Changer la quantité d'un livre
        // Finaliser la commande (peut etre faire une autre méthode ?)
    }

    public void menuMesRecommandations(){
        try{
        afficherLivre(clientConnexion.onVousRecommande((Client) utilisateurConnecter));
        }
        catch(SQLException e ){
            System.out.println("Erreur lors de la récupération des livres a recommandé : "+e.getMessage());
        }
        return;
    }

    public void menuMesCommandes(){
        
    }

    public void menuProfil(){
        Client clientTempo = new Client(utilisateurConnecter.getId(), null, null, 0, utilisateurConnecter.getNom(), utilisateurConnecter.getPrenom(), utilisateurConnecter.getPseudo(), utilisateurConnecter.getMotDePasse());
        System.out.println("------------ PROFIL -------------");
        System.out.println("|                                       |");
        System.out.println("| Nom      > "+utilisateurConnecter.getNom()+"         |");
        System.out.println("| Prénom   > "+utilisateurConnecter.getPrenom()+"      |");
        System.out.println("| Username > "+utilisateurConnecter.getPseudo()+"         |");
        System.out.println("| Password > "+utilisateurConnecter.getMotDePasse()+"         |");
        System.out.println("| > Retour au menu principal            |");
        System.out.println("|                                       |");
        System.out.println("---------------------------------");
        System.out.print("Veuillez choisir une informations a modifier (1-4) : ");
        System.out.print("Q pour retour au menu principal ");
        String choix = scanner.nextLine().toLowerCase();
        switch (choix) {
            case "1":
                System.out.print("Entrez le nouveau nom : ");
                String nom = scanner.nextLine();
                ClientBD.modifierClient(clientTempo);
                break;
            case "2":
                System.out.print("Entrez le nouveau prénom : ");
                String prénom = scanner.nextLine();
                ClientBD.modifierClient(clientTempo);
                break;
            case "3":
                System.out.print("Entrez le nouveau username : ");
                String username = scanner.nextLine();
                ClientBD.modifierClient(clientTempo);
                break;
            case "4":
                System.out.print("Entrez le nouveau password : ");
                String password = scanner.nextLine();
                ClientBD.modifierClient(clientTempo);
                break;
            case "q":
                System.out.println("Informations sauvegardées !");
                return;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                break;
        }
        menuProfil();
    }

    public void afficherLivre(Map<Livre, Boolean> livres) {
        int nbLivre = 0;
        if (livres.isEmpty()) {
            System.out.println("Aucun livre trouvé.");
        } else {
            System.out.println("------ LIVRES TROUVÉS ------");
            System.out.println("|                          |");
            for (Map.Entry<Livre, Boolean> entry : livres.entrySet()) {
                Livre livre = entry.getKey();
                boolean disponible = entry.getValue();
                nbLivre++;
                if(disponible==true){
                    System.out.println("| "+nbLivre+") " + livre + " | Disponible : Oui |");
                }
                else{
                    System.out.println("| "+nbLivre+") "+ livre + " | Disponible : Non |");
                }
            }
            System.out.println("|                          |");
            System.out.println("----------------------------");
            System.out.println("Voulez vous commander un de ces livres ? (O/n)");
        }
    }

    public void main(){
        System.out.println("Bienvenue dans l'application de gestion de librairie !");
        // Connexion à la base de données
        // Pas util car automatisé 
        while(!estConnecteBD){
            try{
                connexion = new ConnexionMySQL();
                connexion.connecter("","","","");

                if (connexion.isConnecte()) {
                    System.out.println("Connexion à la base de données réussie !");
                    this.estConnecteBD = true;
                    
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
                if (!estConnecteBD) {
                    System.out.println("\nVoulez-vous réessayer ? (o/n) : ");
                    String reponse = scanner.nextLine().toLowerCase();
                    if (!reponse.equals("o") && !reponse.equals("oui")) {
                        System.out.println("Au revoir !");
                        System.exit(0);
                        return;
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
        while(estConnecteBD){
            if(!estConnecteUtil){
                try{
                    utilisateurConnecter = menuConnexionUtilisateur();
                    estConnecteUtil=true;
                }
                catch(SQLException e){
                    System.out.println("Voici le message d'erreur : "+ e.getMessage());
                    utilisateurConnecter = null;
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
        if(!estConnecteUtil) {
            utilisateurConnecter = null;
        }
        }
        // Si connexion utilisateur réussie

        // mettre les menus en fontion du role
    }

    public void payer(){
        System.out.println("payer");
    }

    public static void main(String[] args) {
        ApplicationTerminal app = new ApplicationTerminal();
        app.main();
    }
}
