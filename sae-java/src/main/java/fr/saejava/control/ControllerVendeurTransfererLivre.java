package fr.saejava.control;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.UtilisateurBD;
import fr.saejava.model.Vendeur;
import fr.saejava.model.VendeurBD;
import fr.saejava.model.VendeurSansMagasinException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ControllerVendeurTransfererLivre {

    @FXML
    private Button buttonTransfererLivreFinaliserTransfert;

    @FXML
    private Button buttonTransfererLivreSelectionnerLivre;

    @FXML
    private Button buttonTransfererLivreSelectionnerMagasin;

    @FXML
    private Label labelTransfererLivreNomMagasin;

    @FXML
    private Label labelTransfererLivreTitreLivre;

    @FXML
    private Label labelTransfrerLivreQuantite;

    @FXML
    private TextField textFieldTransfererLivreTitreLivre;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private VendeurBD vendeurBD;

    public ControllerVendeurTransfererLivre(ApplicationLibrairie app, VendeurBD vendeurBD, UtilisateurBD utilisateurBD) throws SQLException{
        this.app=app;
        //this.adminBD=new AdminBD(connexion);
        this.utilisateurBD=utilisateurBD;
        this.vendeurBD=vendeurBD;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    void startRecherche(KeyEvent event) {
        if(event.getCode()==KeyCode.ENTER){
            app.afficherRechercheLivreVendeurView(app.getStage(), textFieldTransfererLivreTitreLivre.getText());
        }
    }
}
