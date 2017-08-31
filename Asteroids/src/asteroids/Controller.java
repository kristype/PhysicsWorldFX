package asteroids;

import framework.CollisionEvent;
import framework.PhysicsEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;
import shapes.PhysicsPolygon;

import static framework.PhysicsWorldHelper.*;

public class Controller {

    public Polygon flame;
    @FXML private PhysicsPolygon playerShip;
    private boolean gameOver;

    @FXML
    private void handleCollision(CollisionEvent collisionEvent) {
        if (collisionEvent.getObject1() == playerShip && collisionEvent.getObject2().getStyleClass().contains("asteroid")){
            gameOver = true;
        }
    }

    @FXML
    private  void handlePhysicsStep(PhysicsEvent physicsEvent) {
        if (!gameOver){
            if (keyIsPressed(KeyCode.UP)){
                playerShip.applyForceUp(0, 100);
                flame.setVisible(true);
            }
            else{
                flame.setVisible(false);
            }
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event){
        registerKeyPressed(event.getCode());
    }

    @FXML
    private void handleKeyUp(KeyEvent event){
        registerKeyReleased(event.getCode());
    }
}
