package jetpack;

import framework.PhysicsWorld;
import framework.events.CollisionEvent;
import framework.events.PhysicsEvent;
import framework.nodes.PhysicsPolyline;
import framework.nodes.ShapeComposition;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;

import static framework.PhysicsWorldFunctions.*;

public class Controller {
    public PhysicsWorld world;
    public ShapeComposition jetman;
    public PhysicsPolyline boundingBox;
    public Polygon flame;

    public void onBeginCollision(CollisionEvent collisionEvent) {
        if (collisionEvent.getObject1() == jetman){
            if (hasStyle(collisionEvent.getObject2(), "spike")){
                world.finishLevel(false, 0);
            }
        }
    }

    public void onEndCollision(CollisionEvent collisionEvent) {

    }

    public void onPhysicsStep(PhysicsEvent physicsEvent) throws Exception {
        if (keyIsPressed(KeyCode.UP)){
            Point2D centerOfMass = getLocalCenterOfMass(jetman);
            Point2D rotatedPoint = getRotatedPoint(jetman, centerOfMass.getX(), centerOfMass.getY());
            jetman.applyForceUpToPoint(rotatedPoint.getX(), rotatedPoint.getY(), 0, -2000);
            flame.setVisible(true);
        }
        if (keyIsPressed(KeyCode.LEFT)){
            Point2D centerOfMass = getLocalCenterOfMass(jetman);
            Point2D rotatedPoint = getRotatedPoint(jetman, centerOfMass.getX(), centerOfMass.getY());
            jetman.applyForceUpToPoint(rotatedPoint.getX(), rotatedPoint.getY(), -200, -0);
            flame.setVisible(true);
        }
        if (keyIsPressed(KeyCode.RIGHT)){
            Point2D centerOfMass = getLocalCenterOfMass(jetman);
            Point2D rotatedPoint = getRotatedPoint(jetman, centerOfMass.getX(), centerOfMass.getY());
            jetman.applyForceUpToPoint(rotatedPoint.getX(), rotatedPoint.getY(), 200, -0);
            flame.setVisible(true);
        }
        else {
            flame.setVisible(false);
        }

        double rotate = jetman.getRotate();
        double abs = Math.abs(rotate);
        if (abs > 5){
            jetman.setAngularVelocity(0);
            jetman.setRotate(rotate/abs * 5);
        }
    }



    public void onKeyPressed(KeyEvent event) {
        registerKeyPressed(event.getCode());
    }

    public void onKeyReleased(KeyEvent event) {
        registerKeyReleased(event.getCode());
    }
}
