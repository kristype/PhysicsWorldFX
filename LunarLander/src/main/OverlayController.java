package lunar;

import framework.PhysicsGame;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;

import static framework.PhysicsWorldFunctions.*;

public class OverlayController {

    @FXML private Pane root;
    @FXML private Label score;
    @FXML private Label title;
    @FXML private Label pressSpace;

    private int scoreValue = 0;

    private String[] levels = {"level1.fxml","level2.fxml","level3.fxml"};
    private int currentLevel = 0;

    private PhysicsGame game;
    private Region currentLoadedLevel;

    @FXML
    private void initialize(){
        updateScore();
    }

    private void startGame() {
        game = new PhysicsGame();
        title.setVisible(false);
        pressSpace.setVisible(false);
        scoreValue = 0;
        updateScore();

        startLevel(levels[currentLevel]);
        game.setOnLevelComplete((levelFinishedEvent) -> {
            game.stopGame();
           loadNextLevel();
        });
    }

    private void updateScore(){
        score.setText("Score: "+scoreValue);
    }

    private void loadNextLevel() {
        currentLevel++;
        scoreValue += 100;
        updateScore();
        if (currentLevel < levels.length){
            startLevel(levels[currentLevel]);
        }else {
            currentLevel = 0;
            this.root.getChildren().remove(currentLoadedLevel);
            currentLoadedLevel = null;
            title.setVisible(true);
            pressSpace.setVisible(true);
        }
    }

    private void startLevel(String level) {
        try{
            if (currentLoadedLevel != null){
                this.root.getChildren().remove(currentLoadedLevel);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(level));
            Region root = loader.load();
            currentLoadedLevel = root;

            this.root.getChildren().add(0, root);
            game.load(this.root);
            //game.enableDebug();
            game.startGame();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML private void handleKeyPressed(KeyEvent event) {
        registerKeyPressed(event.getCode());
        if (keyIsPressed(KeyCode.SPACE) && currentLoadedLevel == null){
            startGame();
        }
    }

    @FXML private void handleKeyReleased(KeyEvent event) {
        registerKeyReleased(event.getCode());
    }
}
