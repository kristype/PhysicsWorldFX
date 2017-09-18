package utilites;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class PhysicsShapeHelper {

    private final CoordinateConverter coordinateConverter;
    private final PositionHelper positionHelper;

    public PhysicsShapeHelper(CoordinateConverter coordinateConverter, PositionHelper positionHelper) {
        this.coordinateConverter = coordinateConverter;
        this.positionHelper = positionHelper;
    }

    public void applyForceUp(Body body, Vec2 centerOffset, double vx, double vy) {
        if (body != null) {
            Vec2 worldForce = coordinateConverter.convertVectorToWorld(vx, vy);
            applyForce(body.getWorldVector(worldForce), body, centerOffset);
        }
    }

    public void applyForce(Body body, Vec2 centerOffset, double vx, double vy) {
        if (body != null) {
            Vec2 worldForce = coordinateConverter.convertVectorToWorld(vx, vy);
            applyForce(worldForce, body, centerOffset);
        }
    }

    public void applyUpwardForceToPoint(Node node, Body body, double pointX, double pointY, double forceX, double forceY) {
        if (body != null) {
            Vec2 worldForce = coordinateConverter.convertVectorToWorld(forceX, forceY);
            Vec2 worldVector = body.getWorldVector(worldForce);
            applyForceToPoint(node, body, pointX, pointY, worldVector);
        }
    }

    public void applyForceToPoint(Node node, Body body, double pointX, double pointY, double forceX, double forceY) {
        if (body != null) {
            Vec2 worldForce = coordinateConverter.convertVectorToWorld(forceX, forceY);
            applyForceToPoint(node, body, pointX, pointY, worldForce);
        }
    }

    private void applyForceToPoint(Node node, Body body, double pointX, double pointY, Vec2 worldForce) {
        Point2D center = positionHelper.getCenter(node.getLayoutBounds());
        Vec2 offsetPoint = coordinateConverter.convertVectorToWorld(pointX - center.getX(), pointY - center.getY());
        Vec2 worldCenter = body.getPosition();

        Vec2 point = new Vec2(worldCenter.x + offsetPoint.x, worldCenter.y + offsetPoint.y);
        body.applyForce(worldForce, point);
    }

    private static void applyForce(Vec2 worldForce, Body body, Vec2 centerOffset) {
        Vec2 worldCenter = body.getPosition();

        double radians = body.getAngle() % (2 * Math.PI);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        float xRotation = cos * centerOffset.x - sin * centerOffset.y;
        float yRotation = (sin * centerOffset.x) + cos * centerOffset.y;

        Vec2 offsetWorldCenter = new Vec2(xRotation + worldCenter.x, yRotation + worldCenter.y);
        body.applyForce(worldForce, offsetWorldCenter);
    }

    public void applyForceToCenterOfMass(Body body, double vx, double vy) {
        Vec2 center = body.getWorldCenter();
        Vec2 force = coordinateConverter.convertVectorToWorld(vx, vy);
        body.applyForce(force, center);
    }

    public void applyForceUpToCenterOfMass(Body body, double vx, double vy) {
        Vec2 center = body.getWorldCenter();
        Vec2 force = coordinateConverter.convertVectorToWorld(vx, vy);
        body.applyForce(body.getWorldVector(force), center);
    }
}