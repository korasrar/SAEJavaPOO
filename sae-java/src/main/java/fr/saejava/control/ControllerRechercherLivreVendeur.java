package fr.saejava.control;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerRechercherLivreVendeur {

    @FXML
    private Button buttonRetour;

    @FXML
    private Button buttonSelectionner;

    @FXML
    private ListView<Livre> listViewResultatRecherche;

    @FXML
    private ComboBox<Magasin> comboBoxMagasin;

    private Livre livreSelectionner;
    private ApplicationLibrairie app;
    private ClientBD clientBD;
    private LivreBD livreBD;
    private VendeurBD vendeurBD;
    private UtilisateurBD utilisateurBD;
    private String livreARechercher;
    private List<Livre> listeLivreResultat;
    private Stage stage;
    private MagasinBD magasinBD;

    public ControllerRechercherLivreVendeur() {
        // Constructeur par défaut
    }

    public ControllerRechercherLivreVendeur(ApplicationLibrairie app, Stage stage, String livreARechercher, LivreBD livreBD, VendeurBD vendeurBD, MagasinBD magasinBD, UtilisateurBD utilisateurBD) {
        this.app = app;
        this.stage = stage;
        this.utilisateurBD = utilisateurBD;
        this.livreARechercher = livreARechercher;
        this.livreBD = livreBD;
        this.vendeurBD=vendeurBD;
        this.magasinBD=magasinBD;
    }

    @FXML
    void initialize() throws VendeurSansMagasinException {
        buttonSelectionner.setDisable(true);
        try{
        //this.comboBoxMagasin.getItems().addAll(this.magasinBD.chargerMagasin());
        List<Livre> livreDispo = new ArrayList<>();
        Map<Livre,Boolean> mapLivres = livreBD.rechercherLivre(Filtre.titre, "", livreARechercher, "", vendeurBD);
        for(Livre livre : mapLivres.keySet()){
            if(mapLivres.get(livre).equals(Boolean.TRUE) && vendeurBD.verifierDispo(this.vendeurBD.getMagasin(this.utilisateurBD.getUtilisateurConnecter().getId()), livre) == true){
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
    void retour(MouseEvent event) {
        stage.close();
    }

    @FXML
    void selectionner(MouseEvent event) {
        this.buttonSelectionner.setDisable(false);
    }

    @FXML
    void selectionnerLivre(MouseEvent event) {
        this.livreSelectionner = listViewResultatRecherche.getSelectionModel().getSelectedItem();
        this.livreBD.setLivreSelectionner(this.livreSelectionner);
        System.out.println(livreBD.getLivreSelectionner());
        app.afficherInformation("Livre sélectionné");
        stage.close();
    }
}

