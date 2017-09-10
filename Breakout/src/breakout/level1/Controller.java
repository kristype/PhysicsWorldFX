package breakout.level1;

import framework.events.CollisionEvent;
import framework.events.PhysicsEvent;
import framework.PhysicsWorld;
import framework.SimulationType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import framework.nodes.PhysicsCircle;
import framework.nodes.PhysicsRectangle;

import static framework.PhysicsWorldFunctions.*;

public class Controller {

    @FXML private PhysicsRectangle paddle;
    @FXML private PhysicsCircle ball;
    @FXML private PhysicsWorld world;
    @FXML private PhysicsRectangle floor;
    @FXML private Pane brickContainer;
    @FXML private PhysicsRectangle wallLeft;
    @FXML private PhysicsRectangle wallRight;

    private boolean paddleIsResized = false;

    @FXML
    private void initialize() {

        double width = brickContainer.getPrefWidth() / 10;
        Color[] colors = new Color[] {Color.RED, Color.ORANGE, Color.YELLOW, Color.BROWN};
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 10; i++) {
                double height = 20;
                PhysicsRectangle brick = new PhysicsRectangle();
                brick.getStyleClass().add("brick");
                brick.setBodyType(SimulationType.NonMovable);
                brick.setStroke(Color.BLACK);
                brick.setFill(colors[j]);
                brick.setLayoutX(i*width);
                brick.setLayoutY(j*height);
                brick.setWidth(width);
                brick.setHeight(height);
                brickContainer.getChildren().add(brick);
            }
        }
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
        setPaddleSpeed(paddle);
    }

    private void setPaddleSpeed(PhysicsRectangle paddle) {
        if (keyIsPressed(KeyCode.LEFT) && !physicsNodesTouching(wallLeft, paddle)){
            paddle.setLinearVelocityX(-800);
        }else if (keyIsPressed(KeyCode.RIGHT) && !physicsNodesTouching(wallRight, paddle)){
            paddle.setLinearVelocityX(800);
        }else{
            paddle.setLinearVelocityX(0);
        }
    }

    @FXML
    private void handleCollision(CollisionEvent collisionEvent) {
        handleCollision(collisionEvent.getObject1(), collisionEvent.getObject2());
        handleCollision(collisionEvent.getObject2(), collisionEvent.getObject1());
    }

    private void handleCollision(Node object1, Node object2) {
        if (object1 == ball){
            if (hasStyle(object2, "brick")){
                world.remove(object2);
                brickContainer.getChildren().remove(object2);

                if (!paddleIsResized && ((PhysicsRectangle)object2).getFill() == Color.RED){
                    paddle.setWidth(paddle.getWidth()/2);
                    paddleIsResized = true;
                }
            }
        }
    }
}
