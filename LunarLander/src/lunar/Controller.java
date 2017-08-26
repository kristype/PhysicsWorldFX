package lunar;

import bodies.ShapeComposition;
import com.sun.javafx.geom.Point2D;
import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import shapes.PhysicsPolygon;

import static framework.PhysicsWorldHelper.*;

public class Controller {

    public static final int singleThrust = 100;
    public static final int fullThrustForce = 400;

    @FXML private PhysicsWorld world;
    @FXML private ShapeComposition triangleContainer;
    @FXML private PhysicsPolygon leftThruster;
    @FXML private PhysicsPolygon rightThruster;
    @FXML private PhysicsPolygon landerBody;

    @FXML
    private void initialize() {
        world.addEventHandler(PhysicsEvent.PHYSICS_STEP, event -> {
            if (KeyIsDown(KeyCode.RIGHT)){
                Point2D vector = getVectorForDegrees(triangleContainer.getRotate(), singleThrust);
                leftThruster.applyForce(vector.x, vector.y);
            }
            else if (KeyIsDown(KeyCode.LEFT)){
                Point2D vector = getVectorForDegrees(triangleContainer.getRotate(), singleThrust);
                rightThruster.applyForce(vector.x, vector.y);
            }
            else if (KeyIsDown(KeyCode.UP)){
                Point2D vector = getVectorForDegrees(triangleContainer.getRotate(), fullThrustForce);
                leftThruster.applyForce(vector.x, vector.y);
                rightThruster.applyForce(vector.x, vector.y);
            }
        });

        world.addEventHandler(CollisionEvent.COLLISION, event -> {

        });
    }

    @FXML
    private void handleKeyUp(KeyEvent keyEvent) {
        RegisterKeyUp(keyEvent.getCode());
    }

    @FXML private void handleKeyDown(KeyEvent keyEvent) {
        registerKeyDown(keyEvent.getCode());
    }
}
