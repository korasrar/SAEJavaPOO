package fr.saejava.control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerAccueil implements Initializable {

    @FXML
    private Button idAjouterPanier;
    
    @FXML
    private ListView<?> listView; 
    
    /**
     * Initialise le contrôleur après le chargement du FXML
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComponents();
    }
    
    /**
     * Méthode appelée lors du clic sur le bouton "Ajouter au panier"
     */
    @FXML
    private void ajouterPanier(MouseEvent event) {
        try {
            ajouterArticleAuPanier();
            
           // afficherConfirmation("Article ajouté au panier avec succès !");
            
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
    
    /**
     * Initialise les composants de l'interface
     */
    private void initializeComponents() {
        configurerListView();
        configurerBoutonAjout();
    }
    
    /**
     * Configure le ListView des meilleures ventes
     */
    private void configurerListView() {
        if (listView != null) {

        }
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