package main;

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

    public Pane gameContainer;
    public Pane titleContainer;
    public Label title;
    public Label pressSpace;
    private PhysicsGame game;

    private void startGame() {
        gameContainer.getChildren().clear();
        titleContainer.setVisible(false);
        if (game != null){
            game.stopGame();
        }

        game = new PhysicsGame();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jetpack.fxml"));
            Region asteroids = loader.load();
            gameContainer.getChildren().add(asteroids);

            game.load(gameContainer);
            game.startGame();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        game.setOnLevelFailed(e -> showMenu());
    }

    private void showMenu() {
        titleContainer.setVisible(true);
        title.setText("Game Over!");
        pressSpace.setText("Press space to restart the game");
    }

    @FXML private void handleKeyPressed(KeyEvent event) {
        registerKeyPressed(event.getCode());
        if (keyIsPressed(KeyCode.SPACE) && titleContainer.isVisible()){
            startGame();
        }
        if (keyIsPressed(KeyCode.TAB) ) {
            if (game.isDebugEnabled()) {
                game.disableDebug();
            }
        }
    }

    @FXML
    private void handleKeyReleased(KeyEvent event) {
        registerKeyReleased(event.getCode());
    }
}
