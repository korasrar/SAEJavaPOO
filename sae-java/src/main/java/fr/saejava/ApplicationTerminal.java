package fr.saejava;

import java.sql.Date;
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
    Livre livreSelectionner;

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
            System.out.println("test magasin");
            panier.setMagasin(menuChoisirMagasin());
        }
        else if(modeLivraison.equals(ModeLivraison.MAISON)){
            System.out.println("test maison");
            panier.setMagasin(magasinConnexion.trouverMeilleurMagasin(panier));
        }
        else{
            throw new SQLException("Mode de livraison inconnu");
        }
        String ajout1="insert into COMMANDE(numcom, datecom, enligne, livraison, idcli, idmag) values\n";
        ajout1+=ajout1+"("+this.panier.getNumcom()+","+panier.getDateCommande()+","+"O"+","+"C"+","+panier.getClient().getNum()+","+panier.getMagasin().getId()+")\n";
        ajout1=ajout1+"insert into DETAILCOMMANDE(numcom, numlig, isbn, qte, prixvente) values;\n";
        int numlig=1;
        String ajout2="";
        for(DetailCommande detail:this.panier.contenue){
            // Vérifier si le livre est disponible dans le magasin
            ajout2=ajout2+"("+this.panier.getNumcom()+","+numlig+","+detail.getLivre().getIsbn().toString()+","+detail.getLivre().getPrix()+")";
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
                
                    this.clientConnexion.creeCompteClient((utilisateurConnexion.getDernierID()+1), adresse, ville, Integer.parseInt(codePostal), nom, prenom, nomDUtilisateur, mdp);
                    System.out.println("Votre compte a bien été enregistré");
                    try{
                    util = utilisateurConnexion.getUtilisateur(nomDUtilisateur, mdp);
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
        System.out.println("| > Créer un compte vendeur |"); //fait
        System.out.println("| > Ajouter un magasin      |"); //fait
        System.out.println("| > Gérer les stocks        |"); //ajouter livre fait
        System.out.println("| > Voir les stats de vente |");
        System.out.println("| > Mon profil              |"); //fait
        System.out.println("| > Se déconnecter          |"); //fait
        System.out.println("|                           |");
        System.out.println("-----------------------------");
        System.out.print("Veuillez choisir une option (1-6) : ");
        String choix = scanner.nextLine();
        switch (choix) {
            case "1":
                menuCreerCompteVendeur();
                break;
            case "2":
                menuAjouterMagasin();
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
        System.out.println("Veuillez entrer le nom du nouveau vendeur : ");
            String nomVendeur = scanner.nextLine();
            System.out.println("Veuillez entrer le nouveau prénom : ");
            String prenomVendeur = scanner.nextLine();
            System.out.println("Veuillez entrer le nouvel username : ");
            String usernameVendeur = scanner.nextLine();
            System.out.println("Veuillez entrer le nouveau mot de passe : ");
            String motDePasseVendeur = scanner.nextLine();
            try {
                Magasin magasin = menuChoisirMagasin();
                if (magasin != null) {
                    adminConnexion.creerCompteVendeur((utilisateurConnexion.getDernierID()+1), nomVendeur, prenomVendeur, usernameVendeur, motDePasseVendeur);
                    System.out.println("Compte vendeur créé avec succès.");
                } else {
                    System.out.println("Aucun magasin sélectionné. Le compte n'a pas été créé.");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la création du compte vendeur : " + e.getMessage());
            }
            continuer = false;
        }
    }

    public void menuAjouterMagasin() {
    boolean continuer = true;
    while(continuer) {
        System.out.println("---- AJOUTER UN MAGASIN ----");
        System.out.println("|                           |");
        System.out.println("| > Ajouter un magasin      |");
        System.out.println("|                           |");
        System.out.println("-----------------------------");
        
        try {
            System.out.print("Veuillez entrer l'id du magasin : ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Veuillez entrer le nom du magasin : ");
            String nom = scanner.nextLine();
            System.out.print("Veuillez entrer la ville du magasin : ");
            String ville = scanner.nextLine();
            Magasin nouveauMagasin = new Magasin(id, nom, ville);
            adminConnexion.ajouterMagasin(nouveauMagasin);
            System.out.println("Magasin ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du magasin : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        continuer = false;
    }
    }

    public void menuGererStocks() {
        boolean continuer = true;
        while(continuer) {
            System.out.println("---- GÉRER LES STOCKS ----");
            System.out.println("|                       |");
            System.out.println("| > Ajouter un livre    |"); //fait
            System.out.println("| > Modifier un livre   |");
            System.out.println("| > Supprimer un livre  |"); //fait
            System.out.println("|                       |");
            System.out.println("-------------------------");
            System.out.print("Veuillez choisir une option (1-4) : ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    System.out.println("---- AJOUTER UN LIVRE ----");
                    System.out.println("|                       |");
                    System.out.println("| > Ajouter un livre    |");
                    System.out.println("|                       |");
                    System.out.println("-------------------------");
                    System.out.print("Veuillez entrer l'ISBN : ");
                    String isbn = scanner.nextLine();
                    System.out.print("Veuillez entrer le titre : ");
                    String titre = scanner.nextLine();
                    System.out.print("Veuillez entrer l'année de publication : ");
                    String anneePubli = scanner.nextLine();
                    System.out.print("Veuillez entrer le prix : ");
                    double prix = Double.parseDouble(scanner.nextLine());
                    System.out.print("Veuillez entrer le nombre de pages : ");
                    int pages = Integer.parseInt(scanner.nextLine());
                    Livre livre = new Livre(isbn, titre, pages, anneePubli, prix);

                    System.out.print("Voulez-vous ajouter un auteur ? (oui/non) : ");
                    String reponseAuteur = scanner.nextLine().toLowerCase();
                    List<Auteur> auteursLivre = new ArrayList<>();
                    if (reponseAuteur.equals("oui") || reponseAuteur.equals("o")) {
                        boolean continuerAuteurs = true;
                        
                        while (continuerAuteurs) {
                            System.out.print("Nom de l'auteur : ");
                            String nomAuteur = scanner.nextLine();
                            try {
                                List<Auteur> listAuteur = livreConnexion.rechercheAuteur(nomAuteur);
                                if (listAuteur.isEmpty()) {
                                    System.out.println("Aucun auteur trouvé avec ce nom.");
                                    System.out.print("Voulez-vous créer un nouvel auteur ? (oui/non) : ");
                                    String creerAuteur = scanner.nextLine().toLowerCase();
                                    if (creerAuteur.equals("oui") || creerAuteur.equals("o")) {
                                        System.out.print("Nom de l'auteur : ");
                                        String nomAuteur2 = scanner.nextLine();
                                        System.out.print("Annee de naissance: ");
                                        String dateNaissance = scanner.nextLine();
                                        System.out.print("Veuillez entrer l'annee de deces, sinon laissez vide: ");
                                        String dateDeces = scanner.nextLine();
                                        if (dateDeces.trim().isEmpty()) {
                                            dateDeces = null;
                                        }
                                        Auteur nouvelAuteur = new Auteur((livreConnexion.getDernierIDAuteur()+1), nomAuteur2, Integer.parseInt(dateNaissance), Integer.parseInt(dateDeces));
                                        livreConnexion.insererNouvelAuteur(nouvelAuteur);
                                    }
                                    }
                                    else {
                                    System.out.println("Voici les auteurs trouvés :");
                                    for (int i = 0; i < listAuteur.size(); i++) {
                                        Auteur auteur = listAuteur.get(i);
                                        System.out.println((i+1) + ". " + auteur.getNomAuteur());
                                        }
                                        System.out.print("Choisissez un auteur (numéro) : ");
                                        int choixAuteur = Integer.parseInt(scanner.nextLine()) - 1;
                                    if (choixAuteur >= 0 && choixAuteur < listAuteur.size()) {
                                        auteursLivre.add(listAuteur.get(choixAuteur));
                                        System.out.println("Auteur ajouté au livre.");
                                    } else {
                                        System.out.println("Choix invalide.");
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("Erreur lors de la recherche de l'auteur : " + e.getMessage());
                            } catch (NumberFormatException e) {
                            System.out.println("Veuillez entrer un nombre valide.");
                            }
                        }// fin while auteurs
                        }// fin if reponse auteur

                        System.out.print("Voulez-vous ajouter une classification? (oui/non) : ");
                        String reponseClassification = scanner.nextLine().toLowerCase();
                        List<Classification> classificationLivre = new ArrayList<>();
                
                        if (reponseClassification.equals("oui") || reponseClassification.equals("o")) {
                        boolean continuerClassification = true;
                        
                        while (continuerClassification) {
                            System.out.print("Nom de la classification : ");
                            String nomClassification = scanner.nextLine();
                            try {
                                List<Classification> listClassifications = livreConnexion.rechercheClassification(nomClassification);
                                if (listClassifications.isEmpty()) {
                                    System.out.println("Aucune classification trouvé avec ce nom.");
                                    System.out.print("Voulez-vous créer une nouvelle classification ? (oui/non) : ");
                                    String creerClassification = scanner.nextLine().toLowerCase();
                                    
                                    if (creerClassification.equals("oui") || creerClassification.equals("o")) {
                                        System.out.print("Nom de la classification : ");
                                        String nomClassification2 = scanner.nextLine();
                                        Classification nouvelClassification = new Classification((livreConnexion.getDernierIDClassification()+1), nomClassification);
                                        livreConnexion.insererNouvelleClassification(nouvelClassification);
                                    }
                                    } 
                                    else {
                                    System.out.println("Voici les classifications trouvées :");
                                    for (int i = 0; i < listClassifications.size(); i++) {
                                        Classification classification = listClassifications.get(i);
                                        System.out.println((i+1) + ". " + classification.getNomClass());
                                        }
                                        System.out.print("Choisissez une classification (numéro) : ");
                                        int choixClass = Integer.parseInt(scanner.nextLine()) - 1;
                                    if (choixClass >= 0 && choixClass < listClassifications.size()) {
                                        classificationLivre.add(listClassifications.get(choixClass));
                                        System.out.println("Classification ajoutée au livre.");
                                    } else {
                                        System.out.println("Choix invalide.");
                                    }
                                }
                            } catch (SQLException e3) {
                                System.out.println("Erreur lors de la recherche de la classification : " + e3.getMessage());
                            } catch (NumberFormatException e3) {
                            System.out.println("Veuillez entrer un nombre valide.");
                            }
                        }// fin while classifications
                        }// fin if reponse classification
                        
                        System.out.print("Voulez-vous ajouter un éditeur? (oui/non) : ");
                        String reponseEditeur = scanner.nextLine().toLowerCase();
                        List<Editeur> editeursLivre = new ArrayList<>();
                
                        if (reponseEditeur.equals("oui") || reponseEditeur.equals("o")) {
                        boolean continuerEditeurs = true;
                        
                        while (continuerEditeurs) {
                            System.out.print("Nom de l'editeur : ");
                            String nomEditeur = scanner.nextLine();
                            try {
                                List<Editeur> listEditeurs = livreConnexion.rechercheEditeur(nomEditeur);
                                if (listEditeurs.isEmpty()) {
                                    System.out.println("Aucun editeur trouvé avec ce nom.");
                                    System.out.print("Voulez-vous créer un nouvel editeur ? (oui/non) : ");
                                    String creerEditeur = scanner.nextLine().toLowerCase();
                                    
                                    if (creerEditeur.equals("oui") || creerEditeur.equals("o")) {
                                        System.out.print("Nom de l'editeur : ");
                                        String nomEditeur2 = scanner.nextLine();
                                        Editeur nouvelEditeur = new Editeur((livreConnexion.getDernierIDEditeur()+1), nomEditeur2);
                                        livreConnexion.insererNouvelEditeur(nouvelEditeur);
                                    }
                                    } 
                                    else {
                                    System.out.println("Voici les editeurs trouvés :");
                                    for (int i = 0; i < listEditeurs.size(); i++) {
                                        Editeur editeur = listEditeurs.get(i);
                                        System.out.println((i+1) + ". " + editeur.getNomEdit());
                                        }
                                        System.out.print("Choisissez un editeur (numéro) : ");
                                        int choixEdit = Integer.parseInt(scanner.nextLine()) - 1;
                                    if (choixEdit >= 0 && choixEdit < listEditeurs.size()) {
                                        editeursLivre.add(listEditeurs.get(choixEdit));
                                        System.out.println("Editeur ajouté au livre.");
                                    } else {
                                        System.out.println("Choix invalide.");
                                    }
                                }
                            } catch (SQLException e1) {
                                System.out.println("Erreur lors de la recherche de l'editeur : " + e1.getMessage());
                            } catch (NumberFormatException e2) {
                            System.out.println("Veuillez entrer un nombre valide.");
                            }
                        }// fin while editeurs
                        }// fin if reponse editeur
                    livre.setAuteurs(auteursLivre);
                    livre.setClassifications(classificationLivre);
                    livre.setEditeurs(editeursLivre);
                    break;    
                
                case "2":
                    menuRechercherLivre();
                    
                    break;
                case "3":
                    menuRechercherLivre();
                    System.out.println("Etes vous sûr de vouloir supprimer le livre"+"\n"+livreSelectionner+" ? (O/n)");
                    String reponseSupp = scanner.nextLine().toLowerCase();
                    if (!reponseSupp.equals("o") && !reponseSupp.equals("oui")) {
                        System.out.println("Suppression annulée.");
                        break;
                    }
                    else{
                        try{
                        adminConnexion.supprimerLivre(livreSelectionner);
                        }
                        catch(SQLException e){
                            System.out.println("Erreur lors de la suppression du livre : " + e.getMessage());
                            break;
                        }
                        System.out.println("Livre supprimé avec succès !");
                    }
            }
        }
    }
    public void menuStatsDeVentes() {
        boolean continuer = true;
        while(continuer) {
            System.out.println("---- STATISTIQUES DE VENTES ----");
            System.out.println("|                             |");
            System.out.println("| > Voir les ventes du mois   |");
            System.out.println("| > Voir les ventes de l'année|");
            System.out.println("| > Retour au menu principal  |");
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
                //menuCatalogue();
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
                        System.out.println("Est ce que c'est pour commander ? (O/n)");
                        String reponse = scanner.nextLine().toLowerCase();
                        if(reponse.equals("o") || reponse.equals("oui")){
                            afficherLivre(livres, true);
                        }
                        else{
                            afficherLivre(livres, false);
                        }
                        
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
                        System.out.println("Est ce que c'est pour commander ? (O/n)");
                        String reponse = scanner.nextLine().toLowerCase();
                        if(reponse.equals("o") || reponse.equals("oui")){
                            afficherLivre(livres, true);
                        }
                        else{
                            afficherLivre(livres, false);
                        }
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
                        System.out.println("Est ce que c'est pour commander ? (O/n)");
                        String reponse = scanner.nextLine().toLowerCase();
                        if(reponse.equals("o") || reponse.equals("oui")){
                            afficherLivre(livres, true);
                        }
                        else{
                            afficherLivre(livres, false);
                        }
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
        afficherLivre(clientConnexion.onVousRecommande((Client) utilisateurConnecter), true);
        }
        catch(SQLException e ){
            System.out.println("Erreur lors de la récupération des livres a recommandé : "+e.getMessage());
        }
        return;
    }

    public void menuMesCommandes(){
        // TO DO
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
        System.out.println("Veuillez choisir une informations a modifier (1-4) : ");
        System.out.println("Pour retourner au menu principal : q ");
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

    public Magasin menuChoisirMagasin() {
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

    public void afficherLivre(Map<Livre, Boolean> livres, boolean pourCommander) {
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
                String reponse;
                if (pourCommander) {
                    System.out.println("Voulez vous commander un de ces livres ? (O/n)");
                    System.out.println("Pour changer de page : < | >");
                    reponse = scanner.nextLine().toLowerCase();
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
                } else {
                    System.out.println("Voulez vous selectionner un de ces livres ? (O/n)");
                    System.out.println("Voulez vous commander un de ces livres ? (O/n)");
                    System.out.println("Pour changer de page : < | >");
                    reponse = scanner.nextLine().toLowerCase();
                    if (reponse.equals("o") || reponse.equals("oui")) {
                        System.out.print("Entrez le numéro du livre à sélectionner (1-" + nbObjetParPage + ") : ");
                        int choixLivre = Integer.parseInt(scanner.nextLine());
                        Livre livreChoisi = listeLivres.get(choixLivre-1+(pageCourante - 1) * nbObjetParPage);
                        livreSelectionner = livreChoisi;
                    }
                }
                if (reponse.equals("<")) {
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
        try{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT isbn, titre, nomauteur FROM LIVRE NATURAL JOIN ECRIRE NATURAL JOIN AUTEUR WHERE titre LIKE '%Ron Mueck%' OR nomauteur LIKE '%Ron Mueck%';");
        // Si connexion BD réussie
        System.out.println("Livres disponibles :");
            while(r.next()){
                String isbn = r.getString("isbn");
                String titre = r.getString("titre");
                String auteur = r.getString("nomauteur");
                System.out.println("ISBN: " + isbn + ", Titre: " + titre + ", Auteur: " + auteur);
            }
        }
        catch(SQLException e){
            System.out.println("Erreur lors de la récupération des livres : " + e.getMessage());
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
