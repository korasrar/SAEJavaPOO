package fr.saejava;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    public void afficherAjoutMagasinView(){
        loader = new FXMLLoader(getClass().getResource("/view/AjoutMagasinView.fxml"));
        try {
            this.root = loader.load();
            this.scene = new Scene(this.root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajout d'un magasin");
            stage.show();
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de la vue d'ajout de magasin : " + e.getMessage());
        }
    }


    // Load la page de connexion par défaut
    @Override
    public void start(Stage primaryStage) throws Exception {
        estConnecteBD= false;
        try {
            connexion = new ConnexionMySQL();
            connexion.connecter("", "", "", "");
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
            this.root = loader.load();
            this.scene = new Scene(this.root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("SAE Java Application");
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
