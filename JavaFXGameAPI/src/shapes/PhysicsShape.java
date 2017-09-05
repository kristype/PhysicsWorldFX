package shapes;

import bodies.BodyPropertiesOwner;
import framework.ChangedEventListener;
import javafx.geometry.Point2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.PhysicsShapeHelper;

public interface PhysicsShape {
     void setup(Body body, PhysicsShapeHelper helper);
}

