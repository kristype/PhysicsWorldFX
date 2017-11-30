package framework.physical;

import org.jbox2d.dynamics.Body;
import utilites.PhysicsShapeHelper;

public interface PhysicsShape {
     void setup(Body body, PhysicsShapeHelper helper);
}

