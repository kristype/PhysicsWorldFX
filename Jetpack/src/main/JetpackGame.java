package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class JetpackGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/overlay.fxml"));
        Region root = loader.load();

        primaryStage.setTitle("Jetpack");
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> root.getOnKeyPressed().handle(e));
        scene.setOnKeyReleased(e -> root.getOnKeyReleased().handle(e));

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}