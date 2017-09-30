package main;

import framework.PhysicsGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import static framework.PhysicsWorldFunctions.*;

public class Game extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameWorld.fxml"));
        Region root = loader.load();
        primaryStage.setTitle("Physics game");
        Scene scene = new Scene(root);

        //Register key pressed
        scene.setOnKeyPressed(e -> {
            registerKeyPressed(e.getCode());
        });

        //Register key released
        scene.setOnKeyReleased(e -> {
            registerKeyReleased(e.getCode());
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        //Create a new physics game
        PhysicsGame game = new PhysicsGame();

        //Load the game
        game.load(root);

        //Start the game
        game.startGame();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
