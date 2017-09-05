package lunar;

import framework.PhysicsWorld;
import framework.nodes.PhysicsPolygon;
import framework.nodes.PhysicsRectangle;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;

import static framework.PhysicsWorldFunctions.*;

public class GameController {

    public static final int singleThrustForce = -80;
    public static final int fullThrustForce = -200;
    @FXML private PhysicsRectangle landingPad;

    @FXML private Polygon rightFlame;
    @FXML private Polygon leftFlame;

    @FXML private PhysicsWorld world;
    @FXML private PhysicsPolygon leftThruster;
    @FXML private PhysicsPolygon rightThruster;

    @FXML
    private void handleKeyUp(KeyEvent keyEvent) {
        registerKeyReleased(keyEvent.getCode());
    }

    @FXML
    private void handleKeyDown(KeyEvent keyEvent) {
        registerKeyPressed(keyEvent.getCode());
    }

    @FXML
    private void handleStep() {
        if (keyIsPressed(KeyCode.RIGHT)){
            leftThruster.applyForceUp(0, singleThrustForce);
            leftFlame.setVisible(true);
            rightFlame.setVisible(false);
        }
        else if (keyIsPressed(KeyCode.LEFT)){
            rightThruster.applyForceUp(0, singleThrustForce);
            rightFlame.setVisible(true);
            leftFlame.setVisible(false);
        }
        else if (keyIsPressed(KeyCode.UP)){
            leftThruster.applyForceUp(0, fullThrustForce);
            rightThruster.applyForceUp(0, fullThrustForce);
            leftFlame.setVisible(true);
            rightFlame.setVisible(true);
        }
        else {
            leftFlame.setVisible(false);
            rightFlame.setVisible(false);
        }

        if (physicsNodesTouching(leftThruster, landingPad) && physicsNodesTouching(rightThruster, landingPad)){
            world.endLevel();
        }
    }
}
