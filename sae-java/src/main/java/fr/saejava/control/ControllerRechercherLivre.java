package fr.saejava.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ControllerRechercherLivre {

    @FXML
    private Button buttonAjouterPanier;

    @FXML
    private Button buttonRetour;
    
    @FXML
    private ListView<Livre> listViewResultatRecherche;

    private Livre livreSelectionner;
    private ApplicationLibrairie app;
    private ClientBD clientBD;
    private LivreBD livreBD;
    private VendeurBD vendeurBD;
    private UtilisateurBD utilisateurBD;
    private String livreARechercher;
    private List<Livre> listeLivreResultat;

    public ControllerRechercherLivre(){
        // Default Constructor
    }

    public ControllerRechercherLivre(ApplicationLibrairie app, ClientBD clientBD, LivreBD livreBD, VendeurBD vendeurBD,UtilisateurBD utilisateurBD, String livreARechercher){
        this.app=app;
        this.clientBD=clientBD;
        this.livreBD=livreBD;
        this.vendeurBD=vendeurBD;
        this.utilisateurBD=utilisateurBD;
        this.livreARechercher=livreARechercher;
    }

    @FXML
    public void initialize(){
        try{
        List<Livre> livreDispo = new ArrayList<>();
        Map<Livre,Boolean> mapLivres = livreBD.rechercherLivre(Filtre.titre, "", livreARechercher, "", vendeurBD);
        for(Livre livre : mapLivres.keySet()){
            if(mapLivres.get(livre).equals(Boolean.TRUE)){
                livreDispo.add(livre);
            }
        }
        listViewResultatRecherche.setItems(FXCollections.observableArrayList(livreDispo));
        }
        catch(SQLException e){
            app.afficherErreur("Erreur lors de la recherche des livres : "+ e.getMessage());
        }
    }

    @FXML
    void ajouterAuPanier(MouseEvent event) {
        ((Client) utilisateurBD.getUtilisateurConnecter()).ajouterLivre(livreSelectionner,1);
        retour(null);
    }

    @FXML
    void retour(MouseEvent event) {
        app.afficherClientMainView(app.getStage());
    }

    @FXML
    void selectionnerLivre(MouseEvent event) {
        livreSelectionner = listViewResultatRecherche.getSelectionModel().getSelectedItem();
    }

}
