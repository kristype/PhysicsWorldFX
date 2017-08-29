package shapes;

import bodies.BodyDefBeanOwner;
import framework.ChangedEventListener;
import javafx.geometry.Point2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.CoordinateConverter;
import utilites.PhysicsShapeHelper;

public interface PhysicsShape extends BodyDefBeanOwner, FixtureDefBeanOwner {
     void setup(Body body, PhysicsShapeHelper helper);

     void applyForce(float vx, float vy);
     void applyForceUp(float vx, float vy);
     Point2D getSpeed();
     void setSpeed(float x, float y);

     void addSizeChangedEventListener(ChangedEventListener eventListener);
     void addLayoutChangedEventListener(ChangedEventListener eventListener);

    void setLocalCenterOffset(Vec2 vec2);
}
