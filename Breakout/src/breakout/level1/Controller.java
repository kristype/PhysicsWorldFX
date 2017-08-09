package breakout.level1;

import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import framework.SimulationType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import shapes.PhysicsCircle;
import shapes.PhysicsRectangle;

public class Controller {

    public PhysicsRectangle paddle;
    public PhysicsCircle ball;
    public PhysicsWorld world;
    public PhysicsRectangle floor;
    public Pane brickContainer;

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

        world.addEventHandler(PhysicsEvent.PHYSICS_STEP, event -> {
            setPaddleSpeed(paddle);
        });

        world.addEventHandler(CollisionEvent.COLLISION, event -> {
            handleCollision(event.getObject1(), event.getObject2());
            handleCollision(event.getObject2(), event.getObject1());
        });
    }

    private void handleCollision(Node object1, Node object2) {
        if (object1 == ball){
            if (brickContainer.getChildrenUnmodifiable().contains(object2)){
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
        if (movePaddleLeft && paddle.getLayoutX() > 11){
            paddle.setSpeed(-80f, 0f);
        }else if (movePaddleRight && (paddle.getLayoutX() + paddle.getWidth()) < 629){
            paddle.setSpeed(80f, 0f);
        }else{
            paddle.setSpeed(0f, 0f);
        }
    }

    public void handleKeyUp(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case A:
                movePaddleLeft = false;
                break;
            case D:
                movePaddleRight = false;
                break;
        }
    }

    public void handleKeyDown(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case A:
                movePaddleLeft = true;
                break;
            case D:
                movePaddleRight = true;
                break;
            case SPACE:
                ball.setSpeed(25.0f, 25.0f);
                break;
        }
    }
}
