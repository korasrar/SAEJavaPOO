package fr.saejava.control;

import java.sql.SQLException;
import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.AdminBD;
import fr.saejava.model.ConnexionMySQL;
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
    private AdminBD adminBD;

    public ControllerAjouteVendeur(ApplicationLibrairie app,UtilisateurBD utilisateurBD){
        this.app=app;
        this.utilisateurBD=utilisateurBD;
    }

    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonRetour;

    @FXML
    private ComboBox<?> comboMagasin;

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private TextField textFieldPrenom;

    @FXML
    void confirmer(MouseEvent event) {
        System.out.println("confimer");
    }

    @FXML
    void retour(MouseEvent event) {
        System.out.println("retour");
    }

}
