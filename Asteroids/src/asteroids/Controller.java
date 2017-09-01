package asteroids;

import bodies.ShapeComposition;
import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import shapes.PhysicsCircle;
import shapes.PhysicsPolygon;

import static framework.PhysicsWorldHelper.*;

public class Controller {

    public Polygon flame;
    @FXML private PhysicsPolygon rightWing;
    @FXML private PhysicsPolygon leftWing;
    @FXML private PhysicsPolygon shipBody;
    @FXML private ShapeComposition playerShip;
    @FXML private PhysicsWorld physicsWorld;
    private boolean gameOver;
    private int stepsSinceLastBullet;

    @FXML
    private void handleCollision(CollisionEvent collisionEvent) {
        checkPlayerCollision(collisionEvent.getObject1(), collisionEvent.getObject2());
        checkPlayerCollision(collisionEvent.getObject2(), collisionEvent.getObject1());
    }

    private void checkPlayerCollision(Node object1, Node object2) {
        if (object1 == playerShip && (hasStyle(object2, "asteroid") || hasStyle(object2, "bullet"))){
            gameOver = true;
            shipBody.setFill(Color.DARKRED);
            rightWing.setFill(Color.DARKRED);
            leftWing.setFill(Color.DARKRED);
        }
    }

    @FXML
    private  void handlePhysicsStep(PhysicsEvent physicsEvent) {
        if (!gameOver){
            if (keyIsPressed(KeyCode.UP)){
                shipBody.applyForceUp(0, 500);
                flame.setVisible(true);
            }
            else{
                flame.setVisible(false);
            }

            if (keyIsPressed(KeyCode.LEFT)){
                playerShip.setAngularVelocity(100);
            }else if (keyIsPressed(KeyCode.RIGHT)){
                playerShip.setAngularVelocity(-100);
            }else {
                playerShip.setAngularVelocity(0);
            }

            if (keyIsPressed(KeyCode.SPACE) && stepsSinceLastBullet > 5){
                stepsSinceLastBullet = 0;
                shoot();
            }

            try{
                stepsSinceLastBullet++;
            }
            catch (StackOverflowError e){
                stepsSinceLastBullet = 0;
            }
        }
        else {
            flame.setVisible(false);
        }

        for (Node node : physicsWorld.getChildrenUnmodifiable()) {

        }
    }

    private void shoot() {
        PhysicsCircle circle = new PhysicsCircle();
        circle.setDensity(0.5);
        circle.setRadius(2);
        circle.setBullet(true);
        circle.getStyleClass().add("bullet");
        Point2D point = getVectorForDegrees(playerShip.getRotate(), 300);
        circle.setLinearVelocityX(point.getX());
        circle.setLinearVelocityY(point.getY());

        Point2D spawnPoint = getRotatedLayoutPosition(playerShip, 15, -10);
        circle.setLayoutX(spawnPoint.getX());
        circle.setLayoutY(spawnPoint.getY());

        physicsWorld.getChildren().add(circle);
        physicsWorld.add(circle);
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
