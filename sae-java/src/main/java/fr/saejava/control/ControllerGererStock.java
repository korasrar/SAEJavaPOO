package fr.saejava.control;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.UtilisateurBD;
import fr.saejava.model.VendeurBD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ControllerGererStock {

    @FXML
    private Button buttonGererStockEnregistrer;

    @FXML
    private Button buttonGererStockNouveauLivre;

    @FXML
    private Label labelGererStockDatePubli;

    @FXML
    private Label labelGererStockISBN;

    @FXML
    private Label labelGererStockNBPages;

    @FXML
    private Label labelGererStockPrix;

    @FXML
    private Label labelGererStockQuantite;

    @FXML
    private Label labelGererStockTitre;

    @FXML
    private TextField textFieldGererStockDatePubli;

    @FXML
    private TextField textFieldGererStockISBN;

    @FXML
    private TextField textFieldGererStockNBPages;

    @FXML
    private TextField textFieldGererStockPrix;

    @FXML
    private TextField textFieldGererStockQuantite;

    @FXML
    private TextField textFieldGererStockTitre;

    @FXML
    private TextField textFieldGererStockTitreLivre;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private VendeurBD vendeurBD;

        public ControllerGererStock(ApplicationLibrairie app, VendeurBD vendeurBD, UtilisateurBD utilisateurBD) throws SQLException{
            this.app=app;
            //this.adminBD=new AdminBD(connexion);
            this.utilisateurBD=utilisateurBD;
            this.vendeurBD=vendeurBD;
        }

        @FXML
        public void initialize() {
    }
}
