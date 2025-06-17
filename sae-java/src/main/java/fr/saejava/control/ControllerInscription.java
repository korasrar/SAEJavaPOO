package fr.saejava.control;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.ClientBD;
import fr.saejava.model.ConnexionMySQL;
import fr.saejava.model.UtilisateurBD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ControllerInscription {

    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonRetour;

    @FXML
    private TextField textFieldAdresse;

    @FXML
    private TextField textFieldCodePostal;

    @FXML
    private TextField textFieldIdentifiant;

    @FXML
    private TextField textFieldMotDePasse;

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldPrenom;

    @FXML
    private TextField textFieldVIlle;

    private ConnexionMySQL connexion;
    private UtilisateurBD utilisateurBD;
    private ClientBD clientBD;
    private ApplicationLibrairie app;

    // Règle pour le textField de codePostal
    @FXML
    public void initialize() {
        limiterTextField(textFieldCodePostal,5);
    }

    private void limiterTextField(TextField textField, int tailleMax) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() > tailleMax) {
                textField.setText(oldValue);
            }});
    }

    public ControllerInscription() {
        // Default constructor
    }

    public ControllerInscription(ApplicationLibrairie app, ConnexionMySQL connexion) {
        this.app = app;
        this.connexion=connexion;
        this.utilisateurBD = new UtilisateurBD(connexion);
        this.clientBD= new ClientBD(connexion);
    }

    @FXML
    void inscription(MouseEvent event) {
        try{
            String identifiant = textFieldIdentifiant.getText();
            String motDePasse = textFieldMotDePasse.getText();
            String nom = textFieldNom.getText();
            String prenom = textFieldPrenom.getText();
            String adresse = textFieldAdresse.getText();
            String codePostal = textFieldCodePostal.getText();
            String ville = textFieldVIlle.getText();

            int codePostalInt = 0;
            codePostalInt = Integer.parseInt(codePostal);

            if (identifiant.isEmpty() || motDePasse.isEmpty() || nom.isEmpty() || prenom.isEmpty() ||
                adresse.isEmpty() || codePostal.isEmpty() || ville.isEmpty()) {
                app.afficherErreur("Veuillez remplir tous les champs.");
            }
            else{
                int idCLient = (utilisateurBD.getDernierID()+1);
                // int num, String adresse, String ville, int codePostal, String nom, String prenom, String username, String motDePasse
                clientBD.creeCompteClient(idCLient, adresse, ville, codePostalInt, nom, prenom, identifiant, motDePasse);
                app.afficherInformation("Inscription réussie !");
            }
        }
        catch (NumberFormatException e){
            app.afficherErreur("Le code postal doit être un nombre.");
            textFieldCodePostal.setText("");
        }
        catch (Exception e) {
            app.afficherErreur("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    @FXML
    void retour(MouseEvent event) {
        app.afficherConnexion(app.getStage());
    }

}

