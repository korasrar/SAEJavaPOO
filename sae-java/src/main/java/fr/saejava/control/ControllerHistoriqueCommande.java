package fr.saejava.control;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerHistoriqueCommande {

    @FXML
    private Button buttonEditerFacture;

    @FXML
    private Button buttonRetour;

    @FXML
    private ListView<Commande> listViewHistoriqueCommande;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private CommandeBD commandeBD;
    private Stage stage;
    @FXML
    public void initialize() {
        try{
        listViewHistoriqueCommande.setItems(FXCollections.observableList(commandeBD.getCommandes((Client) utilisateurBD.getUtilisateurConnecter())));
        } catch (Exception e) {
            app.afficherErreur("Erreur lors de la récupération des commandes : " + e.getMessage());
        }
    }

    public ControllerHistoriqueCommande() {
        // Default Constructor
    }

    public ControllerHistoriqueCommande(ApplicationLibrairie app, UtilisateurBD utilisateurBD,CommandeBD commandeBD, Stage stage) {
        this.app = app;
        this.utilisateurBD = utilisateurBD;
        this.stage = stage;
        this.commandeBD = commandeBD;
    }

    @FXML
    void editerFacture(MouseEvent event) {
        ((Client)utilisateurBD.getUtilisateurConnecter()).editerFacture(listViewHistoriqueCommande.getSelectionModel().getSelectedItem());
        app.afficherInformation("Facture éditée avec succès \n Vous pouvez la retrouver dans le dossier facture/");
    }

    @FXML
    void retour(MouseEvent event) {
        stage.close();
    }

}

