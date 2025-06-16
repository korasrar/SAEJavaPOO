package fr.saejava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private FXMLLoader loader;
    private Pane root;
    private Scene scene;





    // Load la page de connexion par défaut
    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("/view/ConnexionView.fxml"));
        this.root = loader.load();
        this.scene = new Scene(this.root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("SAE Java Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
