package fr.saejava.control;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.LivreBD;
import fr.saejava.model.UtilisateurBD;
import fr.saejava.model.VendeurBD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
    private TextField textFieldGererStockDatePubli; //setText DatePubli

    @FXML
    private TextField textFieldGererStockISBN; //setText ISBN

    @FXML
    private TextField textFieldGererStockNBPages; //setText NBPages

    @FXML
    private TextField textFieldGererStockPrix; //setText Prix

    @FXML
    private TextField textFieldGererStockQuantite; //setText Quantité

    @FXML
    private TextField textFieldGererStockTitre; //setText Titre

    @FXML
    private TextField textFieldGererStockTitreLivre;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private VendeurBD vendeurBD;
    private LivreBD livreBD;

        public ControllerGererStock(ApplicationLibrairie app, VendeurBD vendeurBD, UtilisateurBD utilisateurBD, LivreBD livreBD) throws SQLException{
            this.app = app;
            //this.adminBD=new AdminBD(connexion);
            this.utilisateurBD = utilisateurBD;
            this.vendeurBD = vendeurBD;
            this.livreBD = livreBD;
        }

        @FXML
        public void initialize(){
            this.labelGererStockISBN.setText("ISBN : ...");
            this.labelGererStockTitre.setText("Titre : ...");
            this.labelGererStockNBPages.setText("Nombre de Pages : ...");
            this.labelGererStockDatePubli.setText("Date de Publication : ...");
            this.labelGererStockPrix.setText("Prix : ...");
            this.labelGererStockQuantite.setText("Quantité : ...");

            this.textFieldGererStockISBN.setDisable(true);
            this.textFieldGererStockTitre.setDisable(true);
            this.textFieldGererStockNBPages.setDisable(true);
            this.textFieldGererStockDatePubli.setDisable(true);
            this.textFieldGererStockPrix.setDisable(true);
            this.textFieldGererStockQuantite.setDisable(true);
        }

        @FXML
        public String titreLivre(){
            return this.textFieldGererStockTitreLivre.getText();
        }

        @FXML
        public void nouveauLivre(MouseEvent event) {
            this.buttonGererStockNouveauLivre = (Button) (event.getSource());

            this.textFieldGererStockISBN.setDisable(false);
            this.textFieldGererStockTitre.setDisable(false);
            this.textFieldGererStockNBPages.setDisable(false);
            this.textFieldGererStockDatePubli.setDisable(false);
            this.textFieldGererStockPrix.setDisable(false);
            this.textFieldGererStockQuantite.setDisable(false);

            this.textFieldGererStockISBN.setText(null);
            this.textFieldGererStockTitre.setText(null);
            this.textFieldGererStockNBPages.setText(null);
            this.textFieldGererStockDatePubli.setText(null);
            this.textFieldGererStockPrix.setText(null);
            this.textFieldGererStockQuantite.setText(null);

            this.textFieldGererStockISBN.setPromptText("ISBN");
            this.textFieldGererStockTitre.setPromptText("Titre");
            this.textFieldGererStockNBPages.setPromptText("NBPages");
            this.textFieldGererStockDatePubli.setPromptText("DatePubli");
            this.textFieldGererStockPrix.setPromptText("Prix");
            this.textFieldGererStockQuantite.setPromptText("Quantité");
        }

        @FXML
        public void rechercheTitre(KeyEvent event){
            if(event.getCode() == KeyCode.ENTER){
                app.afficherRechercheLivreView(app.getStage(), textFieldGererStockTitreLivre.getText());
            }
        }
        
        // @FXML
        // void startRecherche(KeyEvent event) {
        //     if(event.getCode() == KeyCode.ENTER){
        //         app.afficherRechercheLivreView(app.getStage(), clientSearchBar.getText());
        //     }
        // }
}


