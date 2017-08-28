package lunar;

import framework.PhysicsGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

import static framework.PhysicsWorldHelper.getStage;

public class MenuController {

    @FXML private HBox root;

    private String[] levels = new String[]{"level1.fxml","level2.fxml","level3.fxml"};
    private int currentLevel = 0;

    private Stage stage;
    private PhysicsGame game;
    private Scene menuScene;

    @FXML
    private void startGame(ActionEvent actionEvent) {
        menuScene = root.getScene();
        stage = getStage(root);
        game = new PhysicsGame();


        StartLevel(levels[currentLevel]);
        game.setOnLevelEnd(() -> {
           LoadNextLevel();
        });
    }

    private void LoadNextLevel() {
        currentLevel ++;
        if (currentLevel < levels.length){
            StartLevel(levels[currentLevel]);
        }else {
            currentLevel = 2;
            stage.setScene(menuScene);
            stage.show();
        }
    }

    private void StartLevel(String level) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(level));
            Region root = loader.load();

            stage.setTitle("Lunar lander");
            Scene scene = new Scene(root);
            scene.setOnKeyPressed(e -> root.getOnKeyPressed().handle(e));
            scene.setOnKeyReleased(e -> root.getOnKeyReleased().handle(e));

            stage.setScene(scene);
            stage.show();

            game.load(root);
            game.startGame();
        }catch (IOException e){
            System.out.println("Exception");
        }
    }

}
