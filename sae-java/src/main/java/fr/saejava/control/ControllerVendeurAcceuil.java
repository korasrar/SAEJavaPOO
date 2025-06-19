package fr.saejava.control;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.AdminBD;
import fr.saejava.model.ConnexionMySQL;
import fr.saejava.model.Livre;
import fr.saejava.model.UtilisateurBD;
import fr.saejava.model.Vendeur;
import fr.saejava.model.VendeurBD;
import fr.saejava.model.VendeurSansMagasinException;
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
    private UtilisateurBD utilisateurBD;
    private VendeurBD vendeurBD;
    private AdminBD adminBD;

    public ControllerVendeurAcceuil(){
        // Default controller
    }

    public ControllerVendeurAcceuil(ApplicationLibrairie app, VendeurBD vendeurBD, UtilisateurBD utilisateurBD) {
        this.app=app;
        //this.adminBD=new AdminBD(connexion);
        this.utilisateurBD=utilisateurBD;
        this.vendeurBD=vendeurBD;
        
    }

    @FXML
    public void initialize() throws VendeurSansMagasinException {
        try{
            this.listViewMeilleurVentes.setItems(FXCollections.observableArrayList(vendeurBD.getMeilleurVente(((Vendeur)utilisateurBD.getUtilisateurConnecter()).getMagasin())));
            this.labelAccueilVendeurNomVendeur.setText("Nom : "+this.utilisateurBD.getUtilisateurConnecter().getNom());
            this.labelAccueilVendeurPrenomVendeur.setText("Prenom : "+this.utilisateurBD.getUtilisateurConnecter().getPrenom());
            this.labelAccueilVendeuIDVendeur.setText("ID : "+Integer.toString(this.utilisateurBD.getUtilisateurConnecter().getId()));
            this.labelAccueilVendeurBienvunueNomVendeur.setText("Bienvenue Mr "+this.utilisateurBD.getUtilisateurConnecter().getNom()+"! Voici vos informations >");
            //"Bienvenue "+this.utilisateurBD.getUtilisateurConnecter().getNom()+"! Voici vos informations >"
            this.labelAccueilVendeurNomMagasinVendeur.setText(this.vendeurBD.getMagasin(this.utilisateurBD.getUtilisateurConnecter().getId()).toString());

        }
        catch(SQLException e){
            //app.afficherErreur("Erreur lors de la récupération des meilleurs vente");
            app.afficherErreur("Erreur lors de la récupération de certaines données");
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

