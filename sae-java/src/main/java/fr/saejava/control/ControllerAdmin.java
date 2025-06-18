package fr.saejava.control;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.AdminBD;
import fr.saejava.model.ConnexionMySQL;
import fr.saejava.model.Livre;
import fr.saejava.model.UtilisateurBD;
import fr.saejava.model.Vendeur;
import fr.saejava.model.VendeurBD;
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
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControllerAdmin {

    @FXML
    private Label adminUsername;

    @FXML
    private Button buttonAfficherChart;

    @FXML
    private Button buttonAjouteMagasin;

    @FXML
    private Button buttonAjouteVendeur;

    @FXML
    private Button buttonMenueVendeur;

    @FXML
    private BarChart<?, ?> chartAdmin;

    @FXML
    private ComboBox<?> comboAdmin2;

    @FXML
    private ComboBox<?> comboAdmin3;

    @FXML
    private ComboBox<?> comboAdminTypeChart;

    @FXML
    private ImageView iconUser;

    @FXML
    private ImageView logoHome;

    @FXML
    private TextField textFieldAdmin1;

    @FXML
    private TextField textFieldAdmin2;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private AdminBD adminBD;


    public ControllerAdmin(){
        // Default controller
    }

    public ControllerAdmin(ApplicationLibrairie app,UtilisateurBD utilisateurBD) {
        this.app=app;
        //this.adminBD=new AdminBD(connexion);
        this.utilisateurBD=utilisateurBD;
    }

    @FXML
    public void initialize() {
        
    }
    @FXML
    void afficheChart(MouseEvent event) {
        System.out.println("affiche chart");
    }

    @FXML
    void menueVendeur(MouseEvent event) {
        System.out.println("menue vendeur");
    }

    @FXML
    void pageAjouterMagasin(MouseEvent event) {
        this.app.afficherAjoutMagasinView(this.app.getStage());
    }

    @FXML
    void pageAjouterVendeur(MouseEvent event) {
        this.app.afficherAjoutVendeurView(this.app.getStage());
    }
   

}

