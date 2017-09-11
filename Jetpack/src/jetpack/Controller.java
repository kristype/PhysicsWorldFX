package jetpack;

import framework.PhysicsWorld;
import framework.events.CollisionEvent;
import framework.events.PhysicsEvent;
import framework.nodes.PhysicsPolyline;
import framework.nodes.PhysicsRectangle;
import framework.nodes.ShapeComposition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import static framework.PhysicsWorldFunctions.*;

public class Controller {

    @FXML private PhysicsRectangle endZone;
    @FXML private PhysicsRectangle elevator;
    @FXML private PhysicsWorld world;
    @FXML private ShapeComposition jetman;
    @FXML private PhysicsPolyline boundingBox;
    @FXML private Polygon flame;
    @FXML private Rectangle fuelIndicator;

    private int fuel = 100;
    private final int maxFuel = 100;
    private boolean gameOver;
    private double maxFuelIndicatorHeight;

    @FXML
    private void initialize(){
        maxFuelIndicatorHeight = fuelIndicator.getHeight();
    }

    @FXML private void onBeginCollision(CollisionEvent collisionEvent) {
        handleCollision(collisionEvent.getObject1(), collisionEvent.getObject2());
        handleCollision(collisionEvent.getObject2(), collisionEvent.getObject1());
    }

    private void handleCollision(Node object1, Node object2) {
        if (object1 == jetman){
            if (hasStyle(object2, "spike")){
                world.finishLevel(false, 0);
                jetman.setFixedRotation(false);
                gameOver = true;
            }
        }
    }

    @FXML private void onEndCollision(CollisionEvent collisionEvent) {

    }

    @FXML private void onPhysicsStep(PhysicsEvent event) throws Exception {
        if (!gameOver){
            if (keyIsPressed(KeyCode.UP) && fuel >= 5){
                Point2D centerOfMass = getLocalCenterOfMass(jetman);
                Point2D rotatedPoint = getRotatedPoint(jetman, centerOfMass.getX(), centerOfMass.getY());
                jetman.applyForceUpToPoint(rotatedPoint.getX(), rotatedPoint.getY(), 0, -1500);
                fuel -= 5;
                setFuelIndicator();
                flame.setVisible(true);
            }
            else{
                if (fuel < maxFuel){
                    fuel += 1;
                    setFuelIndicator();
                }
                flame.setVisible(false);
            }

            if (keyIsPressed(KeyCode.LEFT)){
                Point2D centerOfMass = getLocalCenterOfMass(jetman);
                Point2D rotatedPoint = getRotatedPoint(jetman, centerOfMass.getX(), centerOfMass.getY());
                jetman.applyForceUpToPoint(rotatedPoint.getX(), rotatedPoint.getY(), -500, -0);
                if (jetman.getScaleX() > 0){
                    jetman.setScaleX(-1);
                }
            }else if (keyIsPressed(KeyCode.RIGHT)){
                Point2D centerOfMass = getLocalCenterOfMass(jetman);
                Point2D rotatedPoint = getRotatedPoint(jetman, centerOfMass.getX(), centerOfMass.getY());
                jetman.applyForceUpToPoint(rotatedPoint.getX(), rotatedPoint.getY(), 500, -0);
                if (jetman.getScaleX() < 0){
                    jetman.setScaleX(1);
                }
            }

            double absVelocityX = Math.abs(jetman.getLinearVelocityX());
            if (absVelocityX > 500){
                jetman.setLinearVelocityX((jetman.getLinearVelocityX() / absVelocityX) * 500);
            }

            double absVelocityY = Math.abs(jetman.getLinearVelocityY());
            if (absVelocityY > 500){
                jetman.setLinearVelocityY((jetman.getLinearVelocityY() / absVelocityY) * 500);
            }

            if (physicsNodesTouching(endZone, jetman)){
                gameOver = true;
            }
        }

        if (elevator.getLayoutY() > 480){
            elevator.setLinearVelocityY(-100);
        }else if (elevator.getLayoutY() < 380){
            elevator.setLinearVelocityY(100);
        }
    }

    private void setFuelIndicator() {
        double value = (fuel / (double) maxFuel) * maxFuelIndicatorHeight;
        fuelIndicator.setHeight(value);
        fuelIndicator.setTranslateY(maxFuelIndicatorHeight - value);
    }

    @FXML private void onKeyPressed(KeyEvent event) {
        registerKeyPressed(event.getCode());
    }

    @FXML private void onKeyReleased(KeyEvent event) {
        registerKeyReleased(event.getCode());
    }
}
