package fr.saejava.control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;
import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;



public class ControllerClientAccueil{

    @FXML
    private Button idAjouterPanier;
    
    @FXML
    private ListView<Livre> listViewMeilleurVentes2;

    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private ClientBD clientBD;

    public ControllerClientAccueil (){}

    public ControllerClientAccueil(ApplicationLibrairie app, UtilisateurBD utilisateurBD, ClientBD clientBD){
        this.app = app;
        this.utilisateurBD = utilisateurBD;
        this.clientBD = clientBD;
    }

 
    @FXML
    public void initialize() {
        try{
             listViewMeilleurVentes2.setItems(FXCollections.observableArrayList(clientBD.getMeilleurVente()));

        }
        catch(SQLException e){
            app.afficherErreur("Erreur lors de la récupération des meilleurs vente");
        }
    }
    
  
    
    @FXML
    private void ajouterPanier(MouseEvent event) {
        try {
            
           
            
        } catch (Exception e) {
          System.out.println("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }
    
    /**
     * Ajoute l'article recommandé au panier
     */
    private void ajouterArticleAuPanier() {
        System.out.println("Article ajouté au panier");
    }

    
    private void configurerBoutonAjout() {
        if (idAjouterPanier != null) {
            idAjouterPanier.setDisable(false);
        }
    }
    
    public void naviguerVersVitrine() {
        // 
    }
    
    public void naviguerVersPromos() {
        // 
    }
    
    public void naviguerVersExclusifs() {
        //
    }
}