package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class LunarLanderGame extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/overlay.fxml"));
        Region root = loader.load();

        primaryStage.setTitle("Lunar lander");
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> root.getOnKeyPressed().handle(e));
        scene.setOnKeyReleased(e -> root.getOnKeyReleased().handle(e));

        primaryStage.setScene(scene);
        primaryStage.show();

/*        PhysicsGame game = new PhysicsGame();
        game.load(root);
        game.startGame();*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}
