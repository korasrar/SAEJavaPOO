package fr.saejava.control;

import java.sql.SQLException;
import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.AdminBD;
import fr.saejava.model.MagasinBD;
import fr.saejava.model.Magasin;
import fr.saejava.model.ConnexionMySQL;
import fr.saejava.model.Magasin;
import fr.saejava.model.UtilisateurBD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ControllerAjouteVendeur {

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private MagasinBD magasinBD;
    private AdminBD adminBD;


    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonRetour;

    @FXML
    private ComboBox<Magasin> comboMagasin;

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private TextField textFieldPrenom;

    @FXML
    private TextField textFieldUserName;
    

    public ControllerAjouteVendeur(ApplicationLibrairie app,UtilisateurBD utilisateurBD){
        this.app=app;
        this.utilisateurBD=utilisateurBD;
    }

    public ControllerAjouteVendeur(ApplicationLibrairie app,UtilisateurBD utilisateurBD,AdminBD adminBD,MagasinBD magasinBD){
        this.app=app;
        this.utilisateurBD=utilisateurBD;
        this.adminBD=adminBD;
        this.magasinBD=magasinBD;
    }

    public void chargeComboMagasin(){
        try{
            this.comboMagasin.getItems().addAll(magasinBD.chargerMagasin());    
        }
        catch(Exception e){
            this.app.afficherErreur("Erreur chargement magasin : " + e.getMessage());
        }
    }

    @FXML
    void confirmer(MouseEvent event) {
        try{
            String nom=this.textFieldNom.getText();
            String prenom=this.textFieldPrenom.getText();
            String userName=this.textFieldUserName.getText();
            String motDePasse=this.textFieldPassword.getText();
            Magasin magasin=this.comboMagasin.getSelectionModel().getSelectedItem();
            if(nom.isEmpty() || prenom.isEmpty() || motDePasse.isEmpty() || magasin==null){
                app.afficherErreur("Veuillez remplir tous les champs.");
            }
            else{
                int idVendeur=this.utilisateurBD.getDernierID()+1;
                this.adminBD.creeCompteVendeur(idVendeur, nom, prenom, userName, motDePasse, magasin);
                app.afficherInformation("Ajout du vendeur réussie !");
            }
        }
        catch(Exception e){
            app.afficherErreur("Erreur lors de l'ajout du vendeur : " + e.getMessage());
        }
    }

    @FXML
    void retour(MouseEvent event) {
        this.app.afficherAdminMainView(this.app.getStage());
    }

}
