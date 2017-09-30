package main;

import framework.PhysicsWorld;
import framework.events.CollisionEvent;
import framework.events.PhysicsEvent;
import framework.nodes.PhysicsRectangle;
import framework.nodes.GeometricComposition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import static framework.PhysicsWorldFunctions.*;

public class JetpackController {

    @FXML private ProgressBar health;
    @FXML private PhysicsRectangle endZone;
    @FXML private PhysicsRectangle elevator;
    @FXML private PhysicsWorld world;
    @FXML private GeometricComposition jetman;
    @FXML private Polygon flame;
    @FXML private Rectangle fuelIndicator;

    private int fuel = 100;
    private int healthValue;
    private final int maxFuel = 100;
    private boolean gameOver;
    private double maxFuelIndicatorHeight;

    @FXML
    private void initialize(){
        flame.setVisible(false);
        maxFuelIndicatorHeight = fuelIndicator.getHeight();
        setHealth(100);
    }

    @FXML
    private void onBeginCollision(CollisionEvent collisionEvent) {
        handleCollision(collisionEvent.getObject1(), collisionEvent.getObject2());
        handleCollision(collisionEvent.getObject2(), collisionEvent.getObject1());
    }

    private void handleCollision(Node object1, Node object2) {
        if (object1 == jetman){
            if (hasStyle(object2, "spike")){
                setHealth(-19);
            }
            else if (hasStyle(object2, "ball")){
                setHealth(-51);
            }
        }
    }

    @FXML
    private void onEndCollision(CollisionEvent collisionEvent) {

    }

    @FXML
    private void onPhysicsStep(PhysicsEvent event) throws Exception {
        if (!gameOver){
            if (keyIsPressed(KeyCode.UP) && fuel >= 5){
                //Jetpack force upwards
                jetman.applyUpwardForceToCenterOfMass(0, -1500);
                setFuel(-5);
            }
            else if (fuel < maxFuel){
                setFuel(1);
            }

            if (keyIsPressed(KeyCode.LEFT)){
                jetman.applyUpwardForceToCenterOfMass(-500, 0);
                //Set jetman direction to force direction
                if (jetman.getScaleX() > 0){
                    jetman.setScaleX(-1);
                }

            }else if (keyIsPressed(KeyCode.RIGHT)){
                jetman.applyUpwardForceToCenterOfMass(500, 0);
                //Set jetman direction to force direction
                if (jetman.getScaleX() < 0){
                    jetman.setScaleX(1);
                }
            }

            double currentSpeed = getHighestVelocity(jetman);
            if (currentSpeed > 500){
                setVelocityToCurrentTravelVector(jetman, 500);
            }

            if (physicsNodesTouching(jetman, endZone)){
                gameOver = true;
                world.finishLevel(true, 0);
            }
            else if (healthValue < 0){
                world.finishLevel(false, 0);
                jetman.setFixedRotation(false);
                gameOver = true;
            }
        }

        //Elevator control
        if (elevator.getLayoutY() > 480){
            elevator.setLinearVelocityY(-100);
        }else if (elevator.getLayoutY() < 380){
            elevator.setLinearVelocityY(100);
        }
    }

    private void setFuel(int fuelChange) {
        fuel += fuelChange;
        double value = (fuel / (double) maxFuel) * maxFuelIndicatorHeight;
        fuelIndicator.setHeight(value);
        fuelIndicator.setTranslateY(maxFuelIndicatorHeight - value);

        if (fuelChange < 0){
            flame.setVisible(true);
        }else {
            flame.setVisible(false);
        }
    }

    private void setHealth(int healthChange) {
        healthValue += healthChange;
        health.setProgress(healthValue/100d);
    }
}
