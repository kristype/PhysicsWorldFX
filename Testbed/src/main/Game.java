package main;

import framework.PhysicsGame;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Game extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameWorld.fxml"));
        Region root = loader.load();
        primaryStage.setTitle("Physics game");
        Scene scene = new Scene(root);

        //Transfer key presses to PhysicsWorld
        scene.setOnKeyPressed(e -> {
            EventHandler<? super KeyEvent> onKeyPressed = root.getOnKeyPressed();
            if (onKeyPressed != null)
                onKeyPressed.handle(e);
        });

        //Transfer key releases to PhysicsWorld
        scene.setOnKeyReleased(e -> {
            EventHandler<? super KeyEvent> onKeyReleased = root.getOnKeyReleased();
            if (onKeyReleased != null)
                onKeyReleased.handle(e);
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
