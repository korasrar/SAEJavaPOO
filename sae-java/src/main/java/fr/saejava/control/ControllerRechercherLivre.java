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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerRechercherLivre {

    @FXML
    private Button buttonAjouterPanier;

    @FXML
    private Button buttonRetour;

    @FXML
    private ComboBox<Integer> comboBoxQuantité;
    
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
    private Stage stage;

    public ControllerRechercherLivre(){
        // Default Constructor
    }

    public ControllerRechercherLivre(ApplicationLibrairie app, ClientBD clientBD, LivreBD livreBD, VendeurBD vendeurBD,UtilisateurBD utilisateurBD, String livreARechercher, Stage stage){
        this.app=app;
        this.clientBD=clientBD;
        this.livreBD=livreBD;
        this.vendeurBD=vendeurBD;
        this.utilisateurBD=utilisateurBD;
        this.livreARechercher=livreARechercher;
        this.stage = stage;
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
        ((Client) utilisateurBD.getUtilisateurConnecter()).ajouterLivre(livreSelectionner, comboBoxQuantité.getSelectionModel().getSelectedItem());
        app.afficherInformation("Livre ajouté au panier avec succès !");
        retour(null);
    }

    @FXML
    void retour(MouseEvent event) {
        stage.close();
    }

    @FXML
    void selectionnerLivre(MouseEvent event) {
        livreSelectionner = listViewResultatRecherche.getSelectionModel().getSelectedItem();
        comboBoxQuantité.getItems().clear();
        Integer quantiteMax = 0;
        if (livreSelectionner != null) {
            try{
                quantiteMax = vendeurBD.getQteDispo(livreSelectionner);
            }
            catch (SQLException e) {
                app.afficherErreur("Erreur lors de la récupération de la quantité disponible : " + e.getMessage());
                return;
            }
            for (int i = 1; i <= quantiteMax; i++) {
                comboBoxQuantité.getItems().add(i);
            }
            if (!comboBoxQuantité.getItems().isEmpty()) {
                comboBoxQuantité.getSelectionModel().selectFirst();
            }
        } else {
            app.afficherInformation("Veuillez sélectionner un livre.");
        }
    }

}
