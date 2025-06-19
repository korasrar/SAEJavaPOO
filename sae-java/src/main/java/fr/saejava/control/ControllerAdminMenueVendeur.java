package fr.saejava.control;

import java.sql.SQLException;
import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.AdminBD;
import fr.saejava.model.ConnexionMySQL;
import fr.saejava.model.Magasin;
import fr.saejava.model.MagasinBD;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ControllerAdminMenueVendeur {

    
    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonRetour;

    @FXML
    private ComboBox<Magasin> comboMagasin;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private MagasinBD magasinBD;

    public ControllerAdminMenueVendeur(ApplicationLibrairie app,UtilisateurBD utilisateurBD,MagasinBD magasinBD){
        this.app=app;
        this.utilisateurBD=utilisateurBD;
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
        if(this.comboMagasin.getSelectionModel().getSelectedItem()==null){
            app.afficherErreur("Veuillez choisir un magasin.");
        }
        else{
            app.afficherVendeurMainView(this.app.getStage());
        }
    }

    @FXML
    void retour(MouseEvent event) {
        this.app.afficherAdminMainView(this.app.getStage());
    }

}

