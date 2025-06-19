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

    private Livre livreSelectionner;
    private ApplicationLibrairie app;
    private ClientBD clientBD;
    private LivreBD livreBD;
    private VendeurBD vendeurBD;
    private UtilisateurBD utilisateurBD;
    private String livreARechercher;
    private List<Livre> listeLivreResultat;
    private Stage stage;
    

    public ControllerRechercherLivreVendeur() {
        // Constructeur par défaut
    }

    public ControllerRechercherLivreVendeur(ApplicationLibrairie app, Stage stage, String livreARechercher, LivreBD livreBD) {
        this.app = app;
        this.stage = stage;
        this.livreARechercher = livreARechercher;
        this.livreBD = livreBD;
    }

    @FXML
    void initialize() {
        buttonSelectionner.setDisable(true);
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
    void retour(MouseEvent event) {
        stage.close();
    }

    @FXML
    void selectionnerLivre(MouseEvent event) {
        buttonSelectionner.setDisable(false);
        livreSelectionner = listViewResultatRecherche.getSelectionModel().getSelectedItem();
        if (livreSelectionner != null) {
            livreBD.setLivreSelectionner(livreSelectionner);
            app.afficherInformation("Livre sélectionné : " + livreSelectionner.getTitre());
            stage.close();
        } else {
            app.afficherErreur("Veuillez sélectionner un livre.");
        }
    }

}

