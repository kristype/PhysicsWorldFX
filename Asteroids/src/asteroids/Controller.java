package asteroids;

import framework.CollisionEvent;
import framework.PhysicsEvent;
import framework.PhysicsWorld;
import framework.nodes.PhysicsCircle;
import framework.nodes.PhysicsPolygon;
import framework.nodes.ShapeComposition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;

import static framework.PhysicsWorldFunctions.*;

public class Controller {

    private final int minX = -30;
    private final int maxX = 830;
    private final int minY = -30;
    private final int maxY = 630;
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

    @FXML
    private void handleCollisionEnded(CollisionEvent collisionEvent) {
        handleAsteroidCollision(collisionEvent.getObject1(), collisionEvent.getObject2());
        handleAsteroidCollision(collisionEvent.getObject2(), collisionEvent.getObject1());
    }

    private void handleAsteroidCollision(Node object1, Node object2) {
        if (hasStyle(object1, "bullet") && hasStyle(object2, "asteroid")){
            removeObject(object1);
            removeObject(object2);
            if (hasStyle(object2, "medium")){
                spawnAsteroid(object1, (PhysicsPolygon) object2, "small", 0.5, -30);
                spawnAsteroid(object1, (PhysicsPolygon) object2, "small", 0.5, 30);
            }

         }
    }

    private void checkPlayerCollision(Node object1, Node object2) {
        if (object1 == playerShip && (hasStyle(object2, "asteroid"))){
            gameOver = true;
            shipBody.setFill(Color.DARKRED);
            rightWing.setFill(Color.DARKRED);
            leftWing.setFill(Color.DARKRED);
        }
    }

    private void spawnAsteroid(Node bullet, PhysicsPolygon asteroid, String newTypes, double scale, double angleOffset) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/asteroid1.fxml"));
            PhysicsPolygon newAsteroid = loader.load();

            newAsteroid.getStyleClass().add(newTypes);
            newAsteroid.setScaleX(scale);
            newAsteroid.setScaleY(scale);

            newAsteroid.setLinearVelocityX(asteroid.getLinearVelocityX());
            newAsteroid.setLinearVelocityY(asteroid.getLinearVelocityY());
            newAsteroid.setAngularVelocity(asteroid.getAngularVelocity());

            Point2D position = getRotatedLayoutPosition(asteroid, 0, 0, angleOffset);
            newAsteroid.setLayoutX(position.getX());
            newAsteroid.setLayoutY(position.getY());


            physicsWorld.getChildren().add(newAsteroid);
            physicsWorld.add(newAsteroid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeObject(Node object1) {
        physicsWorld.remove(object1);
        physicsWorld.getChildren().remove(object1);
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
            if (hasStyle(node, "asteroid")){
                PhysicsPolygon physicsPolygon = (PhysicsPolygon) node;
                double velocityX = physicsPolygon.getLinearVelocityX();
                double velocityY = physicsPolygon.getLinearVelocityY();
                teleportIfOutOfBounds(node, velocityX, velocityY);
            }
            else if (node != playerShip){
                deleteOutOfBounds(node);
            }
        }
        double velocityX = playerShip.getLinearVelocityX();
        double velocityY = playerShip.getLinearVelocityY();
        teleportIfOutOfBounds(playerShip, velocityX, velocityY);
    }

    private void teleportIfOutOfBounds(Node node, double velocityX, double velocityY) {
        if (node.getLayoutX() < minX && velocityX < 0){
            node.setLayoutX(860);
        }
        if (node.getLayoutX() > maxX && velocityX > 0){
            node.setLayoutX(-60);
        }
        if (node.getLayoutY() < minY && velocityY < 0){
            node.setLayoutY(660);
        }
        if (node.getLayoutY() > maxY && velocityY > 0){
            node.setLayoutY(-60);
        }
    }

    private void deleteOutOfBounds(Node node) {
        if (node.getLayoutX() < minX || node.getLayoutX() > maxX || node.getLayoutY() < minY || node.getLayoutY() > maxY){
            physicsWorld.remove(node);
        }
    }

    private void shoot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/bullet.fxml"));
            PhysicsCircle bullet = loader.load();
            Point2D point = getVectorForDegrees(playerShip.getRotate(), 400);
            bullet.setLinearVelocityX(point.getX() + playerShip.getLinearVelocityX());
            bullet.setLinearVelocityY(point.getY() + playerShip.getLinearVelocityY());

            Point2D spawnPoint = getRotatedLayoutPosition(playerShip, 15, -10);
            bullet.setLayoutX(spawnPoint.getX());
            bullet.setLayoutY(spawnPoint.getY());

            physicsWorld.getChildren().add(bullet);
            physicsWorld.add(bullet);
        } catch (IOException e) {
            e.printStackTrace();
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
