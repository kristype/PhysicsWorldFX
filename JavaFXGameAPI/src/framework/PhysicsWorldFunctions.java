package framework;

import framework.geometric.Geometric;
import framework.nodes.GeometricComposition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.jbox2d.collision.Collision;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import utilites.CoordinateConverter;
import utilites.PositionHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PhysicsWorldFunctions {
    private static Map<Node, Body> nodeBodyMap;
    private static Map<Node, Fixture> nodeFixtureMap;
    private static CoordinateConverter coordinateConverter;

    private static Collection<KeyCode> keysPressed = new ArrayList<>();
    private static Collision collision;

    private static PositionHelper positionHelper = new PositionHelper();

    static void setup(World world, Map<Node, Body> nodeBodyMap, Map<Node, Fixture> nodeFixtureMap, CoordinateConverter coordinateConverter){
        PhysicsWorldFunctions.nodeBodyMap = nodeBodyMap;
        PhysicsWorldFunctions.nodeFixtureMap = nodeFixtureMap;
        PhysicsWorldFunctions.coordinateConverter = coordinateConverter;
        PhysicsWorldFunctions.keysPressed = new ArrayList<>(); //reset keys pressed
        PhysicsWorldFunctions.collision = new Collision(world.getPool());
    }

    /**
     * The window stage the node is in
     * @param node the node used to retrieve the window stage
     * @return the window stage
     */
    public static Stage getStage(Node node){
        return (Stage)node.getScene().getWindow();
    }

    /**
     * Calculates a vector with the given force based on the given degree
     * @param degrees 0 is up, 90 is right, 180 is down, 270 is left
     * @param force the force the vector should be multiplied with
     * @return the calculated vector pointing in the direction given in degrees with the given force
     */
    public static Point2D getVectorForDegrees(double degrees, double force){
        double radian = Math.toRadians(degrees);
        return new Point2D((float)(Math.sin(radian) * force), (float)-(Math.cos(radian) * force));
    }

    /**
     * Calculates a point in the layout bounds of the parent for the node, that is rotated along the geometric center
     * @param node the node the layout coordinates and rotation angle are taken from, and the geometric center is calculated from
     * @param layoutOffsetX offset value for layoutX
     * @param layoutOffsetY offset value for layoutY
     * @return the calculated point rotated around the geometric center of the node
     */
    public static Point2D getRotatedLayoutPosition(Node node, double layoutOffsetX, int layoutOffsetY) {

        return getRotatedLayoutPosition(node, layoutOffsetX, layoutOffsetY, node.getRotate());
    }

    /**
     * Calculates a point in the layout bounds of the parent for the node, that is rotated along the geometric center
     * @param node the node the layout coordinates are taken from, and the geometric center is calculated from
     * @param layoutOffsetX offset value for layoutX
     * @param layoutOffsetY offset value for layoutY
     * @param rotate the rotation angle in degrees
     * @return the calculated point rotated around the geometric center of the node
     */
    public static Point2D getRotatedLayoutPosition(Node node, double layoutOffsetX, int layoutOffsetY, double rotate) {

        Bounds bounds = node.getLayoutBounds();
        double x = node.getLayoutX() + layoutOffsetX;
        double y = node.getLayoutY() + layoutOffsetY;
        Point2D center = positionHelper.getCenter(bounds);
        return getRotatedPoint(rotate, x, y, new Point2D(node.getLayoutX() + center.getX(), node.getLayoutY() + center.getY()));
    }

    /**
     * Calculates a rotated point that is rotated along the geometric center of the node
     * @param node the node the layout coordinates and rotation angle are taken from, and the geometric center is calculated from
     * @param x x value
     * @param y y value
     * @param rotate the rotation angle in degrees
     * @return the caluclated point rotated around the geometric center of the node
     */
    public static Point2D getRotatedPoint(Node node, double x, double y, double rotate){
        return getRotatedPoint(rotate, x, y, positionHelper.getGeometricCenter(node));
    }

    private static Point2D getRotatedPoint(double rotate, double x, double y, Point2D center){
        return getRotatedPoint(rotate, x, y, center.getX(), center.getY());
    }

    /**
     * Calculates a rotated point that is rotated along the given center
     * @param rotate the rotation angle in degrees
     * @param x x value
     * @param y y value
     * @param centerX center x value
     * @param centerY center y value
     * @return the rotated point
     */
    public static Point2D getRotatedPoint(double rotate, double x, double y, double centerX, double centerY) {
        double radian = Math.toRadians(rotate);

        x -= centerX;
        y -= centerY;

        double cos = Math.cos(radian);
        double sin = Math.sin(radian);

        double rotatedX  = cos * x - sin * y;
        double rotatedY = sin * x + cos * y;

        x = rotatedX;
        y = rotatedY;

        x += centerX;
        y += centerY;

        return new Point2D(x, y);
    }

    /**
     * Checks if two physics nodes are touching or overlapping
     * @param node1 node1
     * @param node2 node2
     * @return true if the nodes are touching, false if they are not touching
     */
    public static boolean physicsNodesTouching(Node node1, Node node2){

        Body body1 = nodeBodyMap.containsKey(node1) ? nodeBodyMap.get(node1) : nodeBodyMap.get(node1.getParent());
        Body body2 = nodeBodyMap.containsKey(node2) ? nodeBodyMap.get(node2) : nodeBodyMap.get(node2.getParent());

        Shape shape1 = nodeFixtureMap.containsKey(node1)  ? nodeFixtureMap.get(node1).getShape() : body1.getFixtureList().getShape();
        Shape shape2 = nodeFixtureMap.containsKey(node2)  ? nodeFixtureMap.get(node2).getShape() : body2.getFixtureList().getShape();

        return collision.testOverlap(shape1, 0, shape2, 0, body1.getTransform(), body2.getTransform());
    }

    /**
     * Checks if a nodes has a StyleClass
     * @param node the node
     * @param style the style to check
     * @return true if the style contains the StyleClass, false if it does not contain the StyleClass
     */
    public static boolean hasStyle(Node node, String style){
        return node.getStyleClass().contains(style);
    }

    /**
     * Registers a key pressed
     * @param keyCode the keycode pressed
     */
    public static void registerKeyPressed(KeyCode keyCode){
        if (!keysPressed.contains(keyCode)) {
            keysPressed.add(keyCode);
        }
    }

    /**
     * Registers a key released
     * @param keyCode the keycode released
     */
    public static void registerKeyReleased(KeyCode keyCode){
        keysPressed.remove(keyCode);
    }

    /**
     * Checks if a key is pressed
     * @param keyCode the keycode to check
     * @return true if the key is pressed, false if the key is released
     */
    public static boolean keyIsPressed(KeyCode keyCode){
        return keysPressed.contains(keyCode);
    }

    /**
     * Retrieves the center of mass point in the world node
     * @param node the physics node to retrieve center of mass point from
     * @return the center of mass location in the world node
     * @throws Exception when physics node is contained in a framework.nodes.GeometricComposition
     * @see GeometricComposition
     */
    public static Point2D getWorldCenterOfMass(Node node) throws Exception {
        if (!nodeBodyMap.containsKey(node))
            throw new Exception("Node must be the outermost physical node", new Throwable());

        Body body = nodeBodyMap.get(node);
        return coordinateConverter.convertWorldPointToScreen(body.getWorldCenter(), node.getParent());
    }

    /**
     * Retrieves the center of mass point in the node layout bounds
     * @param node the physics node to retrieve center of mass point from
     * @return the center of mass location in the node layout bounds
     * @throws Exception when physics node is contained in a framework.nodes.GeometricComposition
     * @see GeometricComposition
     */
    public static Point2D getLocalCenterOfMass(Node node) throws Exception {
        if (!nodeBodyMap.containsKey(node))
            throw new Exception("Node must be the outermost physical node", new Throwable());

        Body body = nodeBodyMap.get(node);
        Vec2 localCenter = body.getLocalCenter();
        Point2D converted = coordinateConverter.convertVectorToScreen(localCenter);
        Bounds bounds = node.getLayoutBounds();
        Point2D center = positionHelper.getCenter(bounds);
        return new Point2D(converted.getX() + center.getX(), converted.getY() + center.getY());
    }

    /**
     * Calculates the geometric center of a node
     * @param node the node
     * @return the geometric center
     */
    public static Point2D getGeometricCenter(Node node){
        return positionHelper.getGeometricCenter(node);
    }

    /**
     * Finds the largest velocity value by comparing the absolute value of linear velocity x and y
     * @param node the node to check
     * @return the largest absolute value of linear velocity x and y
     */
    public static <T extends Node & Geometric> double getHighestVelocity(T node){
        return Math.max(Math.abs(node.getLinearVelocityX()), Math.abs(node.getLinearVelocityY()));
    }

    /**
     * Calculates the combined speed value of linear velocity x and y
     * @param node the node to check
     * @return the combined absolute value of linear velocity x and y
     */
    public static <T extends Node & Geometric> double getCombinedVelocity(T node){
        return Math.abs(node.getLinearVelocityX())  + Math.abs(node.getLinearVelocityY());
    }

    /**
     * Sets the velocity of a node in the direction it is currently travelling
     * @param node the node to set velocity on
     * @param speed the velocity the largest direction should have
     */
    public static <T extends Node & Geometric> void setVelocityToCurrentTravelVector(T node, double speed){
        double currentSpeed = getHighestVelocity(node);
        double directionX =  node.getLinearVelocityX() / currentSpeed;
        double directionY =  node.getLinearVelocityY() / currentSpeed;
        node.setLinearVelocityX(directionX * speed);
        node.setLinearVelocityY(directionY * speed);
    }

    /**
     * Calculates a velocity vector from the current node travel vector that is offset by the given angle
     * @param node the node to calculate velocity from
     * @param angleOffset the angle the calculated vector should be offset
     * @return the offset travel vector
     */
    public static <T extends Node & Geometric> Point2D getOffsetTravelVector(T node, double angleOffset) {
        double currentSpeed = getCombinedVelocity(node);
        double directionX =  node.getLinearVelocityX() / currentSpeed;
        double directionY =  node.getLinearVelocityY() / currentSpeed;

        //Invert directionY because javaFX direction is reversed from normal math direction
        double degrees = Math.toDegrees(Math.atan2(directionX, -directionY)) + angleOffset;
        return getVectorForDegrees(degrees, currentSpeed);
    }

    /**
     * Loads an Fxml resource from file
     * @param relativeClass the class location relative to the resource
     * @param resource the path to the resource from the relativeClass location
     * @param <T> root type in the resource file
     * @return the loaded resource
     * @throws IOException when file can not be found
     */
    public static <T> T loadFxmlResource(Class<?> relativeClass, String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(relativeClass.getResource(resource));
        return loader.load();
    }
}
