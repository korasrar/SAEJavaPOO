package fr.saejava.control;

import fr.saejava.ApplicationLibrairie;
import fr.saejava.model.ClientBD;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ControllerClientHeader {

    @FXML
    private TextField clientSearchBar;

    @FXML
    private Label clientUsername;

    @FXML
    private ImageView logoLibrairieHome;

    @FXML
    private ImageView logoUserProfil;

    private ApplicationLibrairie app;
    private ClientBD clientBD;

    public ControllerClientHeader(){
        // Default Constructor
    }

    public ControllerClientHeader(ApplicationLibrairie app, ClientBD clientBD){
        this.app=app;
        this.clientBD=clientBD;
    }

    @FXML
    void retourAcceuil(MouseEvent event) {
        app.afficherClientMainView(app.getStage());
    }

    @FXML
    void startRecherche(KeyEvent event) {
        if(event.getCode()==KeyCode.ENTER){
            app.afficherRechercheLivreView(app.getStage(), clientSearchBar.getText());
        }
    }

    @FXML
    void afficherProfil(MouseEvent event) {

    }

    

}

