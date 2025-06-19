package fr.saejava.control;

import java.sql.SQLException;
import java.util.List;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerRechercherClient {

    @FXML
    private Button buttonRetour;

    @FXML
    private Button buttonSelectionner;

    @FXML
    private ListView<Client> listViewResultatRecherche;

    private ApplicationLibrairie app;
    private ClientBD clientBD;
    private String usernameARechercher;
    private List<Client> listeClientResultat;
    private Stage stage;

    public ControllerRechercherClient() {
        // Default Constructor
    }

    public ControllerRechercherClient(ApplicationLibrairie app, ClientBD clientBD, Stage stage, String username) {
        this.app = app;
        this.clientBD = clientBD;
        this.usernameARechercher = username;
        this.stage = stage;
    }

    @FXML
    void initialize() {
        buttonSelectionner.setDisable(true);
        try {
            listeClientResultat = clientBD.rechercherCLient(usernameARechercher);
            listViewResultatRecherche.getItems().addAll(listeClientResultat);
        } 
        catch (SQLException e) {
            app.afficherErreur("Erreur lors de la recherche du client : " + e.getMessage());
        }
    }


    @FXML
    void retour(MouseEvent event) {
        stage.close();
    }

    @FXML
    void selectionner(MouseEvent event) {
        Client clientSelectionne = listViewResultatRecherche.getSelectionModel().getSelectedItem();
        clientBD.setClientSelectionner(clientSelectionne);
        app.afficherVendeurMainView(stage);
    }
}

