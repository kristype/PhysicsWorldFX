package pong;

import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import shapes.PhysicsCircle;
import shapes.PhysicsRectangle;

public class Controller {

    @FXML private PhysicsRectangle paddle1;
    @FXML private PhysicsRectangle paddle2;
    @FXML private PhysicsRectangle wallLeft;
    @FXML private PhysicsRectangle wallRight;
    @FXML private PhysicsCircle ball;
    @FXML private PhysicsWorld world;
    @FXML private PhysicsRectangle roof;
    @FXML private PhysicsRectangle floor;
    @FXML private Label score;

    private boolean movePaddle1Up;
    private boolean movePaddle2Up;
    private boolean movePaddle1Down;
    private boolean movePaddle2Down;

    private int leftScore = 0;
    private int rightScore = 0;

    @FXML
    private void initialize() {

        paddle1.setLayoutY((world.getPrefHeight() / 2)-(paddle1.getHeight()/2));
        paddle1.setLayoutX(wallLeft.getLayoutX() + wallLeft.getWidth() + 40);

        paddle2.setLayoutY((world.getPrefHeight() / 2)-(paddle2.getHeight()/2));
        paddle2.setLayoutX(wallRight.getLayoutX() - wallRight.getWidth() - 40);

        world.addEventHandler(PhysicsEvent.PHYSICS_STEP, event -> {
            setPaddleSpeed(paddle1, movePaddle1Up, movePaddle1Down);
            setPaddleSpeed(paddle2, movePaddle2Up, movePaddle2Down);
        });

        world.addEventHandler(CollisionEvent.COLLISION, event -> {
            if (event.getObject1() == ball || event.getObject2() == ball){
                if (event.getObject1() == wallLeft || event.getObject2() == wallRight){
                    rightScore += 1;
                    updateScore();
                }
                else if (event.getObject1() == wallRight || event.getObject2() == wallRight){
                    leftScore += 1;
                    updateScore();
                }
            }
        });

        updateScore();
    }

    private void updateScore() {
        score.setText("Left: "+ leftScore + " Right: " +rightScore);
    }

    private void setPaddleSpeed(PhysicsRectangle paddle, boolean movePaddleUp, boolean movePaddleDown) {
        if (movePaddleUp && paddle.getLayoutY() > wallLeft.getWidth()){
            paddle.setSpeed(0f, 50f);
        }else if (movePaddleDown && (paddle.getLayoutY() + paddle.getHeight()) < wallLeft.getHeight()-wallLeft.getWidth())
        {
            paddle.setSpeed(0f, -50f);
        }else{
            paddle.setSpeed(0f, 0f);
        }
    }

    @FXML
    private void handleKeyUp(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case A:
                movePaddle1Up = false;
                break;
            case Z:
                movePaddle1Down = false;
                break;
            case K:
                movePaddle2Up = false;
                break;
            case M:
                movePaddle2Down = false;
                break;
        }
    }

    @FXML
    private void handleKeyDown(KeyEvent keyEvent) {
        switch(keyEvent.getCode()) {
            case A:
                movePaddle1Up = true;
                break;
            case Z:
                movePaddle1Down = true;
                break;
            case K:
                movePaddle2Up = true;
                break;
            case M:
                movePaddle2Down = true;
                break;
            case SPACE:
                ball.setSpeed(25.0f, 25.0f);
                break;
        }
    }
}
