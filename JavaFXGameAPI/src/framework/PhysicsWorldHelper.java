package framework;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.jbox2d.collision.Collision;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PhysicsWorldHelper {
    private static Map<Node, Body> nodeBodyMap;
    private static Map<Node, Fixture> nodeFixtureMap;

    private static Collection<KeyCode> keysPressed;
    private static Collision collision;

    static void setup(World world, Map<Node, Body> nodeBodyMap, Map<Node, Fixture> nodeFixtureMap){
        PhysicsWorldHelper.nodeBodyMap = nodeBodyMap;
        PhysicsWorldHelper.nodeFixtureMap = nodeFixtureMap;
        PhysicsWorldHelper.keysPressed = new ArrayList<>();
        PhysicsWorldHelper.collision = new Collision(world.getPool());
    }

    public static Stage getStage(Node node){
        return (Stage)node.getScene().getWindow();
    }

    public static Point2D getVectorForDegrees(double degrees, double force){
        double radian = Math.toRadians(degrees);
        return new Point2D((float)(Math.sin(radian) * force), (float)(Math.cos(radian) * force));
    }

    public static boolean physicsNodesTouching(Node node1, Node node2){

        Body body1 = nodeBodyMap.containsKey(node1) ? nodeBodyMap.get(node1) : nodeBodyMap.get(node1.getParent());
        Body body2 = nodeBodyMap.containsKey(node2) ? nodeBodyMap.get(node2) : nodeBodyMap.get(node2.getParent());

        Shape shape1 = nodeFixtureMap.containsKey(node1)  ? nodeFixtureMap.get(node1).getShape() : body1.getFixtureList().getShape();
        Shape shape2 = nodeFixtureMap.containsKey(node2)  ? nodeFixtureMap.get(node2).getShape() : body2.getFixtureList().getShape();

        return collision.testOverlap(shape1, 0, shape2, 0, body1.getTransform(), body2.getTransform());
    }

    public static boolean hasStyle(Node node, String style){
        return node.getStyleClass().contains(style);
    }

    public static void registerKeyPressed(KeyCode keyCode){
        if (!keysPressed.contains(keyCode)) {
            keysPressed.add(keyCode);
        }
    }

    public static void registerKeyReleased(KeyCode keyCode){
        keysPressed.remove(keyCode);
    }

    public static boolean keyIsPressed(KeyCode keyCode){
        return keysPressed.contains(keyCode);
    }

}
