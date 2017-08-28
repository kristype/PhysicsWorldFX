package utilites;

import javafx.geometry.Point2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import sun.jvm.hotspot.gc_implementation.parallelScavenge.ParallelScavengeHeap;

/**
 * Created by Kristian on 26/08/2017.
 */
public class PhysicsShapeHelper {

    private final CoordinateConverter coordinateConverter;

    public PhysicsShapeHelper(CoordinateConverter coordinateConverter){

        this.coordinateConverter = coordinateConverter;
    }

    public Point2D getSpeed(Body body) {
        if (body != null){
            Vec2 velocity = body.getLinearVelocity();
            return coordinateConverter.fxVec2world(velocity.x, velocity.y);
        }
        return null;
    }

    public void setSpeed(Body body, float vx, float vy) {
        if (body != null){
            Vec2 scaled = coordinateConverter.scaleVecToWorld(vx, vy);
            body.setLinearVelocity(scaled);
        }
    }

    public void applyForceUp(Body body,Vec2 centerOffset, float vx, float vy){
        if (body != null) {
            Vec2 worldForce = coordinateConverter.scaleVecToWorld(vx, vy);
            applyForce(body.getWorldVector(worldForce), body, centerOffset);
        }
    }

    public void applyForce(Body body,Vec2 centerOffset,float vx, float vy) {
        if (body != null){
            Vec2 worldForce = coordinateConverter.scaleVecToWorld(vx, vy);
            applyForce(worldForce, body, centerOffset);
        }
    }

    private static void applyForce(Vec2 worldForce, Body body, Vec2 centerOffset) {
        Vec2 worldCenter = body.getWorldCenter();

        double radians = body.getAngle() % (2*Math.PI);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        float xRotation = cos * centerOffset.x - sin * centerOffset.y;
        float yRotation = (sin * centerOffset.x) + cos * centerOffset.y;

        Vec2 offsetWorldCenter = new Vec2(xRotation + worldCenter.x, yRotation + worldCenter.y);
        body.applyForce(worldForce, offsetWorldCenter);
    }
}
