package fr.saejava.control;

import com.itextpdf.text.Image;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.UtilisateurBD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControllerVendeurHeader {

    @FXML
    private Button buttonGererStock;

    @FXML
    private Button buttonTransfererLivre;

    @FXML
    private Button buttonVerifierDispo;

    @FXML
    private ImageView imagesViewRetourAccueil;

    private ApplicationLibrairie app;

    public ControllerVendeurHeader(ApplicationLibrairie app){
        this.app = app;
    }

    @FXML
    public void gererStock(MouseEvent event) {
        Button button = (Button) (event.getSource());
        if (button.getText().contains("Gérer Stocks")){
            app.afficherVendeurGererStock(app.getStage());
        }
    }

    @FXML
    public void tranfererLivre(MouseEvent event) {
        Button button = (Button) (event.getSource());
        if (button.getText().contains("Transférer Livre")){
            app.afficherVendeurTransfererLivre(app.getStage());
        }
    }

    @FXML
    public void verifierDispo(MouseEvent event) {
        Button button = (Button) (event.getSource());
        if (button.getText().contains("Vérifier les Dispos")){
        }
    }

    @FXML
    public void retourAccueil(MouseEvent event) {
        ImageView image = (ImageView) (event.getSource());
        if (image == this.imagesViewRetourAccueil){
            app.afficherVendeurMainView(app.getStage());
        }      
    }
}
