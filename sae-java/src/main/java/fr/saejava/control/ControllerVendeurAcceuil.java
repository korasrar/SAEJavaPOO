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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ControllerVendeurAcceuil {

    @FXML
    private Label labelAccueilVendeuIDVendeur;

    @FXML
    private Label labelAccueilVendeurBienvunueNomVendeur;

    @FXML
    private Label labelAccueilVendeurNomMagasinVendeur;

    @FXML
    private Label labelAccueilVendeurNomVendeur;

    @FXML
    private Label labelAccueilVendeurPrenomVendeur;

    @FXML
    private ListView<Livre> listViewMeilleurVentes;

    private ApplicationLibrairie app;
    private ConnexionMySQL connexion;
    private UtilisateurBD utilisateurBD;
    private VendeurBD vendeurBD;
    private AdminBD adminBD;

    public ControllerVendeurAcceuil(){
        // Default controller
    }

    public ControllerVendeurAcceuil(ApplicationLibrairie app, ConnexionMySQL connexion){
        this.app=app;
        this.connexion=connexion;
        this.adminBD=new AdminBD(connexion);
        this.utilisateurBD=new UtilisateurBD(connexion);
        this.vendeurBD= new VendeurBD(connexion);
        
    }

    @FXML
    public void initialize() {
        try{
            listViewMeilleurVentes.setItems(FXCollections.observableArrayList(vendeurBD.getMeilleurVente(((Vendeur)utilisateurBD.getUtilisateurConnecter()).getMagasin())));
        }
        catch(SQLException e){
            app.afficherErreur("Erreur lors de la récupération des meilleurs vente");
        }
    }

    /**
     * Sélectionner un livre parmis les meilleur ventes
     * @param event Quand un clique de souris est fait
     */
    @FXML
    void selectionerLivre(MouseEvent event) {

    }

}

