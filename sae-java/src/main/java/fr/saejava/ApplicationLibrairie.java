package fr.saejava;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import fr.saejava.control.*;
import fr.saejava.model.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ApplicationLibrairie extends javafx.application.Application {

    private FXMLLoader loader;
    private Pane root;
    private Scene scene;

    ConnexionMySQL connexion;
    Utilisateur utilisateurConnecter;
    MagasinBD magasinConnexion;
    UtilisateurBD utilisateurConnexion;
    VendeurBD vendeurConnexion;
    AdminBD adminConnexion;
    CommandeBD commandeConnexion;
    ClientBD clientConnexion;
    LivreBD livreConnexion;
    boolean estConnecteBD;
    boolean estConnecteUtil;
    Commande panier;
    Livre livreSelectionner;

    public Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

    /**
     * Permet d'afficher une alerte d'erreur
     * @param message le message d'erreur à afficher
     */
    public void afficherErreur(String message) {
        Alert errorBD = new Alert(AlertType.ERROR);
        errorBD.setTitle("Erreur BD");
        errorBD.setHeaderText(message);
        errorBD.showAndWait();
    }

    /**
     * Permet d'afficher une alerte warning
     * @param message le message de warning à afficher
     */
    public void afficherWarning(String message) {
        Alert warning = new Alert(AlertType.WARNING);
        warning.setTitle("Attention !");
        warning.setHeaderText(message);
        warning.showAndWait();
    }

    /**
     * Permet d'afficher une alerte d'information
     * @param message le message d'information à afficher
     */
    public void afficherInformation(String message) {
        Alert warning = new Alert(AlertType.INFORMATION);
        warning.setTitle("Information");
        warning.setHeaderText(message);
        warning.showAndWait();
    }

    public void afficherRechercheLivreView(Stage stage, String titreLivre){
        loader = new FXMLLoader(getClass().getResource("/view/RechercheLivreView.fxml"));
        try {
            Stage stageRechercheLivre = new Stage();
            stageRechercheLivre.initModality(Modality.APPLICATION_MODAL);
            stageRechercheLivre.initOwner(stage);
            ControllerRechercherLivre controllerRechercherLivre = new ControllerRechercherLivre(this, clientConnexion,livreConnexion,vendeurConnexion,utilisateurConnexion, titreLivre, stageRechercheLivre);
            loader.setController(controllerRechercherLivre);
            Pane paneRechercheLivre = loader.load();
            Scene sceneRechercheLivre = new Scene(paneRechercheLivre);
            stageRechercheLivre.setScene(sceneRechercheLivre);
            stageRechercheLivre.setTitle("Résultat recherche Livre");
            stageRechercheLivre.showAndWait();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue de recherche de livre : " + e.getMessage());
        }
    }

    public void afficherAjoutMagasinView(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/AjoutMagasinView.fxml"));
        try {
            ControllerAjouteMagasin controller = new ControllerAjouteMagasin(this, utilisateurConnexion,this.adminConnexion);
            loader.setController(controller);
            this.root = loader.load();
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Ajout d'un magasin");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue d'ajout de magasin : " + e.getMessage());
        }
    }

    public void afficherAjoutVendeurView(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/AjoutVendeurView.fxml"));
        try {
            ControllerAjouteVendeur controller = new ControllerAjouteVendeur(this, utilisateurConnexion,this.adminConnexion,this.magasinConnexion);
            loader.setController(controller);
            this.root = loader.load();
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Ajout d'un vendeur");
            controller.chargeComboMagasin();
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue d'ajout de vendeur : " + e.getMessage());
        }
    }

    public void afficherConnexion(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/ConnexionView.fxml"));
        try {
            ControllerConnexion controllerConnexion = new ControllerConnexion(this, utilisateurConnexion);
            loader.setController(controllerConnexion);
            this.root = loader.load();
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue de connexion : " + e.getMessage());
        }
    }



    public void afficherInscriptionView(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/InscriptionView.fxml"));
        try {
            ControllerInscription controllerInscription = new ControllerInscription(this, utilisateurConnexion, clientConnexion);
            loader.setController(controllerInscription);
            this.root = loader.load();
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Inscription");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue pour s'inscrire : " + e.getMessage());
        }
    }

    public void afficherPanierView(Stage stage) {
        try {
            Stage stagePanier = new Stage();
            stagePanier.initModality(Modality.APPLICATION_MODAL);
            stagePanier.initOwner(stage);
            FXMLLoader panierLoader = new FXMLLoader(getClass().getResource("/view/PanierView.fxml"));
            ControllerPanierView controllerPanier = new ControllerPanierView(this, clientConnexion, utilisateurConnexion, commandeConnexion, magasinConnexion);
            panierLoader.setController(controllerPanier);
            Pane panierPane = panierLoader.load();
            Scene panierScene = new Scene(panierPane);
            stagePanier.setScene(panierScene);
            stagePanier.setTitle("Mon Panier");
            stagePanier.showAndWait();
        }
        catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue du panier : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afficherVendeurTransfererLivre(Stage stage) { // A coder
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/VendeurTransfereLivreContainer.fxml"));
            BorderPane mainPane = mainLoader.load();
            
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/view/VendeurHeaderView.fxml"));
            ControllerVendeurHeader headerController = new ControllerVendeurHeader(this);
            headerLoader.setController(headerController);
            Pane headerPane = headerLoader.load();
            
            FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/view/VendeurTransfereLivre.fxml"));
            ControllerVendeurTransfererLivre centerController = new ControllerVendeurTransfererLivre(this, vendeurConnexion, utilisateurConnexion);
            centerLoader.setController(centerController);
            Pane centerPane = centerLoader.load();
            
            mainPane.setTop(headerPane);
            mainPane.setCenter(centerPane);
            
            this.root = mainPane;
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Menu Transférer Livre");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue transférer du Vendeur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afficherVendeurGererStock(Stage stage) { // A coder
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/VendeurGererStockContainer.fxml"));
            BorderPane mainPane = mainLoader.load();
            
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/view/VendeurHeaderView.fxml"));
            ControllerVendeurHeader headerController = new ControllerVendeurHeader(this);
            headerLoader.setController(headerController);
            Pane headerPane = headerLoader.load();
            
            FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/view/VendeurGererStock.fxml"));
            ControllerGererStock centerController = new ControllerGererStock(this, vendeurConnexion, utilisateurConnexion, magasinConnexion);
            centerLoader.setController(centerController);
            Pane centerPane = centerLoader.load();
            
            mainPane.setTop(headerPane);
            mainPane.setCenter(centerPane);
            
            this.root = mainPane;
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Menu Gérer Stock");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue gerer stock du Vendeur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    //public void afficherRechercheLivreVendeurView(Stage stage, String titreLivre){
    //        loader = new FXMLLoader(getClass().getResource("/view/RechercheLivreView.fxml"));
    //        try {
    //            Stage stageRechercheLivre = new Stage();
    //            stageRechercheLivre.initModality(Modality.APPLICATION_MODAL);
    //            stageRechercheLivre.initOwner(stage);
    //            ControllerRechercherLivreVendeur controllerRechercherLivre = new ControllerRechercherLivreVendeur(this, clientConnexion,livreConnexion,vendeurConnexion,utilisateurConnexion, titreLivre);
    //            loader.setController(controllerRechercherLivre);
    //            Pane paneRechercheLivre = loader.load();
    //            Scene sceneRechercheLivre = new Scene(paneRechercheLivre);
    //            stageRechercheLivre.setScene(sceneRechercheLivre);
    //            stageRechercheLivre.setTitle("Résultat recherche Livre");
    //            stageRechercheLivre.showAndWait();
    //        } catch (Exception e) {
    //            afficherErreur("Erreur lors du chargement de la vue de recherche de livre : " + e.getMessage());
    //        }
    //    }

    // --------------- Vue Utilisateur Main --------------- //

    public void afficherClientMainView(Stage stage) {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/ClientMainViewContainer.fxml"));
            BorderPane mainPane = mainLoader.load();
            
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/view/ClientHeaderView.fxml"));
            ControllerClientHeader headerController = new ControllerClientHeader(this, clientConnexion, utilisateurConnexion);
            headerLoader.setController(headerController);
            Pane headerPane = headerLoader.load();

            FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/view/ClientAccueilView.fxml"));
            ControllerClientAccueil centerController = new ControllerClientAccueil(this, utilisateurConnexion, clientConnexion, vendeurConnexion);
            centerLoader.setController(centerController);
            Pane centerPane = centerLoader.load();
            
            mainPane.setTop(headerPane);
            mainPane.setCenter(centerPane);
            
            this.root = mainPane;
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Menu Client");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue main du Client : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afficherVendeurMainView(Stage stage) {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/VendeurMainViewContainer.fxml"));
            BorderPane mainPane = mainLoader.load();
            
            FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/view/VendeurHeaderView.fxml"));
            ControllerVendeurHeader headerController = new ControllerVendeurHeader(this);
            headerLoader.setController(headerController);
            Pane headerPane = headerLoader.load();
            
            FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/view/VendeurAccueilCenter.fxml"));
            ControllerVendeurAcceuil centerController = new ControllerVendeurAcceuil(this, vendeurConnexion, utilisateurConnexion);
            centerLoader.setController(centerController);
            Pane centerPane = centerLoader.load();
            
            mainPane.setTop(headerPane);
            mainPane.setCenter(centerPane);
            this.root = mainPane;
            this.scene = new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Menu Vendeur");
            stage.show();
            
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue main du Vendeur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afficherAdminMenueVendeur(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/adminMenueVendeur.fxml"));
        try{
            ControllerAdminMenueVendeur controllerAdminMenueVendeur=new ControllerAdminMenueVendeur(this, this.utilisateurConnexion, this.magasinConnexion);
            loader.setController(controllerAdminMenueVendeur);
            this.root=loader.load();
            controllerAdminMenueVendeur.chargeComboMagasin();
            this.scene=new Scene(this.root);
            stage.setScene(scene);
            stage.setTitle("Admin Menue Vendeur");
            stage.show();

        }
        catch(Exception e){
            afficherErreur("Erreur lors du chargement de la vue admin menue vendeur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afficherAdminMainView(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
        try {
            ControllerAdmin controllerAdmin = new ControllerAdmin(this, this.utilisateurConnexion,this.magasinConnexion,this.adminConnexion);
            System.out.println("Affichage de la vue Admin1");
            loader.setController(controllerAdmin);
            System.out.println("Affichage de la vue Admin2");
            this.root = loader.load();
            System.out.println("Affichage de la vue Admin3");
            this.scene = new Scene(this.root);
            System.out.println("Affichage de la vue Admin4");
            stage.setScene(scene);
            System.out.println("Affichage de la vue Admin5");
            controllerAdmin.chargeComboMagasin();
            controllerAdmin.initComboTypeChart();
            stage.setTitle("Menu Vendeur");
            stage.show();
            
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue main du Vendeur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        estConnecteBD= false;
        try {
            connexion = new ConnexionMySQL();
            connexion.connecter("", "", "", "");

            // Partage de la connexion avec les autres classes

            utilisateurConnexion = new UtilisateurBD(connexion);
            vendeurConnexion = new VendeurBD(connexion);
            adminConnexion = new AdminBD(connexion);
            commandeConnexion = new CommandeBD(connexion);
            clientConnexion = new ClientBD(connexion);
            livreConnexion = new LivreBD(connexion);
            magasinConnexion = new MagasinBD(connexion);

            estConnecteBD = true;
        } catch (SQLTimeoutException e) {
            afficherErreur("La connexion à la base de données a expiré.");
        } catch (SQLException e) {
            afficherErreur("Impossible de se connecter à la base de données : \n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            afficherErreur("Le driver de la base de données n'a pas été trouvé.");
        } catch (Exception e) {
            afficherErreur("Erreur inconnue : " + e.getMessage());
        }
        if(estConnecteBD) {
            loader = new FXMLLoader(getClass().getResource("/view/ConnexionView.fxml"));
            ControllerConnexion controllerConnexion = new ControllerConnexion(this, utilisateurConnexion);
            loader.setController(controllerConnexion);
            this.root = loader.load();
            this.scene = new Scene(this.root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("SAE Java Application");
            primaryStage.show();
        }
    }

    public void exit(){
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}