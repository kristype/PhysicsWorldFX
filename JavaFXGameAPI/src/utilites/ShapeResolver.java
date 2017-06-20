package utilites;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import shapes.PhysicsCircle;
import shapes.PhysicsRectangle;


public class ShapeResolver {

    private final CoordinateConverter converter;

    public ShapeResolver(CoordinateConverter converter){
        this.converter = converter;
    }

    public Shape ResolveShape(Node node){

        Bounds bounds = node.getBoundsInLocal();
        if (node instanceof PhysicsRectangle){

            PolygonShape shape = new PolygonShape();
            float hWidth = (float) bounds.getWidth() / 2f;
            float hHeight = (float) bounds.getHeight() / 2f;
            Point2D world = converter.fxVec2world(hWidth, hHeight);
            shape.setAsBox((float) Math.abs(world.getX()), (float)Math.abs(world.getY()));

            return shape;
        }
        else if(node instanceof PhysicsCircle){
            CircleShape circleShape = new CircleShape();
            double world = Math.abs(converter.fxScaleToWorld(bounds.getWidth()));
            circleShape.setRadius((float)world / 2f);
            return circleShape;
        }
        return null;

    }

}
