package breakout.level1;

import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import framework.SimulationType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import shapes.PhysicsCircle;
import shapes.PhysicsRectangle;

import java.util.ArrayList;
import java.util.Collection;

import static framework.PhysicsWorldHelper.*;

public class Controller {

    public PhysicsRectangle paddle;
    public PhysicsCircle ball;
    public PhysicsWorld world;
    public PhysicsRectangle floor;
    public Pane brickContainer;
    public PhysicsRectangle wallLeft;
    public PhysicsRectangle wallRight;


    private boolean movePaddleLeft;
    private boolean movePaddleRight;

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
        //setOnTick
        world.addEventHandler(PhysicsEvent.PHYSICS_STEP, event -> {
            setPaddleSpeed(paddle);
        });

        //setOnCollision
        //Forutsigbar rekkef;lge
        world.addEventHandler(CollisionEvent.COLLISION, event -> {
            handleCollision(event.getObject1(), event.getObject2());
            handleCollision(event.getObject2(), event.getObject1());
        });
    }

    private void handleCollision(Node object1, Node object2) {
        if (object1 == ball){
            //if (brickContainer.getChildrenUnmodifiable().contains(object2)){
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

    private void setPaddleSpeed(PhysicsRectangle paddle) {
        if (pressedKeys.contains(KeyCode.LEFT) && !physicsNodesOverlap(wallLeft, paddle)){
            paddle.setSpeed(-800f, 0f);
        }else if (pressedKeys.contains(KeyCode.RIGHT) && !physicsNodesOverlap(wallRight, paddle)){ //Overlap sjekk
            paddle.setSpeed(800f, 0f);
        }else{
            paddle.setSpeed(0f, 0f);
        }
    }

    private Collection<KeyCode> pressedKeys = new ArrayList<>();

    public void handleKeyUp(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());
    }

    public void handleKeyDown(KeyEvent keyEvent) {
        if (!pressedKeys.contains(keyEvent.getCode())){
            pressedKeys.add(keyEvent.getCode());
        }
    }
}
