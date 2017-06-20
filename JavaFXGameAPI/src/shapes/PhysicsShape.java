package shapes;

import org.jbox2d.dynamics.Body;

/**
 * Created by Kristian on 10/06/2017.
 */
public interface PhysicsShape extends BodyDefBeanOwner, FixtureDefBeanOwner {
    void setup(Body body);
}
