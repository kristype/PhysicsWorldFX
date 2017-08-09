package lunar;

import bodies.ShapeContainer;
import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import shapes.PhysicsCircle;
import shapes.PhysicsRectangle;

public class Controller {

    public ShapeContainer triangleContainer;

    @FXML
    private void initialize() {
    }

    public void handleKeyUp(KeyEvent keyEvent) {
        switch (keyEvent.getCode()){
            case UP:

                triangleContainer.setSpeed(0, 50);
                break;
            case LEFT:
                triangleContainer.setRotationSpeed(10);
                break;

            case RIGHT:
                triangleContainer.setRotationSpeed(-10);
                break;
            default:
                break;
        }
    }

    public void handleKeyDown(KeyEvent keyEvent) {

    }


}
