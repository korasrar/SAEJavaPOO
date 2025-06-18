package fr.saejava;

import fr.saejava.control.ControllerConnexion;
import fr.saejava.control.ControllerInscription;
import fr.saejava.control.ControllerVendeurAcceuil;
import fr.saejava.control.ControllerAdmin;
import fr.saejava.control.ControllerAdminMenueVendeur;
import fr.saejava.control.ControllerAjouteVendeur;
import fr.saejava.control.ControllerAjouteMagasin;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import fr.saejava.model.*;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;

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
     * Permet d'afficher une alerte d'information
     * @param message le message d'information à afficher
     */
    public void afficherWarning(String message) {
        Alert warning = new Alert(AlertType.WARNING);
        warning.setTitle("Attention !");
        warning.setHeaderText(message);
        warning.showAndWait();
    }

    public void afficherInformation(String message) {
        Alert warning = new Alert(AlertType.INFORMATION);
        warning.setTitle("Information");
        warning.setHeaderText(message);
        warning.showAndWait();
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


    public void afficherVendeurMainView(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/VendeurAccueilCenter.fxml"));
        try {
            ControllerVendeurAcceuil controllerVendeurAccueil = new ControllerVendeurAcceuil(this, vendeurConnexion, utilisateurConnexion);
            System.out.println("Affichage de la vue VendeurAccueilCenter");
            loader.setController(controllerVendeurAccueil);
            System.out.println("Affichage de la vue VendeurAccueilCenter2");
            this.root = loader.load();
            System.out.println("Affichage de la vue VendeurAccueilCenter3");
            this.scene = new Scene(this.root);
            System.out.println("Affichage de la vue VendeurAccueilCenter4");
            stage.setScene(scene);
            System.out.println("Affichage de la vue VendeurAccueilCenter5");
            stage.setTitle("Menu Vendeur");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue main du Vendeur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void afficherAdminMainView(Stage stage){
        loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
        try {
            ControllerAdmin controllerAdmin = new ControllerAdmin(this, utilisateurConnexion);
            System.out.println("Affichage de la vue Admin1");
            loader.setController(controllerAdmin);
            System.out.println("Affichage de la vue Admin2");
            this.root = loader.load();
            System.out.println("Affichage de la vue Admin3");
            this.scene = new Scene(this.root);
            System.out.println("Affichage de la vue Admin4");
            stage.setScene(scene);
            System.out.println("Affichage de la vue Admin5");
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
            stage.setTitle("Admin menue vendeur");
            stage.show();

        }
        catch(Exception e){
            afficherErreur("Erreur lors du chargement de la vue admin menue vendeur : " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Load la page de connexion par défaut
    @Override
    public void start(Stage primaryStage) throws Exception {
        estConnecteBD= false;
        try {
            connexion = new ConnexionMySQL();
            connexion.connecter("servinfo-maria", "DBpelletant", "pelletant", "pelletant");

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