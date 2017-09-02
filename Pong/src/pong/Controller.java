package pong;

import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import framework.nodes.PhysicsCircle;
import framework.nodes.PhysicsRectangle;

import static framework.PhysicsWorldFunctions.*;

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

    private int leftScore = 0;
    private int rightScore = 0;

    @FXML
    private void initialize() {

        paddle1.setLayoutY((world.getPrefHeight() / 2)-(paddle1.getHeight()/2));
        paddle1.setLayoutX(wallLeft.getLayoutX() + wallLeft.getWidth() + 40);

        paddle2.setLayoutY((world.getPrefHeight() / 2)-(paddle2.getHeight()/2));
        paddle2.setLayoutX(wallRight.getLayoutX() - wallRight.getWidth() - 40);

        updateScore();
    }

    private void updateScore() {
        score.setText("Left: "+ leftScore + " Right: " +rightScore);
    }

    private void setPaddleSpeed(PhysicsRectangle paddle, boolean movePaddleUp, boolean movePaddleDown) {
        if (movePaddleUp && paddle.getLayoutY() > wallLeft.getWidth()){
            paddle.setLinearVelocityY(50);
        }else if (movePaddleDown && (paddle.getLayoutY() + paddle.getHeight()) < wallLeft.getHeight()-wallLeft.getWidth()){
            paddle.setLinearVelocityY(-50);
        }else{
            paddle.setLinearVelocityY(0);
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
    private void handlePhysicsStep(PhysicsEvent physicsEvent) {
        setPaddleSpeed(paddle1, keyIsPressed(KeyCode.A), keyIsPressed(KeyCode.Z));
        setPaddleSpeed(paddle2, keyIsPressed(KeyCode.K), keyIsPressed(KeyCode.M));
    }

    @FXML
    private void handleCollision(CollisionEvent event) {
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
    }
}
