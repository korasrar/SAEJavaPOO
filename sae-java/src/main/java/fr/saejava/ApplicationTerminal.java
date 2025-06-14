package fr.saejava;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class ApplicationTerminal {

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

    /**
     * Pour afficher les livres par page
     */
    int nbObjetParPage;
    int pageCourante;
    

    /**
     * crée le panier du client
     * @param client
     * @param date
     * @param magasin
     * @throws SQLException
     */
    void créeCommande(Client client,Date date,Magasin magasin) throws SQLException{
        this.panier=new Commande(this.commandeConnexion.getDerniereIdCommande()+1,date,client,magasin, null);
    }

    /**
     * ajoute un livre au panier
     * @param livre
     */
    void commander(Livre livre){
        if(this.panier.contientLivre(livre)){
            DetailCommande detail=this.panier.livreDansCommande(livre);
            detail.setQte(detail.getQte()+1);
        }
        else{
            this.panier.ajouterDetailCommande(new DetailCommande(livre,this.panier));
        }
    }
    
    /**
     * ajoute un livre au panier avec une quantité spécifique
     * @param livre
     * @param qte
     */
    void commander(Livre livre,int qte){
        if(this.panier.contientLivre(livre)){
            DetailCommande detail=this.panier.livreDansCommande(livre);
            detail.setQte(detail.getQte()+qte);
        }
        else{
            this.panier.ajouterDetailCommande(new DetailCommande(qte,livre,this.panier));
        }
    }
    
    void finaliseCommande(ModeLivraison modeLivraison) throws SQLException{
    this.panier.setModeLivraison(modeLivraison);
    if(modeLivraison.equals(ModeLivraison.MAGASIN)){
        panier.setMagasin(menuChoisirMagasin());
    }
    else if(modeLivraison.equals(ModeLivraison.MAISON)){
        panier.setMagasin(magasinConnexion.trouverMeilleurMagasin(panier));
    }
    else{
        throw new SQLException("Mode de livraison inconnu");
    }
    
    String sqlCommande = "INSERT INTO COMMANDE(numcom, datecom, enligne, livraison, idcli, idmag) VALUES (?, ?, ?, ?, ?, ?)";
    PreparedStatement pstCommande = connexion.prepareStatement(sqlCommande);
    
    pstCommande.setInt(1, this.panier.getNumcom());
    pstCommande.setDate(2, panier.getDateCommande());
    pstCommande.setString(3, "O");
    pstCommande.setString(4, modeLivraison.equals(ModeLivraison.MAGASIN) ? "M" : "C");
    pstCommande.setInt(5, panier.getClient().getNum());
    pstCommande.setInt(6, panier.getMagasin().getId());
    
    pstCommande.executeUpdate();
    System.out.println("Commande insérée avec succès !");
    
    String sqlDetail = "INSERT INTO DETAILCOMMANDE(numcom, numlig, isbn, qte, prixvente) VALUES (?, ?, ?, ?, ?)";
    PreparedStatement pstDetail = connexion.prepareStatement(sqlDetail);
    
    int numlig = 1;
    for(DetailCommande detail : this.panier.contenue){
        pstDetail.setInt(1, this.panier.getNumcom());
        pstDetail.setInt(2, numlig);
        pstDetail.setString(3, detail.getLivre().getIsbn());
        pstDetail.setInt(4, detail.getQte());
        pstDetail.setDouble(5, detail.getLivre().getPrix());
        pstDetail.executeUpdate();
        numlig++;
    }
    
    System.out.println("Détails de la commande insérés avec succès !");
    pstCommande.close();
    pstDetail.close();
    // reset du panier
    Date date = new Date(System.currentTimeMillis());
    try{
            créeCommande((Client) utilisateurConnecter,date , null);
            }
            catch(SQLException e){
                System.out.println("Erreur lors du reset du panier " + e.getMessage());
            }
    this.payer();
}

    /**
     * Le constructeur de l'application terminal
     */
    public ApplicationTerminal() {
        this.estConnecteBD = false;
        this.estConnecteUtil = false;
        this.scanner = new Scanner(System.in);
        this.nbObjetParPage = 5;
        this.pageCourante = 1;
        this.panier = null;
    }

    /**
     * Plus Utile car Docker !
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws NoSuchElementException
     */
    public void menuConnexionBD() throws SQLException, ClassNotFoundException, NoSuchElementException {
            // Récupère les infos pour se connecter
        System.out.println("                         ");
        System.out.println("|--< APPLICATION LIBRAIRIE >--|");
        System.out.println("|                             |");
        System.out.println("| > Connexion Base de Données |");
        System.out.println("|                             |");
        System.out.println("|-----------------------------|");
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
    }

    public Utilisateur menuConnexionUtilisateur() throws SQLException{
        Utilisateur util = null;
        while(util==null){
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
                
                    //this.clientConnexion.creeCompteClient(nb++, adresse, ville, codePostal, nom, prenom, nomDUtilisateur, mdp); // get le dernier id d'utilisateur et faire ++
                    System.out.println("Votre compte a bien été enregistré");
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
       boolean continuer = true;
        while(continuer) {
        System.out.println( "-------- MENU ADMIN --------");
        System.out.println("|                           |");
        System.out.println("| > Créer un compte vendeur |");
        System.out.println("| > Ajouter une  librairie  |");
        System.out.println("| > Gérer les stocks        |");
        System.out.println("| > Voir les stats de vente |");
        System.out.println("| > Mon profil              |");
        System.out.println("| > Se déconnecter          |");
        System.out.println("|                           |");
        System.out.println("-----------------------------");
        System.out.print("Veuillez choisir une option (1-6) : ");
        String choix = scanner.nextLine();
        switch (choix) {
            case "1":
                menuCreerCompteVendeur();
                break;
            case "2":
                menuAjouterLibrairie();
                break;
            case "3":
                menuGererStocks();
                break;
            case "4":
                menuStatsDeVentes();
                break;
            case "5":
                menuProfil();
                break;
            case "6":
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

    public void menuCreerCompteVendeur(){
       boolean continuer = true;
        while(continuer) {        
        System.out.println(" ---- CREER COMPTE VENDEUR ----");
        System.out.println("|                             |");
        System.out.println("| > Créer un compte vendeur   |");
        System.out.println("|                             |");
        System.out.println("-------------------------------");
        System.out.println("Veuillez entrer les infos du compte vendeur : ");
        String login = scanner.nextLine();
        //VendeurBD.creerCompteVendeur(login);
        System.out.println("Le compte vendeur a ete cree avec succes");
        break;
        }
    }

    public void menuAjouterLibrairie() {
        boolean continuer = true;
        while(continuer) {
            System.out.println("---- AJOUTER UNE LIBRAIRIE ----");
            System.out.println("|                             |");
            System.out.println("| > Ajouter une librairie     |");
            System.out.println("|                             |");
            System.out.println("-------------------------------");
            System.out.println("Veuillez entrer le nom de la librairie : ");
            String nomLibrairie = scanner.nextLine();
           // TrucBD.ajouterLibrairie(nomLibrairie); jsp encore
            System.out.println("La librairie a ete ajoutee avec succes");
            break;
        }
    }

    public void menuGererStocks() {
        boolean continuer = true;
        while(continuer) {
            System.out.println("---- GÉRER LES STOCKS ----");
            System.out.println("|                       |");
            System.out.println("| > Ajouter un livre    |");
            System.out.println("| > Modifier un livre   |");
            System.out.println("| > Supprimer un livre  |");
            System.out.println("| > Retour au menu      |");
            System.out.println("|                       |");
            System.out.println("-------------------------");
            System.out.print("Veuillez choisir une option (1-4) : ");
            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    System.out.println("Veuillez entrer le titre du livre à ajouter : ");
                    String titre = scanner.nextLine();
                    //LivreBD.ajouterLivre(titre);
                    System.out.println("Le livre a été ajouté avec succès");
                    break;
                case "2":
                    System.out.println("Veuillez entrer le titre du livre à modifier : ");
                    String titreModif = scanner.nextLine();
                    //LivreBD.modifierLivre(titreModif);
                    System.out.println("Le livre a été modifié avec succès");
                    break;
                case "3":
                    System.out.println("Veuillez entrer le titre du livre à supprimer : ");
                    String titreSupp = scanner.nextLine();
                    //LivreBD.supprimerLivre(titreSupp);
                    System.out.println("Le livre a été supprimé avec succès");
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

    public void menuStatsDeVentes() {
        boolean continuer = true;
        while(continuer) {
            System.out.println("---- STATISTIQUES DE VENTES ----");
            System.out.println("|                             |");
            System.out.println("| > Voir les ventes du mois   |");
            System.out.println("| > Voir les ventes de l'année |");
            System.out.println("| > Retour au menu principal   |");
            System.out.println("|                             |");
            System.out.println("-------------------------------");
            System.out.print("Veuillez choisir une option (1-3) : ");
            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    // jsp
                    break;
                case "2":
                    // jsp
                    break;
                case "3":
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }
        }
    }




    public void menuVendeurMain() {
        System.out.println("--------- MENU VENDEUR ----------");
        System.out.println("|                               |");
        System.out.println("| > Rechercher un livre         |");
        System.out.println("| > Catalogue                   |");
        System.out.println("| > Mes recommandations         |");
        System.out.println("| > Voir panier                 |");
        System.out.println("| > Mes commandes               |");
        System.out.println("| > Mon profil                  |");
        System.out.println("| > Transferer un livre         |");
        System.out.println("| > Maj des stocks              |");
        System.out.println("| > Verifier les disponibilités |");
        System.out.println("| > Se déconnecter              |");
        System.out.println("|                               |");
        System.out.println("---------------------------------"); 
        System.out.print("Veuillez choisir une option (1-10) : ");
        String choix = scanner.nextLine();
        switch (choix) {
            case "1":
                menuRechercherLivre();
                break;
            case "2":
                // rien
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
                menuTransfertLivre();
                break;
            case "8":
                menuMajStock();
                break;
            case "9":
                menuDispo();
                break;
            case "10":
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


    public void menuClientMain() {
        boolean continuer = true;
        while(continuer) {
        System.out.println("------ MENU CLIENT ------");
        System.out.println("|                       |");
        System.out.println("| > Rechercher un livre |");
        System.out.println("| > Mes recommandations |");
        System.out.println("| > Voir panier         |");
        System.out.println("| > Mes commandes       |");
        System.out.println("| > Mon profil          |");
        System.out.println("| > Se déconnecter      |");
        System.out.println("|                       |");
        System.out.println("-------------------------");
        System.out.print("Veuillez choisir une option (1-6) : ");
        String choix = scanner.nextLine();
        switch (choix) {
            case "1":
                menuRechercherLivre();
                break;
            case "2":
                menuMesRecommandations();
                break;
            case "3":
                menuPanier();
                break;
            case "4":
                menuMesCommandes();
                break;
            case "5":
                menuProfil();
                break;
            case "6":
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

    public void menuPanier(){
        if (panier == null || panier.getContenue().isEmpty()) {
            System.out.println("------------- PANIER ------------");
            System.out.println("|                               |");
            System.out.println("| Votre panier est vide         |");
            System.out.println("|                               |");
            System.out.println("---------------------------------");
            System.out.println("Appuyez sur Entrée pour retourner au menu principal...");
            scanner.nextLine();
            return;
        }

        int totalPages = (int) panier.getContenue().size()/nbObjetParPage;
        if(totalPages==0){
            totalPages=1;
        }
        pageCourante = 1;
        boolean continuer = true;
        while(continuer){
            System.out.println("------------- PANIER ------------");
            System.out.println("|                               |");
            List<DetailCommande> listeLivres = new ArrayList<>(panier.getContenue());
            for(int i =(pageCourante-1)*nbObjetParPage;i<nbObjetParPage+(pageCourante-1)*nbObjetParPage && i<listeLivres.size();i++){
                Livre livre = listeLivres.get(i).getLivre();
                System.out.println("| "+(i+1)+") " + livre + "| qte : "+listeLivres.get(i).getQte()+"|");
            }
            System.out.println("| Page : " + pageCourante + "/" + totalPages + "                    |");
            System.out.println("|                               |");
            System.out.println("---------------------------------");
            System.out.println("Voulez-vous valider la commande ? (O/n)");
            System.out.println("Pour modifier la commande : m");
            System.out.println("Pour changer de page : < | >");
            
            String reponse = scanner.nextLine().toLowerCase();
            
            if (reponse.equals("o") || reponse.equals("oui")) {
                try {
                    String modeLivraison =null;
                    while(modeLivraison==null) {
                        System.out.println("Veuillez choisir le mode de livraison (M=magasin/C=maison):");
                        modeLivraison = scanner.nextLine().toLowerCase();
                        if(modeLivraison.equals("m") || modeLivraison.equals("magasin")) {
                            finaliseCommande(ModeLivraison.MAGASIN);
                        } else if(modeLivraison.equals("c") || modeLivraison.equals("maison")) {
                            finaliseCommande(ModeLivraison.MAISON);
                        } else {
                            System.out.println("Mode de livraison invalide, veuillez réessayer.");
                            modeLivraison=null;
                        }
                    }
                    System.out.println("Commande validée avec succès !");
                    continuer = false;
                } catch (SQLException e) {
                    System.out.println("Erreur lors de la validation de la commande : " + e.getMessage());
                }
            } 
            else {
                    System.out.println("Commande annulée.");
            }
            if (reponse.equals("<")) {
                if (pageCourante > 1) {
                    pageCourante--;
                } else {
                    System.out.println("Vous êtes déjà à la première page.");
                }
            } 
            else if (reponse.equals(">")) {
                if (pageCourante < totalPages) {
                    pageCourante++;
                } else {
                    System.out.println("Vous êtes déjà à la dernière page.");
                }
            } 
            else if (reponse.equals("n")) {
                continuer = false;
            } 
            else if (reponse.equals("m")) {
                System.out.print("Entrez le numéro du livre à modifier (1-"+listeLivres.size()+") : ");
                String livreSelec = scanner.nextLine();
                try {
                    int numLivre = Integer.parseInt(livreSelec);
                    if (numLivre >= 1 && numLivre <=nbObjetParPage) {
                        int indexLivre = numLivre - 1 + (pageCourante - 1) * nbObjetParPage;
                        DetailCommande detail = panier.getContenue().get(indexLivre);
                        
                        System.out.println("Livre sélectionné : " + detail.getLivre().getTitre());
                        System.out.println("Quantité actuelle : " + detail.getQte());
                        System.out.println();
                        System.out.println("1 > Modifier la quantité");
                        System.out.println("2 > Supprimer du panier");
                        System.out.println("3 > Retour");
                        String choix = scanner.nextLine();
                        switch (choix) {
                            case "1":
                                System.out.print("Nouvelle quantité : ");
                                try {
                                    int nouvelleQte = Integer.parseInt(scanner.nextLine());
                                    if (nouvelleQte > 0) {
                                        detail.setQte(nouvelleQte);
                                        System.out.println("Quantité mise à jour !");
                                    } else {
                                        System.out.println("La quantité doit être supérieure à 0.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Veuillez entrer un nombre valide.");
                                }
                                break;
                            case "2":
                                System.out.print("Êtes-vous sûr de supprimer ce livre ? (O/n) : ");
                                String confirmSuppr = scanner.nextLine().toLowerCase();
                                if (confirmSuppr.equals("o") || confirmSuppr.equals("oui")) {
                                    panier.getContenue().remove(indexLivre);
                                    System.out.println("Livre supprimé du panier !");
                                    
                                    totalPages = (int) panier.getContenue().size()/nbObjetParPage;
                                    
                                    if (panier.getContenue().isEmpty()) {
                                        System.out.println("Votre panier est maintenant vide.");
                                        continuer = false;
                                    }
                                }
                                break;
                            case "3":
                                break;
                            default:
                                System.out.println("Choix invalide.");
                                break;
                        }
                    } else {
                        System.out.println("Numéro invalide.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre.");
                }
            }
        }
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
        List<Commande> commandes = commandeConnexion.getCommandes((Client) utilisateurConnecter);
        
        if (commandes.isEmpty()) {
            System.out.println("------------- MES COMMANDES ------------");
            System.out.println("|                                      |");
            System.out.println("| Vous n'avez aucune commande         |");
            System.out.println("|                                      |");
            System.out.println("----------------------------------------");
            System.out.println("Appuyez sur Entrée pour retourner au menu principal...");
            scanner.nextLine();
            return;
        }

        int totalPages = (int) Math.ceil((double) commandes.size() / nbObjetParPage);
        pageCourante = 1;
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("------------- MES COMMANDES ------------");
            System.out.println("|                                      |");
            
            for (int i = (pageCourante - 1) * nbObjetParPage; i < nbObjetParPage + (pageCourante - 1) * nbObjetParPage && i < commandes.size(); i++) {
                Commande commande = commandes.get(i);
                String modeLivraison = commande.getModeLivraison() == ModeLivraison.MAGASIN ? "Magasin" : "Domicile";
                
                System.out.println("| " + (i + 1) + "> Commande #" + commande.getNumcom());
                System.out.println("|    Date: " + commande.getDateCommande());
                System.out.println("|    Livraison: " + modeLivraison);
                System.out.println("|    Total: " + commande.prixTotal() + "€");
                System.out.println("|                                      |");
            }
            System.out.println("| Page : " + pageCourante + "/" + totalPages + "                             |");
            System.out.println("|                                      |");
            System.out.println("----------------------------------------");
            System.out.println("Pour éditer une facture : f");
            System.out.println("Pour changer de page : < | >");
            System.out.println("Pour retourner au menu : q");
            
            String reponse = scanner.nextLine().toLowerCase();
            
            if (reponse.equals("f") || reponse.equals("facture")) {
                System.out.print("Entrez le numéro de la commande pour éditer la facture (1-" + 
                    Math.min(nbObjetParPage, commandes.size() - (pageCourante - 1) * nbObjetParPage) + ") : ");
                try {
                    int choixCommande = Integer.parseInt(scanner.nextLine());
                    int indexCommande = choixCommande - 1 + (pageCourante - 1) * nbObjetParPage;
                    
                    if (indexCommande >= 0 && indexCommande < commandes.size()) {
                        ((Client) utilisateurConnecter).editerFacture(commandes.get(indexCommande));
                    } else {
                        System.out.println("Numéro de commande invalide.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }
            else if (reponse.equals("<")) {
                if (pageCourante > 1) {
                    pageCourante--;
                } else {
                    System.out.println("Vous êtes déjà à la première page.");
                }
            }
            else if (reponse.equals(">")) {
                if (pageCourante < totalPages) {
                    pageCourante++;
                } else {
                    System.out.println("Vous êtes déjà à la dernière page.");
                }
            }
            else if (reponse.equals("q") || reponse.equals("n") || reponse.equals("non")) {
                continuer = false;
            }
            else {
                System.out.println("Réponse invalide, veuillez réessayer.");
            }
        }
}

    public void menuProfil(){
        Client clientTempo = new Client(utilisateurConnecter.getId(), null, null, 0, utilisateurConnecter.getNom(), utilisateurConnecter.getPrenom(), utilisateurConnecter.getPseudo(), utilisateurConnecter.getMotDePasse());
        boolean continuer = true;
        while(continuer) {
        System.out.println("------------ PROFIL -------------");
        System.out.println("|                                       |");
        System.out.println("| Nom      > "+utilisateurConnecter.getNom()+"         |");
        System.out.println("| Prénom   > "+utilisateurConnecter.getPrenom()+"      |");
        System.out.println("| Username > "+utilisateurConnecter.getPseudo()+"         |");
        System.out.println("| Password > "+utilisateurConnecter.getMotDePasse()+"         |");
        System.out.println("| > Retour au menu principal            |");
        System.out.println("|                                       |");
        System.out.println("---------------------------------");
        System.out.println("Veuillez choisir une informations a modifier (1-4) : ");
        System.out.println("Pour retourner au menu principal : q ");
        String choix = scanner.nextLine().toLowerCase();
        switch (choix) {
            case "1":
                System.out.print("Entrez le nouveau nom : ");
                String nom = scanner.nextLine();
                clientTempo.setNom(nom);
                utilisateurConnecter.setNom(nom);
                break;
            case "2":
                System.out.print("Entrez le nouveau prénom : ");
                String prénom = scanner.nextLine();
                clientTempo.setPrenom(prénom);
                utilisateurConnecter.setPrenom(prénom);
                break;
            case "3":
                System.out.print("Entrez le nouveau username : ");
                String username = scanner.nextLine();
                if (username.isEmpty()) {
                    System.out.println("Le nom d'utilisateur ne peut pas être vide.");
                    continue;
                }
                else{
                    clientTempo.setPseudo(username);
                    utilisateurConnecter.setPseudo(username);
                }
                break;
            case "4":
                System.out.print("Entrez le nouveau password : ");
                String password = scanner.nextLine();
                if (password.isEmpty()) {
                    System.out.println("Le mot de passe ne peut pas être vide.");
                    return;
                }
                else{
                    clientTempo.setMotDePasse(password);
                    utilisateurConnecter.setMotDePasse(password);
                }
                break;
            case "q":
                System.out.println("Informations sauvegardées !");
                continuer = false;
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                break;
            }
        }
        try{
            clientConnexion.modifierClient(clientTempo);
        }
        catch(SQLException e){
            System.out.println("Erreur lors de la modification du profil : " + e.getMessage());
        }
    }

    private Magasin menuChoisirMagasin() {
        List<Magasin> magasins;
        try{
            magasins = magasinConnexion.chargerMagasin();
        }
        catch(SQLException e){
            System.out.println("Erreur lors de la récupération des magasins : " + e.getMessage());
            return null;
        }
        boolean continuer = true;
        while(continuer) {
            System.out.println("------ CHOISIR UN MAGASIN ------");
            System.out.println("|                              |");
            for(Magasin magasin : magasins) {
                System.out.println("| " + magasin.getNom() + " - " + magasin.getVille() + " |");
            }
            System.out.println("| > Retour au menu principal    |");
            System.out.println("|                              |");
            System.out.println("---------------------------------");
            System.out.print("Veuillez choisir une option (1-"+(magasins.size()+1)+") : ");
            String choix = scanner.nextLine();
            if(choix.equals(magasins.size()+1+"")){
                continuer = false;
            }
            else{
                try{
                    int indexMagasin = Integer.parseInt(choix) - 1;
                    if(indexMagasin >= 0 && indexMagasin < magasins.size()) {
                        System.out.println("Magasin sélectionné : " + magasins.get(indexMagasin));
                        continuer = false;
                        return magasins.get(indexMagasin);
                    } else {
                        System.out.println("Numéro de magasin invalide, veuillez réessayer.");
                    }
                }
                catch(NumberFormatException e){
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }
        }
        return null;
    }

    public void menuTransfertLivre(){
        // a coder
    }

    public void menuMajStock(){
        // a coder
    }

    public void menuDispo(){
        // a coder
    }

    public void afficherLivre(Map<Livre, Boolean> livres) {
        int totalPages = (int) livres.size()/nbObjetParPage;
        if(totalPages==0){
            totalPages=1;
        }
        pageCourante = 1;
        boolean continuer = true;
        while(continuer){
            if (livres.isEmpty()) {
                System.out.println("Aucun livre trouvé.");
            } else {
                System.out.println("------------------- LIVRES TROUVÉS -------------------");
                System.out.println("|                                                    |");
                List<Livre> listeLivres = new ArrayList<>(livres.keySet());
                for(int i =(pageCourante-1)*nbObjetParPage;i<nbObjetParPage+(pageCourante-1)*nbObjetParPage && i<listeLivres.size();i++){
                    Livre livre = listeLivres.get(i);
                    boolean disponible = livres.get(livre);
                    if(disponible==true){
                        System.out.println("| "+(listeLivres.indexOf(livre)+1)+") " + livre + " | Disponible : Oui |");
                    }
                    else{
                        System.out.println("| "+(listeLivres.indexOf(livre)+1)+") "+ livre + " | Disponible : Non |");
                    }
                }
                System.out.println("| Page : "+pageCourante+"/"+totalPages+"                                           |");
                System.out.println("|                                                    |");
                System.out.println("------------------------------------------------------");
                System.out.println("Voulez vous commander un de ces livres ? (O/n)");
                System.out.println("Pour changer de page : < | >");
                String reponse = scanner.nextLine().toLowerCase();
                if (reponse.equals("o") || reponse.equals("oui")) {
                    System.out.print("Entrez le numéro du livre à commander (1-" + nbObjetParPage + ") : ");
                    int choixLivre = Integer.parseInt(scanner.nextLine());
                    Livre livreChoisi = listeLivres.get(choixLivre-1+(pageCourante - 1) * nbObjetParPage);
                    System.out.println("Quelle quantité souhaitez-vous commander ? ");
                    int quantite = Integer.parseInt(scanner.nextLine());
                    boolean quantiteDisponible = false;
                    try{
                        quantiteDisponible = vendeurConnexion.verifierQteDispo(livreChoisi, quantite);
                    }
                    catch (SQLException e) {
                        System.out.println("Problème lors de la vérification de la quantité disponible : " + e.getMessage());
                    }
                    catch(PasStockPourLivreException e){
                        System.out.println("Pas assez de stock pour le livre " + livreChoisi.getTitre() + ". Veuillez choisir une quantité inférieure.");
                    }
                    
                    if(livres.get(livreChoisi)==true && quantiteDisponible==true){
                        try{
                        commander(livreChoisi, quantite);
                        }
                        catch (IndexOutOfBoundsException e) {
                            System.out.println("Numéro de livre invalide. Veuillez réessayer.");
                        }
                    }
                    else{
                        System.out.println("Livre non disponible, veuillez en choisir un autre.");
                    }
                    
                }
                else if (reponse.equals("<")) {
                    if (pageCourante > 1) {
                        pageCourante--;
                    } else {
                        System.out.println("Vous êtes déjà à la première page.");
                    }
                } else if (reponse.equals(">")) {
                    if (pageCourante < totalPages) {
                        pageCourante++;
                    } else {
                        System.out.println("Vous êtes déjà à la dernière page.");
                    }
                } else if (reponse.equals("n") || reponse.equals("non")) {
                    continuer = false;
                } else {
                    System.out.println("Réponse invalide, veuillez réessayer.");
                }
            }
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
                    magasinConnexion = new MagasinBD(connexion);
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
            Date date = new Date(System.currentTimeMillis());
            try{
            créeCommande((Client) utilisateurConnecter,date , null);
            }
            catch(SQLException e){
                System.out.println("Erreur lors de la création de la commande : " + e.getMessage());
            }
            menuClientMain();
        }
        if(!estConnecteUtil) {
            utilisateurConnecter = null;
        }
        }
    }

    public void payer(){
        System.out.println("payer");
    }

    public static void main(String[] args) {
        ApplicationTerminal app = new ApplicationTerminal();
        app.main();
    }
}
