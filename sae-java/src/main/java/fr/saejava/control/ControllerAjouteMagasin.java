package fr.saejava.control;

import java.sql.SQLException;
import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.AdminBD;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ControllerAjouteMagasin {

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private AdminBD adminBD;

    public ControllerAjouteMagasin(ApplicationLibrairie app,UtilisateurBD utilisateurBD){
        this.app=app;
        this.utilisateurBD=utilisateurBD;
    }

    public ControllerAjouteMagasin(ApplicationLibrairie app,UtilisateurBD utilisateurBD,AdminBD adminBD){
        this.app=app;
        this.utilisateurBD=utilisateurBD;
        this.adminBD=adminBD;
    }

    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonRetour;

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldVIlle;

    @FXML
    void confirmer(MouseEvent event) {
        try{
            String nom=this.textFieldNom.getText();
            String ville=this.textFieldVIlle.getText();
            if(nom.isEmpty() || ville.isEmpty()){
                app.afficherErreur("Veuillez remplir tous les champs.");
            }
            else{
                int idMagasin =this.adminBD.getDernierIDMagasin()+1;
                adminBD.ajouterMagasin(new Magasin(idMagasin, nom, ville));
                app.afficherInformation("Ajout du magasin réussie !");
            }
        }
        catch (Exception e) {
            app.afficherErreur("Erreur lors de l'ajout du magasin : " + e.getMessage());
        }
    }

    @FXML
    void retour(MouseEvent event) {
        this.app.afficherAdminMainView(this.app.getStage());
    }

}
