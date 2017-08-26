package framework;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Transform;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.collision.Collision;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Kristian on 21/08/2017.
 */
public class PhysicsWorldHelper {
    private static World world;
    private static Map<Node, Body> nodeBodyMap;

    private static Collection<KeyCode> keysPressed;
    private static Collision collision;

    static void setup(World world, Map<Node, Body> nodeBodyMap){
        PhysicsWorldHelper.world = world;
        PhysicsWorldHelper.nodeBodyMap = nodeBodyMap;
        PhysicsWorldHelper.keysPressed = new ArrayList<>();
        PhysicsWorldHelper.collision = new Collision(world.getPool());

    }

    public static Point2D getVectorForDegrees(double degrees, double force){
        double radian = Math.toRadians(degrees);
        return new Point2D((float)(Math.sin(radian) * force), (float)(Math.cos(radian) * force));
    }

    public static boolean physicsNodesOverlap(Node node1, Node node2){

        Body body1 = nodeBodyMap.containsKey(node1) ? nodeBodyMap.get(node1) : nodeBodyMap.get(node1.getParent());
        Body body2 = nodeBodyMap.containsKey(node2) ? nodeBodyMap.get(node2) : nodeBodyMap.get(node2.getParent());

        Shape shape = body1.getFixtureList().getShape();
        Shape shape1 = body2.getFixtureList().getShape();
        return collision.testOverlap(shape, 0, shape1, 0, body1.getTransform(), body2.getTransform());
    }

    public static boolean hasStyle(Node node, String style){
        return node.getStyleClass().contains(style);
    }

    public static void registerKeyDown(KeyCode keyCode){
        if (!keysPressed.contains(keyCode)) {
            keysPressed.add(keyCode);
        }
    }

    public static void RegisterKeyUp(KeyCode keyCode){
        keysPressed.remove(keyCode);
    }

    public static boolean KeyIsDown(KeyCode keyCode){
        return keysPressed.contains(keyCode);
    }

    public static boolean KeyIsUp(KeyCode keyCode){
        return !keysPressed.contains(keyCode);
    }
}
