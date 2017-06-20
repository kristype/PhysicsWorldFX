package pong;

import framework.PhysicsGame;
import framework.PhysicsWorld;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PongGame extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Pong");
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        PhysicsGame game = new PhysicsGame();
        game.load(root);
        game.startGame();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
