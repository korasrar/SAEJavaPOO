package fr.saejava.control;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class ControllerConnexion {

    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonQuitter;

    @FXML
    private Button buttonInscription;

    @FXML
    private TextField textFieldIdentifiant;

    @FXML
    private PasswordField textFieldMotDePasse;

    private UtilisateurBD utilisateurBD;
    private ApplicationLibrairie app;

    public ControllerConnexion() {
        // Default constructor
    }

    public ControllerConnexion(ApplicationLibrairie app, UtilisateurBD utilisateurBD) {
        this.app = app;
        this.utilisateurBD = utilisateurBD;
    }

    @FXML
    public void connexion(MouseEvent event) {
        String identifiant = textFieldIdentifiant.getText();
        String motDePasse = textFieldMotDePasse.getText();


        if (identifiant.isEmpty() || motDePasse.isEmpty()) {
            app.afficherErreur("Veuillez remplir tous les champs.");
            return;
        }

        try {
            Utilisateur utilisateur = utilisateurBD.getUtilisateur(identifiant, motDePasse);
            utilisateurBD.setUtilisateurConnecter(utilisateur);
            if (utilisateur instanceof Admin) {
                // Afficher main Admin
            } 
            else if (utilisateur instanceof Vendeur) {
                app.afficherVendeurMainView(app.getStage());
            } 
            else if (utilisateur instanceof Client) {
                app.afficherClientMainView(app.getStage());
            }
        }
        catch (SQLException e){
            app.afficherErreur("Erreur de connexion à la base de données.");
            return;
        } catch (UtilisateurIntrouvableException e) {
            app.afficherErreur("Identifiant ou mot de passe incorrect.");
            return;
        } catch (VendeurSansMagasinException e) {
            app.afficherErreur("Votre compte vendeur n'a pas de magasin associé.");
            return;
        } catch (MotDePasseIncorrectException e) {
            app.afficherErreur("Mot de passe incorrect.");
            return;
        }
    }

    @FXML
    void inscrire(MouseEvent event) {
        app.afficherInscriptionView(app.getStage());
    }

    @FXML
    void quitter(MouseEvent event) {
        app.exit();
    }

}
