package lunar;

import framework.PhysicsGame;
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

    private String[] levels = {"level1.fxml","level2.fxml","level3.fxml"};
    private int currentLevel = 2;

    private Stage stage;
    private PhysicsGame game;
    private Scene menuScene;

    @FXML
    private void startGame() {
        menuScene = root.getScene();
        stage = getStage(root);
        game = new PhysicsGame();

        startLevel(levels[currentLevel]);
        game.setOnFinish(() -> {
           loadNextLevel();
        });
    }

    private void loadNextLevel() {
        currentLevel++;
        if (currentLevel < levels.length){
            startLevel(levels[currentLevel]);
        }else {
            currentLevel = 2;
            stage.setScene(menuScene);
            stage.show();
        }
    }

    private void startLevel(String level) {
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
            game.enableDebug();
            game.startGame();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
