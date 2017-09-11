package asteroids;

import framework.events.CollisionEvent;
import framework.PhysicsWorld;
import framework.nodes.PhysicsCircle;
import framework.nodes.PhysicsPolygon;
import framework.nodes.ShapeComposition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static framework.PhysicsWorldFunctions.*;

public class Controller {

    private final int minX = -30;
    private final int maxX = 805;
    private final int minY = -30;
    private final int maxY = 605;

    private final Random random = new Random();

    public Polygon flame;
    @FXML
    private PhysicsPolygon rightWing;
    @FXML
    private PhysicsPolygon leftWing;
    @FXML
    private PhysicsPolygon shipBody;
    @FXML
    private ShapeComposition playerShip;
    @FXML
    private PhysicsWorld physicsWorld;
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
        if (hasStyle(object1, "bullet") && hasStyle(object2, "asteroid")) {
            removeObject(object1);
            removeObject(object2);
            if (hasStyle(object2, "large")){
                spawnAsteroid((PhysicsPolygon) object2, "medium", 1, 20, 0,0, 0);
                spawnAsteroid((PhysicsPolygon) object2, "medium", 1, 20,0,20, 0);
                spawnAsteroid((PhysicsPolygon) object2, "medium", 1, -20, 0,0, 20);
                spawnAsteroid((PhysicsPolygon) object2, "medium", 1, -20,0,20, 20);
            }

            if (hasStyle(object2, "medium")) {
                spawnAsteroid((PhysicsPolygon) object2, "small", 0.5, 30, 0, 10, 0);
                spawnAsteroid((PhysicsPolygon) object2, "small", 0.5, -30, 0, 10, 10);
            }
        }
    }

    private void checkPlayerCollision(Node object1, Node object2) {
        if (object1 == playerShip && (hasStyle(object2, "asteroid"))) {
            if (!gameOver){
                physicsWorld.finishLevel(false,0);
                gameOver = true;
                shipBody.setFill(Color.DARKRED);
                rightWing.setFill(Color.DARKRED);
                leftWing.setFill(Color.DARKRED);
            }
        }
    }

    private void spawnAsteroid(PhysicsPolygon asteroid, String newTypes, double scale, double velocityAngleOffset, double spanwPointAngleOffset, int spawnOffset, int spawnOffsetY) {
        try {
            int randomNumber = random.nextInt(3) + 1;
            PhysicsPolygon newAsteroid = loadFxmlResource(getClass(), "resources/asteroid"+randomNumber+".fxml");

            newAsteroid.getStyleClass().add(newTypes);
            newAsteroid.setScaleX(scale);
            newAsteroid.setScaleY(scale);

            Point2D travelVector = getOffsetSpeedVector(asteroid, velocityAngleOffset);
            newAsteroid.setLinearVelocityX(travelVector.getX());
            newAsteroid.setLinearVelocityY(travelVector.getY());
            newAsteroid.setAngularVelocity(asteroid.getAngularVelocity());

            Point2D position = getRotatedLayoutPosition(asteroid, spawnOffset, spawnOffsetY, spanwPointAngleOffset);
            newAsteroid.setLayoutX(position.getX());
            newAsteroid.setLayoutY(position.getY());


            physicsWorld.getChildren().add(newAsteroid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeObject(Node object1) {
        physicsWorld.getChildren().remove(object1);
    }

    @FXML
    private void handlePhysicsStep() {
        if (!gameOver) {

            //Accelerate space ship
            if (keyIsPressed(KeyCode.UP)) {
                shipBody.applyForceUpToCenter(0, -1000);
                flame.setVisible(true);
            } else {
                flame.setVisible(false);
            }

            //Max speed
            double currentSpeed = getCurrentSpeed(playerShip);
            if (currentSpeed > 400) {
                setSpeedToCurrentSpeedVector(playerShip, 400);
            }


            //Rotate on left/right, stop when no key is down
            if (keyIsPressed(KeyCode.LEFT)) {
                playerShip.setAngularVelocity(120);
            } else if (keyIsPressed(KeyCode.RIGHT)) {
                playerShip.setAngularVelocity(-120);
            } else if (playerShip.getAngularVelocity() != 0) {
                playerShip.setAngularVelocity(0);
            }


            //Shoot bullet on space, but don't shoot on every step
            if (keyIsPressed(KeyCode.SPACE) && stepsSinceLastBullet > 5) {
                stepsSinceLastBullet = 0;
                shoot();
            }

            try {
                stepsSinceLastBullet++;
            } catch (StackOverflowError e) {
                stepsSinceLastBullet = 0;
            }
        } else {
            flame.setVisible(false);
        }


        for (Node node : new ArrayList<>(physicsWorld.getChildrenUnmodifiable())) {
            if (hasStyle(node, "asteroid")) {
                PhysicsPolygon physicsPolygon = (PhysicsPolygon) node;
                double velocityX = physicsPolygon.getLinearVelocityX();
                double velocityY = physicsPolygon.getLinearVelocityY();
                teleportIfOutOfBounds(node, velocityX, velocityY);
            } else if (node != playerShip) {
                deleteOutOfBounds(node);
            }
        }
        double velocityX = playerShip.getLinearVelocityX();
        double velocityY = playerShip.getLinearVelocityY();
        teleportIfOutOfBounds(playerShip, velocityX, velocityY);
    }

    private void teleportIfOutOfBounds(Node node, double velocityX, double velocityY) {
        Bounds bounds = node.getBoundsInParent();
        if (bounds.getMinX() < minX && velocityX < 0) {
            node.setLayoutX(maxX + 25);
        } else if (bounds.getMinX() > maxX && velocityX > 0) {
            node.setLayoutX(minX - 25);
        }
        if (bounds.getMinY() < minY && velocityY < 0) {
            node.setLayoutY(maxY + 25);
        } else if (bounds.getMinY() > maxY && velocityY > 0) {
            node.setLayoutY(minY - 25);
        }
    }

    private void deleteOutOfBounds(Node node) {
        if (node.getLayoutX() < minX || node.getLayoutX() > maxX || node.getLayoutY() < minY || node.getLayoutY() > maxY) {
            physicsWorld.getChildren().remove(node);
        }
    }

    private void shoot() {
        try {
            PhysicsCircle bullet = loadFxmlResource(getClass(), "resources/bullet.fxml");
            Point2D point = getVectorForDegrees(playerShip.getRotate(), 600);
            bullet.setLinearVelocityX(point.getX() + playerShip.getLinearVelocityX());
            bullet.setLinearVelocityY(point.getY() + playerShip.getLinearVelocityY());

            Point2D spawnPoint = getRotatedLayoutPosition(playerShip, 16, -5);
            bullet.setLayoutX(spawnPoint.getX());
            bullet.setLayoutY(spawnPoint.getY());

            physicsWorld.getChildren().add(bullet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}