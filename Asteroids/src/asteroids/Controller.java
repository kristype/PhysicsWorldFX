package asteroids;

import bodies.ShapeComposition;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;
import shapes.PhysicsPolygon;

import static framework.PhysicsWorldHelper.*;

public class Controller {

    public static final int singleThrustForce = 50;
    public static final int fullThrustForce = 200;

    @FXML private Polygon rightFlame;
    @FXML private Polygon leftFlame;

    @FXML private PhysicsWorld world;
    @FXML private ShapeComposition lander;
    @FXML private PhysicsPolygon leftThruster;
    @FXML private PhysicsPolygon rightThruster;
    @FXML private PhysicsPolygon landerBody;

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
    private void handleStep(PhysicsEvent physicsEvent) {
        if (keyIsPressed(KeyCode.RIGHT)){
            leftThruster.applyForceUp(0, singleThrustForce);
            leftFlame.setVisible(true);
            //rightFlame.setVisible(false);
        }
        else if (keyIsPressed(KeyCode.LEFT)){
            rightThruster.applyForceUp(0, singleThrustForce);
            rightFlame.setVisible(true);
            leftFlame.setVisible(false);
        }
        else if (keyIsPressed(KeyCode.UP)){
            leftThruster.applyForceUp(0, fullThrustForce);
            rightThruster.applyForceUp(0, fullThrustForce);
            //leftFlame.setVisible(true);
            rightFlame.setVisible(true);
        }
        else {
            leftFlame.setVisible(false);
            rightFlame.setVisible(false);
        }
    }
}
