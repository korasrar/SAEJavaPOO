package fr.saejava.control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;


public class ControllerClientProfil{

    @FXML
    private Button idButtonConfimer;

    @FXML
    private Button idButtonRetour;

    @FXML
    private Label idLabelModifierPrenom;

    @FXML
    private TextField idModifierAdresse;

    @FXML
    private TextField idModifierCodePostal;

    @FXML
    private TextField idModifierMdp;

    @FXML
    private TextField idModifierNom;

    @FXML
    private TextField idModifierNum;

    @FXML
    private TextField idModifierPrenom;

    @FXML
    private TextField idModifierPseudo;

    @FXML
    private TextField idModifierVille;

    
    private ApplicationLibrairie app;
    private UtilisateurBD utilisateurBD;
    private ClientBD clientBD;

    public ControllerClientProfil (){}

    public ControllerClientProfil(ApplicationLibrairie app, UtilisateurBD utilisateurBD, ClientBD clientBD){
        this.app = app;
        this.utilisateurBD = utilisateurBD;
        this.clientBD = clientBD;
    }

 
    @FXML
    public void initialize() {
        try {

            this.idLabelModifierPrenom.setText(utilisateurBD.getUtilisateurConnecter().getPrenom());
            this.idModifierNom.setText(utilisateurBD.getUtilisateurConnecter().getNom());
            this.idModifierPseudo.setText(utilisateurBD.getUtilisateurConnecter().getPseudo());
            this.idModifierAdresse.setText(((Client)utilisateurBD.getUtilisateurConnecter()).getAdresse());
            this.idModifierCodePostal.setText(String.valueOf(((Client)utilisateurBD.getUtilisateurConnecter()).getCodePostal()));
            this.idModifierVille.setText(((Client)utilisateurBD.getUtilisateurConnecter()).getVille());

        } catch (Exception e) {
            app.afficherErreur("Erreur lors de la modification du profil : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void modifierProfil(){
        String nom = idModifierNom.getText();
        String prenom = idModifierPrenom.getText();
        String pseudo = idModifierPseudo.getText();
        String motDePasse = idModifierMdp.getText();
        String adresse = idModifierAdresse.getText();
        int codePostal = Integer.parseInt(idModifierCodePostal.getText());
        String ville = idModifierVille.getText();
        Client client = new Client(((Client)utilisateurBD.getUtilisateurConnecter()).getNum(), adresse, ville, codePostal, nom, prenom, pseudo, motDePasse);
        try {
            clientBD.modifierClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void confirmerModif(MouseEvent event) {

        this.idButtonConfimer.setOnMouseClicked((MouseEvent e) -> {
            modifierProfil();
        });
    }

    public void retourAccueil(){
        this.idButtonRetour.setOnMouseClicked((MouseEvent e) -> {
            app.afficherClientMainView(app.getStage());
        });
    }


}

