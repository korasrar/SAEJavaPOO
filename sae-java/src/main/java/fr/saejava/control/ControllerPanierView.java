package fr.saejava.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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
    private Button buttonSuivant;

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
    private Label numeroPage;

    @FXML
    private Button precedent;

    private int pageCourante = 1;
    private int livreParPages = 5;

    private ApplicationLibrairie app;
    private ClientBD clientBD;
    private CommandeBD commandeBD;
    private UtilisateurBD utilisateurBD;
    private Commande panierActuel;

    public ControllerPanierView() {
        // Default Constructor
    }

    public ControllerPanierView(ApplicationLibrairie app, ClientBD clientBD, UtilisateurBD utilisateurBD, CommandeBD commandeBD) {
        this.app = app;
        this.clientBD = clientBD;
        this.commandeBD = commandeBD;
        this.utilisateurBD = utilisateurBD;
        this.panierActuel = ((Client)utilisateurBD.getUtilisateurConnecter()).getPanier();
    }

    @FXML
    public void initialize() {
        colonneTitre.setCellValueFactory(caseTable -> 
            new SimpleStringProperty(caseTable.getValue().getLivre().getTitre()));
            
        colonnePrix.setCellValueFactory(caseTable -> 
            new SimpleDoubleProperty(caseTable.getValue().getLivre().getPrix()).asObject());

        colonneQuantite.setCellValueFactory(caseTable -> 
            new SimpleIntegerProperty(caseTable.getValue().getQte()).asObject());
            
        colonnePrixTotal.setCellValueFactory(caseTable -> 
            new SimpleDoubleProperty(caseTable.getValue().getPrixVente()).asObject());

       colonneSuprimmer.setCellFactory(new Callback<TableColumn<DetailCommande, Button>, TableCell<DetailCommande, Button>>() {
            public TableCell<DetailCommande, Button> call(TableColumn<DetailCommande, Button> param) {
                return new TableCell<DetailCommande, Button>() {
                    private final Button supprimerButton = new Button("❌");
                    
                    {
                        supprimerButton.setOnAction(event -> {
                            DetailCommande detail = getTableView().getItems().get(getIndex());
                            panierActuel.supprimerDetail(detail);
                            tablePanier.getItems().remove(detail);
                            if (panierActuel.getContenue().size() <= (pageCourante-1)*livreParPages && pageCourante > 1) {
                                pageCourante--;
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(supprimerButton);
                        }
                    }
                };
            }
        });
    afficherPanier();
    setNbPages();
    majBoutonPages();
    }

    private void setNbPages() {
        List<DetailCommande> tousLesArticles = panierActuel.getContenue();
        numeroPage.setText(pageCourante + "/" + (int) tousLesArticles.size()/livreParPages);
        majBoutonPages();
    }

    private void afficherPanier(){
        List<DetailCommande> articlesParPage = new ArrayList<>();
        List<DetailCommande> tousLesArticles = panierActuel.getContenue();
        if (((pageCourante-1)*livreParPages)<tousLesArticles.size()) {
            articlesParPage = tousLesArticles.subList((pageCourante-1)*livreParPages, ((pageCourante-1)*livreParPages)+tousLesArticles.size());
        }
        ObservableList<DetailCommande> observableArticles = FXCollections.observableArrayList(articlesParPage);
        tablePanier.setItems(observableArticles);
        numeroPage.setText(pageCourante+"/"+(int) tousLesArticles.size()/livreParPages);
        buttonPanierClientFinaliserCommande.setDisable(tousLesArticles.isEmpty());
    }

    private void majBoutonPages() {
        if (pageCourante == 1) {
            precedent.setDisable(true);
        } else {
            precedent.setDisable(false);
        }
        if (pageCourante * livreParPages >= panierActuel.getContenue().size()) {
            buttonSuivant.setDisable(true);
        } else {
            buttonSuivant.setDisable(false);
        }
    }

    @FXML
    void pagePrecedente(MouseEvent event) {
        if (pageCourante > 1) {
            pageCourante--;
            afficherPanier();
            majBoutonPages();
        }
    }

    @FXML
    void pageSuivante(MouseEvent event) {
        if (pageCourante * livreParPages < panierActuel.getContenue().size()) {
            pageCourante++;
            afficherPanier();
            majBoutonPages();
        }
    }

}
