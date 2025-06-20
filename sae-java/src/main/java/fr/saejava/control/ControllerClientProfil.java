package fr.saejava.control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;


public class ControllerClientProfil{

    @FXML
    private Button idButtonConfimer;

    @FXML
    private Button idButtonDeconnexion;

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
    private Stage stage;

    public ControllerClientProfil (){}

    public ControllerClientProfil(ApplicationLibrairie app, UtilisateurBD utilisateurBD, ClientBD clientBD, Stage stage){
        this.app = app;
        this.utilisateurBD = utilisateurBD;
        this.clientBD = clientBD;
        this.stage = stage;
    }

 
    @FXML
    public void initialize() {
        try {
            this.idLabelModifierPrenom.setText(utilisateurBD.getUtilisateurConnecter().getPrenom());
            this.idModifierPrenom.setText(utilisateurBD.getUtilisateurConnecter().getPrenom());
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

    @FXML
    void confirmerModif(MouseEvent event) {
        Client utilisateurConnecter = (Client) utilisateurBD.getUtilisateurConnecter();
        String nom = idModifierNom.getText();
        String prenom = idModifierPrenom.getText();
        String pseudo = idModifierPseudo.getText();
        String motDePasse = idModifierMdp.getText();
        String adresse = idModifierAdresse.getText();
        int codePostal = Integer.parseInt(idModifierCodePostal.getText());
        String ville = idModifierVille.getText();
        
        if(idModifierMdp.getText().isEmpty()) {
            motDePasse = utilisateurConnecter.getMotDePasse();
        }
        Client client = new Client(utilisateurConnecter.getNum(), adresse, ville, codePostal, nom, prenom, pseudo, motDePasse);
        try {
            clientBD.modifierClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        app.afficherInformation("Modification du profil réussie");
    }

    @FXML
    void retourAccueil(MouseEvent event) {
        stage.close();
    }

    @FXML
    void deconnexion(MouseEvent event) {
        utilisateurBD.deconnecter();
        app.afficherInformation("Déconnexion réussie");
        stage.close();
        app.fermerClientMainView(app.getStage());
        app.afficherConnexionView(stage);
    }
}
