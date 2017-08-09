package utilites;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import shapes.PhysicsCircle;
import shapes.PhysicsPolygon;
import shapes.PhysicsRectangle;

import java.util.ArrayList;


public class ShapeResolver {

    private final CoordinateConverter converter;

    public ShapeResolver(CoordinateConverter converter) {
        this.converter = converter;
    }

    public Shape ResolveShape(Node node) {

        Bounds bounds = node.getBoundsInLocal();
        Shape shape = null;
        if (node instanceof PhysicsRectangle) {

            PolygonShape polygonShape = new PolygonShape();
            float hWidth = (float) bounds.getWidth() / 2f;
            float hHeight = (float) bounds.getHeight() / 2f;
            Vec2 world = converter.scaleVecToWorld(hWidth, hHeight);
            polygonShape.setAsBox(world.x, world.y);
            shape = polygonShape;
        }
        else if (node instanceof PhysicsCircle) {

            CircleShape circleShape = new CircleShape();
            double world = Math.abs(converter.fxScaleToWorld(bounds.getWidth()));
            circleShape.setRadius((float) world / 2f);
            shape = circleShape;
        } else if (node instanceof PhysicsPolygon) {

            PolygonShape polygonShape = new PolygonShape();
            PhysicsPolygon physicsPolygon = (PhysicsPolygon) node;
            int size = physicsPolygon.getPoints().size();
            int vertices = size / 2;
            Vec2[] convertedCoordinates = new Vec2[vertices];
            ObservableList<Double> points = physicsPolygon.getPoints();

            double maxY = 0;
            for (int i = 0; i < vertices; i++) {
                maxY = Math.max(points.get(i * 2 +1), maxY);
            }

            for (int i = 0; i < vertices; i++) {
                int pairIndex = i * 2;
                double xValue = points.get(pairIndex);
                double yValue = maxY - points.get(pairIndex+1);
                convertedCoordinates[i] = converter.scaleVecToWorld(xValue, yValue);
            }
            polygonShape.set(convertedCoordinates, vertices);
            shape = polygonShape;
        }
        return shape;

    }

    public void updateShape(Node node, Fixture fixture) {

        Bounds bounds = node.getBoundsInLocal();
        if (node instanceof PhysicsRectangle) {

            PolygonShape shape = (PolygonShape) fixture.getShape();
            float hWidth = (float) bounds.getWidth() / 2f;
            float hHeight = (float) bounds.getHeight() / 2f;
            Vec2 world = converter.scaleVecToWorld(hWidth, hHeight);
            shape.setAsBox(world.x, world.y);
        }
        else if (node instanceof PhysicsCircle) {

            CircleShape shape = (CircleShape) fixture.getShape();
            double world = Math.abs(converter.fxScaleToWorld(bounds.getWidth()));
            shape.setRadius((float) world / 2f);
        }
    }
}
