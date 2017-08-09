package shapes;

import bodies.BodyDefBeanOwner;
import framework.ChangedEventListener;
import javafx.geometry.Point2D;
import org.jbox2d.dynamics.Body;
import utilites.CoordinateConverter;

public interface PhysicsShape extends BodyDefBeanOwner, FixtureDefBeanOwner {
     void setup(Body body, CoordinateConverter coordinateConverter);

     void applyForce(float vx, float vy);
     Point2D getSpeed();
     void setSpeed(float x, float y);

     void addSizeChangedEventListener(ChangedEventListener eventListener);
     void addLayoutChangedEventListener(ChangedEventListener eventListener);
}
