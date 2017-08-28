package utilites;

import bodies.ShapeComposition;
import com.sun.javafx.geom.Point2D;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import shapes.PhysicsCircle;
import shapes.PhysicsPolygon;
import shapes.PhysicsRectangle;
import shapes.PhysicsShape;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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
            shape = mapPolygon((PhysicsPolygon) node);
        }
        return shape;

    }

    private Vec2[] toVec2(double...points) {
        Vec2[] vertices = new Vec2[points.length / 2];
        for (int i = 0; i < points.length; i += 2) {
            vertices[i / 2] = converter.scaleVecToWorld(points[i], points[i + 1]);
        }
        return vertices;
    }

    private Shape mapPolygon(PhysicsPolygon node) {

        Vec2[] vertices;
        if (node.getParent() instanceof ShapeComposition){
            ShapeComposition parent = (ShapeComposition) node.getParent();
            Bounds bounds = parent.getBoundsInLocal();

            double hWidth = bounds.getWidth() /2;
            double hHeight = bounds.getHeight() /2;

            double[] points = getTranslatedPoints(node.getPoints(), node.getLayoutX(), node.getLayoutY(), hWidth, hHeight);
            vertices = toVec2(points);

            setLocalCenterOffsetForChild(node, vertices);

        } else {
            Bounds bounds = node.getBoundsInLocal();
            double hWidth = bounds.getWidth() /2;
            double hHeight = bounds.getHeight() /2;

            double[] points = getTranslatedPoints(node.getPoints(), 0, 0, hWidth, hHeight);
            vertices = toVec2(points);
        }

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices, vertices.length);
        return polygon;
    }

    private void setLocalCenterOffsetForChild(PhysicsPolygon node, Vec2[] vertices) {
        float minX = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;

        for (int i = 0; i < vertices.length; i++){
            Vec2 vertex = vertices[i];
            minX = Math.min(minX, vertex.x);
            maxX = Math.max(maxX, vertex.x);
            minY = Math.min(minY, vertex.y);
            maxY = Math.max(maxY, vertex.y);
        }

        node.setLocalCenterOffset(new Vec2(getOffset(minX, maxX), getOffset(minY, maxY)));
    }

    private float getOffset(float min, float max) {
        return min >= 0 ? min + (max - min) /2 : max + (min - max) / 2;
    }

    private double[] getTranslatedPoints(List<? extends Number> nodePoints, double dx, double dy, double cX, double cY
    ) {
        double[] points = new double[nodePoints.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = nodePoints.get(i).doubleValue();
        }
        translate(points, dx, dy, cX, cY);
        return points;
    }

    private void translate(double[] points, double dx, double dy, double cX, double cY) {
        double offsetX = dx - cX;
        double offsetY = cY - dy/2 - cY;

        double maxY = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            minY = Math.min(minY, points[i + 1]);
            maxY = Math.max(maxY, points[i + 1]);
        }

        for (int i = 0; i < points.length; i += 2) {
            points[i] += offsetX;
            points[i + 1] = (offsetY + ((maxY - minY) - points[i + 1]));
        }
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
