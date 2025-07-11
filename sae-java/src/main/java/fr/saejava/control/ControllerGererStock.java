package fr.saejava.control;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.Livre;
import fr.saejava.model.LivreBD;
import fr.saejava.model.Magasin;
import fr.saejava.model.MagasinBD;
import fr.saejava.model.UtilisateurBD;
import fr.saejava.model.VendeurBD;
import fr.saejava.model.VendeurSansMagasinException;
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

    @FXML
    private Button buttonGererStockSupprimer;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private VendeurBD vendeurBD;
    private MagasinBD magasinBD;

        public ControllerGererStock(ApplicationLibrairie app, VendeurBD vendeurBD, UtilisateurBD utilisateurBD, MagasinBD magasinBD) throws SQLException{
            this.app = app;
            //this.adminBD=new AdminBD(connexion);
            this.utilisateurBD = utilisateurBD;
            this.vendeurBD = vendeurBD;
            this.magasinBD = magasinBD;
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

            this.buttonGererStockSupprimer.setDisable(true);
            //this.buttonGererStockEnregistrer.setDisable(true);
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
            //    app.afficherRechercheLivreView(app.getStage(), textFieldGererStockTitreLivre.getText());
            }
        }

        @FXML
        public void setDatePubli(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER){
                System.out.println(this.textFieldGererStockDatePubli.getText());
            }
        }

        @FXML
        public void setISBN(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER){
                System.out.println(this.textFieldGererStockISBN.getText());
            }
        }

        @FXML
        public void setNBPages(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER){
                System.out.println(this.textFieldGererStockNBPages.getText());
            }
        }

        @FXML
        public void setPrix(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER){
                System.out.println(this.textFieldGererStockPrix.getText());
            }
        }

        @FXML
        public void setQuantite(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER){
                System.out.println(this.textFieldGererStockQuantite.getText());
            }
        }

        @FXML
        public void setTitre(KeyEvent event) {
            if(event.getCode() == KeyCode.ENTER){
                System.out.println(this.textFieldGererStockTitre.getText());
            }
        }

        public @FXML
            void enregistrer(MouseEvent event) throws NumberFormatException, SQLException, VendeurSansMagasinException {
                this.buttonGererStockEnregistrer = (Button) (event.getSource());
                System.out.println("1");
                if (this.textFieldGererStockDatePubli != null || this.textFieldGererStockISBN != null || this.textFieldGererStockNBPages != null || this.textFieldGererStockPrix != null || this.textFieldGererStockQuantite != null || this.textFieldGererStockTitre != null){
                    //this.buttonGererStockEnregistrer.setDisable(false);
                    Livre livre = new Livre(this.textFieldGererStockISBN.getText(), this.textFieldGererStockTitre.getText(), Integer.parseInt(this.textFieldGererStockNBPages.getText()), this.textFieldGererStockDatePubli.getText(), Double.parseDouble(this.textFieldGererStockDatePubli.getText()));
                    System.out.println("2");

                    this.magasinBD.ajoutStock(this.vendeurBD.getMagasin(this.utilisateurBD.getUtilisateurConnecter().getId()), livre, Integer.parseInt(this.textFieldGererStockQuantite.getText()));
                    System.out.println("3");

                    System.out.println(livre);
                }
                else
                    System.out.println("Il manque des informations pour le livre !");
            }
            
            @FXML
            public void supprimer(MouseEvent event) {

            }
}       
