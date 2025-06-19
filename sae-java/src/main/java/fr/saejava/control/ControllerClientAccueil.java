package fr.saejava.control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.sql.SQLException;
import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;



public class ControllerClientAccueil{

    @FXML
    private Button buttonAjouterPanier;

    @FXML
    private ListView<Livre> listViewMeileurVentes;

    @FXML
    private ListView<Livre> listViewRecommande;

    @FXML
    private ComboBox<Integer> comboBoxQuantite;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private ClientBD clientBD;
    private VendeurBD vendeurBD;
    private Livre livreSelectionner;

    public ControllerClientAccueil (){
        // Default Constructor
    }

    public ControllerClientAccueil(ApplicationLibrairie app, UtilisateurBD utilisateurBD, ClientBD clientBD, VendeurBD vendeurBD) {
        this.app = app;
        this.utilisateurBD = utilisateurBD;
        this.clientBD = clientBD;
        this.vendeurBD = vendeurBD;
    }

 
    @FXML
    public void initialize() {
        try{
            listViewMeileurVentes.setItems(FXCollections.observableArrayList(clientBD.getMeilleurVente()));
            List<Livre> livresRecommandeDispo = new ArrayList<>();
            Map<Livre,Boolean> livresRecommande = clientBD.onVousRecommande(((Client)utilisateurBD.getUtilisateurConnecter()));
            for(Livre livre : livresRecommande.keySet()){
                if(livresRecommande.get(livre).equals(Boolean.TRUE)){
                    livresRecommandeDispo.add(livre);
                }
            }
            listViewRecommande.setItems(FXCollections.observableArrayList(livresRecommandeDispo));
        }
        catch(SQLException e){
            app.afficherErreur("Erreur lors de la récupération des meilleurs vente");
        }
    }
    
  
    
    @FXML
    public void ajouterPanier(MouseEvent event) {
        ((Client) utilisateurBD.getUtilisateurConnecter()).ajouterLivre(livreSelectionner, comboBoxQuantite.getSelectionModel().getSelectedItem());
        app.afficherInformation("Livre ajouté au panier avec succès !");
    }

    @FXML
    void selectionnerLivre(MouseEvent event) {
        livreSelectionner = listViewRecommande.getSelectionModel().getSelectedItem();
        comboBoxQuantite.getItems().clear();
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
                comboBoxQuantite.getItems().add(i);
            }
            if (!comboBoxQuantite.getItems().isEmpty()) {
                comboBoxQuantite.getSelectionModel().selectFirst();
            }
        } else {
            app.afficherInformation("Veuillez sélectionner un livre.");
        }
    }
}