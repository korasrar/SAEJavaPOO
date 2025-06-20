package fr.saejava.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import com.itextpdf.text.pdf.qrcode.Mode;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ControllerPanierView {

    @FXML
    private Button buttonPanierClientFinaliserCommande;

    @FXML
    private TableView<DetailCommande> tablePanier;

    @FXML
    private TableColumn<DetailCommande, Double> colonnePrix;

    @FXML
    private TableColumn<DetailCommande, Double> colonnePrixTotal;

    @FXML
    private TableColumn<DetailCommande, Integer> colonneQuantite;

    @FXML
    private TableColumn<DetailCommande, String> colonneTitre;

    @FXML
    private TableColumn<DetailCommande, Button> colonneSuprimmer;

    @FXML
    private ComboBox<Magasin> comboBoxChoixMagasin;

    @FXML
    private ComboBox<ModeLivraison> comboBoxLivraison;

    private ApplicationLibrairie app;
    private ClientBD clientBD;
    private CommandeBD commandeBD;
    private UtilisateurBD utilisateurBD;
    private MagasinBD magasinBD;
    private Commande panierActuel;

    public ControllerPanierView() {
        // Default Constructor
    }

    public ControllerPanierView(ApplicationLibrairie app, ClientBD clientBD, UtilisateurBD utilisateurBD, CommandeBD commandeBD, MagasinBD magasinBD) {
        this.app = app;
        this.clientBD = clientBD;
        this.commandeBD = commandeBD;
        this.utilisateurBD = utilisateurBD;
        this.magasinBD = magasinBD;
        this.panierActuel = ((Client)utilisateurBD.getUtilisateurConnecter()).getPanier();
    }

    @FXML
    public void initialize() {
        // création du tableau
        colonneTitre.setCellValueFactory(caseTable -> 
            new SimpleStringProperty(caseTable.getValue().getLivre().getTitre()));
            
        colonnePrix.setCellValueFactory(caseTable -> 
            new SimpleDoubleProperty(caseTable.getValue().getLivre().getPrix()).asObject());

        colonneQuantite.setCellValueFactory(caseTable -> 
            new SimpleIntegerProperty(caseTable.getValue().getQte()).asObject());
            
        colonnePrixTotal.setCellValueFactory(caseTable -> 
            new SimpleDoubleProperty(caseTable.getValue().getPrixVente()).asObject());

        colonneSuprimmer.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null));
        colonneSuprimmer.setCellFactory(new Callback<TableColumn<DetailCommande, Button>, TableCell<DetailCommande, Button>>() {
            public TableCell<DetailCommande, Button> call(TableColumn<DetailCommande, Button> param) {
                return new TableCell<DetailCommande, Button>() {
                    private final Button supprimerButton = new Button("❌");
                    
                    {
                        supprimerButton.setOnAction(event -> {
                            DetailCommande detail = getTableView().getItems().get(getIndex());
                            panierActuel.supprimerDetail(detail);
                            tablePanier.getItems().remove(detail);
                        });
                    }

                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(supprimerButton);
                        }
                    }
                };
            }
        });
        afficherPanier();

    // création des combobox
        List<Magasin> magasins = new ArrayList<>();
        try{
        magasins = magasinBD.chargerMagasin();
        }
        catch(SQLException e){
            app.afficherErreur("Erreur de chargement des magasins : "+ e.getMessage());
            e.printStackTrace();
        }
        ObservableList<Magasin> observableMagasins = FXCollections.observableArrayList(magasins);
        comboBoxChoixMagasin.setItems(observableMagasins);
        comboBoxChoixMagasin.getSelectionModel().selectFirst();

        List<ModeLivraison> modesLivraison = Arrays.asList(ModeLivraison.MAISON,ModeLivraison.MAGASIN);
        ObservableList<ModeLivraison> observableModesLivraison = FXCollections.observableArrayList(modesLivraison);
        comboBoxLivraison.setItems(observableModesLivraison);
        comboBoxLivraison.getSelectionModel().selectFirst();
        
        comboBoxLivraison.valueProperty().addListener((obs, oldValue, newValue) -> {
        if (newValue == ModeLivraison.MAISON) {
            comboBoxChoixMagasin.setVisible(false);
        } else {
            comboBoxChoixMagasin.setVisible(true);
        }
    });
    
    // Déclencher l'événement une première fois pour initialiser correctement l'interface
    ModeLivraison modeInitial = comboBoxLivraison.getValue();
    comboBoxChoixMagasin.setVisible(modeInitial != ModeLivraison.MAISON);
    }

    private void afficherPanier(){
        List<DetailCommande> tousLesArticles = panierActuel.getContenue();
        ObservableList<DetailCommande> observableArticles = FXCollections.observableArrayList(tousLesArticles);
        tablePanier.setItems(observableArticles);
        buttonPanierClientFinaliserCommande.setDisable(tousLesArticles.isEmpty());
    }

    @FXML
    public void finaliserCommande(MouseEvent event) {
        ModeLivraison modeLivraison = comboBoxLivraison.getValue();
        Magasin magasinChoisi = comboBoxChoixMagasin.getValue();

        try{
            if(modeLivraison == ModeLivraison.MAGASIN) {
                clientBD.finaliseCommande(((Client)utilisateurBD.getUtilisateurConnecter()), modeLivraison, panierActuel, magasinChoisi);
            }
            else if(modeLivraison == ModeLivraison.MAISON) {
                clientBD.finaliseCommande(((Client)utilisateurBD.getUtilisateurConnecter()), modeLivraison, panierActuel, null);
            }
        }
        catch(SQLException e){
            app.afficherErreur("Erreur lors de la finalisation de la commande : " + e.getMessage());
            e.printStackTrace();
        }
        catch (LivrePasDansStockMagasinException e) {
            app.afficherErreur("Un ou plusieurs livres ne sont pas disponibles dans le magasin choisi : " + e.getMessage());
            e.printStackTrace();
        }
        panierActuel.viderCommande();
        afficherPanier();
        app.afficherInformation("Commande finalisée avec succès !");
    }
}
