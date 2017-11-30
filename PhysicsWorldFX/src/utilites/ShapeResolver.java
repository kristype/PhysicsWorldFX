package utilites;

import framework.nodes.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import framework.physical.*;

import java.util.*;
import java.util.stream.Collectors;


public class ShapeResolver {

    private final CoordinateConverter converter;
    private final PositionHelper positionHelper;

    public ShapeResolver(CoordinateConverter converter, PositionHelper positionHelper) {
        this.converter = converter;
        this.positionHelper = positionHelper;
    }

    public Shape ResolveShape(Node node) {

        Bounds bounds = node.getBoundsInLocal();
        Shape shape = null;
        if (node instanceof PhysicsRectangle) {
            shape = mapRectangle((PhysicsRectangle) node, bounds, new PolygonShape());
        } else if (node instanceof PhysicsCircle) {
            shape = mapCircle( (PhysicsCircle)node, bounds, new CircleShape());
        } else if (node instanceof PhysicsPolygon) {
            shape = mapPolygon((PhysicsPolygon) node, new PolygonShape());
        } else if (node instanceof PhysicsPolyline){
            shape = mapPolyLine((PhysicsPolyline)node, new ChainShape());
        }

        return shape;

    }

    private Shape mapCircle(PhysicsCircle node, Bounds bounds, Shape shape) {

        if (node.getScaleX() == node.getScaleY() && !(node.getParent() instanceof GeometricComposition) ){
            double world = Math.abs(converter.scaleVectorToWorld(bounds.getWidth()));
            float radius = (float) world / 2f;
            radius *= node.getScaleX();

            CircleShape circleShape = shape instanceof CircleShape ? (CircleShape)shape : new CircleShape();
            circleShape.setRadius(radius);
            return circleShape;
        }else{
            //Eclipse or in parent
            Double[] points = {0d, 1d, 1d, 0d, 0d, -1d, -1d, 0d };
            for (int i = 0; i < points.length; i++){
                points[i] *= node.getRadius();
            }

            List<Double> doubles = Arrays.stream(points).collect(Collectors.toList());
            Vec2[] vertices = getVertices(node, doubles);
            PolygonShape polygonShape = shape instanceof PolygonShape ? (PolygonShape)shape : new PolygonShape();
            polygonShape.set(vertices, vertices.length);
            return polygonShape;
        }
    }

    private PolygonShape mapRectangle(PhysicsRectangle node, Bounds bounds, PolygonShape shape) {
        double w = bounds.getWidth();
        double h = bounds.getHeight();
        List<Double> corners = Arrays.asList(0d, h, w, h, w, 0d, 0d, 0d);
        Vec2[] vertices = getVertices(node, corners);

        PolygonShape polygonShape = shape;
        polygonShape.set(vertices, vertices.length);
        return polygonShape;
    }

    private Shape mapPolyLine(PhysicsPolyline node, ChainShape shape) {
        Vec2[] vertices = getVertices(node, node.getPoints());
        ChainShape chainShape = shape;
        if (vertices[0].equals(vertices[vertices.length - 1])) {
            chainShape.createLoop(Arrays.stream(vertices).limit(vertices.length-1).toArray(Vec2[]::new), vertices.length -1);
        } else {
            chainShape.createChain(vertices, vertices.length);
        }
        return chainShape;
    }

    private Vec2[] toVec2(double...points) {
        Vec2[] vertices = new Vec2[points.length / 2];
        for (int i = 0; i < points.length; i += 2) {
            vertices[i / 2] = converter.scaleVectorToWorld(points[i], points[i + 1]);
        }
        return vertices;
    }

    private Shape mapPolygon(PhysicsPolygon node, PolygonShape shape) {

        Vec2[] vertices = getVertices(node, node.getPoints());

        PolygonShape polygon = shape;
        polygon.set(vertices, vertices.length);
        return polygon;
    }

    private <T extends Node & Physical> Vec2[] getVertices(T node, List<Double> nodePoints) {
        Vec2[] vertices;
        if (node.getParent() instanceof GeometricComposition){
            GeometricComposition parent = (GeometricComposition) node.getParent();

            Point2D center = positionHelper.getGeometricCenter(parent);
            double cX = center.getX();
            double cY = center.getY();

            double scaleX = node.getScaleX();
            double scaleY = node.getScaleY();
            double layoutX = node.getLayoutX() * parent.getScaleX();
            double layoutY = node.getLayoutY() * parent.getScaleY();
            double[] points = getTranslatedPoints(nodePoints, layoutX, layoutY, cX, cY, scaleX, scaleY,
                                                  node.getRotate(), parent.getScaleX(), parent.getScaleY());
            vertices = toVec2(points);

            setLocalCenterOffsetForChild(node, vertices);

        } else {
            Point2D center = positionHelper.getGeometricCenter(node);
            double cX = center.getX();
            double cY = center.getY();

            double[] points = getTranslatedPoints(nodePoints, 0, 0, cX, cY, node.getScaleX(), node.getScaleY(),
                                                  0, 1, 1);
            vertices = toVec2(points);
        }
        return vertices;
    }

    private void setLocalCenterOffsetForChild(Physical node, Vec2[] vertices) {
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

        node.setLocalCenterOffset(new Vec2(positionHelper.getCenter(minX, maxX), positionHelper.getCenter(minY, maxY)));
    }

    private double[] getTranslatedPoints(List<? extends Number> nodePoints, double dx, double dy, double cX, double cY,
                                         double scaleX, double scaleY, double rotate, double parentScaleX, double parentScaleY) {
        double[] points = new double[nodePoints.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = nodePoints.get(i).doubleValue();
        }

        positionHelper.scaleWithParent(points, parentScaleX, parentScaleY, cX, cY);
        positionHelper.scaleAndRotate(points, scaleX, scaleY, rotate);
        positionHelper.offsetPoints(points, dx, dy, cX, cY);

        return points;
    }


    public Shape updateShape(Node node, Fixture fixture) {
        Shape shape = null;
        Bounds bounds = node.getBoundsInLocal();
        if (node instanceof PhysicsRectangle) {
            shape = mapRectangle((PhysicsRectangle) node, bounds, (PolygonShape) fixture.getShape());
        }else if (node instanceof PhysicsCircle) {
            shape = mapCircle((PhysicsCircle) node,bounds, fixture.getShape());
        }else if (node instanceof PhysicsPolygon){
            shape = mapPolygon((PhysicsPolygon) node, (PolygonShape) fixture.getShape());
        }else if (node instanceof PhysicsPolyline){
            shape = mapPolyLine((PhysicsPolyline) node, (ChainShape) fixture.getShape());
        }
        return shape;
    }
}
