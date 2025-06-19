package fr.saejava.control;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.AdminBD;
import fr.saejava.model.ConnexionMySQL;
import fr.saejava.model.Livre;
import fr.saejava.model.Magasin;
import fr.saejava.model.MagasinBD;
import fr.saejava.model.UtilisateurBD;
import fr.saejava.model.Vendeur;
import fr.saejava.model.VendeurBD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
    private BarChart<String, Integer> chartAdmin;

    @FXML
    private ComboBox<Magasin> comboAdmin2;

    @FXML
    private ComboBox<?> comboAdmin3;

    @FXML
    private ComboBox<String> comboAdminTypeChart;

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
    private MagasinBD magasinBD;


    public ControllerAdmin(){
        // Default controller
    }

    public ControllerAdmin(ApplicationLibrairie app,UtilisateurBD utilisateurBD,MagasinBD magasinBD,AdminBD adminBD) {
        this.app=app;
        this.adminBD=adminBD;
        this.utilisateurBD=utilisateurBD;
        this.magasinBD=magasinBD;
    }

    public void initComboTypeChart(){
        ArrayList<String> lTypeChart=new ArrayList<>();
        lTypeChart.add("Chiffre d'affaire");
        lTypeChart.add("Nb livre vendue");
        this.comboAdminTypeChart.getItems().addAll(lTypeChart);
        this.comboAdmin2.setVisible(false);
        this.comboAdmin3.setVisible(false);
    }

    public void chargeComboMagasin(){
        try{
            this.comboAdmin2.getItems().addAll(magasinBD.chargerMagasin());    
        }
        catch(Exception e){
            this.app.afficherErreur("Erreur chargement magasin : " + e.getMessage());
        }
    }

    @FXML
    void choisiChart(ActionEvent event) {
        String typeChart=this.comboAdminTypeChart.getSelectionModel().getSelectedItem();
        if (typeChart.equals("Chiffre d'affaire") || typeChart.equals("Nb livre vendue") ){
            this.comboAdmin2.setVisible(true);
        }
    }

    @FXML
    public void initialize() {
        
    }

    @FXML
    void afficheChart(MouseEvent event) {
        try{
            if(this.comboAdminTypeChart.getSelectionModel().getSelectedItem()==null){
                app.afficherErreur("Veuillez choisir un type de graphe.");
                
            }
            else if(this.comboAdminTypeChart.getSelectionModel().getSelectedItem().equals("Nb livre vendue")){
                HashMap<Integer,Integer> valChartMagasin=new HashMap<>();
                HashMap<Integer,Integer> valChart=new HashMap<>();
                if( this.comboAdmin2.getSelectionModel().getSelectedItem()==null){
                    app.afficherErreur("Veuillez choisir un magasin."); 
                }
                else{
                    valChartMagasin=this.adminBD.RequeteNbLivreVendu(this.comboAdmin2.getSelectionModel().getSelectedItem());
                    valChart=this.adminBD.RequeteNbLivreVendu();
                    
                    XYChart.Series<String,Integer> serie1 = new XYChart.Series<>();
                    for(Integer annee:valChart.keySet()){
                        serie1.getData().add(new XYChart.Data<>(annee.toString(),valChart.get(annee)));
                    }

                    XYChart.Series<String,Integer> serie2 = new XYChart.Series<>();
                    for(Integer annee:valChartMagasin.keySet()){
                        serie2.getData().add(new XYChart.Data<>(annee.toString(),valChartMagasin.get(annee)));
                    }
                    serie1.setName("Totale");
                    serie2.setName(this.comboAdmin2.getSelectionModel().getSelectedItem().toString());
                    this.chartAdmin.getData().clear();
                    this.chartAdmin.getData().add(serie1);
                    this.chartAdmin.getData().add(serie2);
                }
            }
            else {
                HashMap<Integer,Double> valChartMagasin=new HashMap<>();
                HashMap<Integer,Double> valChart=new HashMap<>();
                if(this.comboAdmin2.getSelectionModel().getSelectedItem()==null){
                    app.afficherErreur("Veuillez choisir un magasin.");
                }
                else{
                    valChartMagasin=this.adminBD.RequeteChiffreAffaire(this.comboAdmin2.getSelectionModel().getSelectedItem());
                    valChart=this.adminBD.RequeteChiffreAffaire();

                    XYChart.Series<String,Integer> serie1 = new XYChart.Series<>();
                    for(Integer annee:valChart.keySet()){
                        serie1.getData().add(new XYChart.Data<>(annee.toString(),valChart.get(annee).intValue()));
                    }

                    XYChart.Series<String,Integer> serie2 = new XYChart.Series<>();
                    for(Integer annee:valChartMagasin.keySet()){
                        serie2.getData().add(new XYChart.Data<>(annee.toString(),valChartMagasin.get(annee).intValue()));
                    }
                    serie1.setName("Totale");
                    serie2.setName(this.comboAdmin2.getSelectionModel().getSelectedItem().toString());
                    this.chartAdmin.getData().clear();
                    this.chartAdmin.getData().add(serie1);
                    this.chartAdmin.getData().add(serie2);
                
                }
            
            }
        }
        catch(Exception e){
            this.app.afficherErreur("Erreur requete graph "+this.comboAdminTypeChart.getSelectionModel().getSelectedItem()+": " + e.getMessage());
        }
    }

    @FXML
    void menueVendeur(MouseEvent event) {
        this.app.afficherAdminMenueVendeur(this.app.getStage());
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

