package shapes;

import bodies.BodyPropertiesOwner;
import framework.ChangedEventListener;
import javafx.geometry.Point2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.PhysicsShapeHelper;

public interface PhysicsShape extends BodyPropertiesOwner, FixturePropertiesOwner, PhysicsShapeWithLayout {
     void setup(Body body, PhysicsShapeHelper helper);

     void applyForce(float vx, float vy);
     void applyForceUp(float vx, float vy);
     Point2D getSpeed();
     void setSpeed(float x, float y);

     void addSizeChangedEventListener(ChangedEventListener eventListener);

     void setLocalCenterOffset(Vec2 vec2);
}

