package main;

import framework.PhysicsWorld;
import framework.events.CollisionEvent;
import framework.events.PhysicsEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

import static framework.PhysicsWorldFunctions.*;

public class GameController {

    @FXML private PhysicsWorld world;

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleKeyUp(KeyEvent keyEvent) {
        registerKeyReleased(keyEvent.getCode());
    }

    @FXML
    private void handleKeyDown(KeyEvent keyEvent) {
        registerKeyPressed(keyEvent.getCode());
    }

    @FXML
    private void handlePhysicsStep(PhysicsEvent physicsEvent) {

    }

    @FXML
    private void handleBeginCollision(CollisionEvent event) {

    }

    @FXML
    private void handleEndCollision(CollisionEvent event) {

    }
}