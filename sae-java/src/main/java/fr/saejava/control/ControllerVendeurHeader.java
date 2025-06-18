package fr.saejava.control;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.UtilisateurBD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ControllerVendeurHeader {

    @FXML
    private Button buttonGererStock;

    @FXML
    private Button buttonTransfererLivre;

    @FXML
    private Button buttonVerifierDispo;

    private ApplicationLibrairie app;

    public ControllerVendeurHeader(ApplicationLibrairie app){
        this.app = app;
    }

    @FXML
    void gererStock(MouseEvent event) {
        Button button = (Button) (event.getSource());
        if (button.getText().contains("Gérer Stocks")){
            System.out.println("trois");
        }
    }

    @FXML
    void tranfererLivre(MouseEvent event) {
        Button button = (Button) (event.getSource());
        if (button.getText().contains("Transférer Livre")){
            System.out.println("un");
            app.afficherVendeurTransfererLivre(app.getStage());
        }
    }

    @FXML
    void verifierDispo(MouseEvent event) {
        Button button = (Button) (event.getSource());
        if (button.getText().contains("Vérifier les Dispos")){
            System.out.println("deux");
        }
    }

}
