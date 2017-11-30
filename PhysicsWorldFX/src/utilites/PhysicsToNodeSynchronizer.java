package utilites;

import framework.geometric.Geometric;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Created by Kristian on 12/09/2017.
 */
public class PhysicsToNodeSynchronizer {

    private final CoordinateConverter coordinateConverter;
    private final PositionHelper positionHelper;

    public PhysicsToNodeSynchronizer(CoordinateConverter coordinateConverter, PositionHelper positionHelper){

        this.coordinateConverter = coordinateConverter;
        this.positionHelper = positionHelper;
    }

    public void updateNodeData(Node node, Body body) {
        Point2D nodePosition = coordinateConverter.convertWorldPointToScreen(body.getPosition(), node.getParent());
        Point2D center = positionHelper.getGeometricCenter(node);
        double x = nodePosition.getX() - center.getX();
        double y = nodePosition.getY() - center.getY();
        node.setLayoutX(x);
        node.setLayoutY(y);
        double fxAngle = positionHelper.getAngle(body.getAngle());
        node.setRotate(fxAngle);

        Vec2 linearVelocity = body.getLinearVelocity();
        Point2D convertedVelocity = coordinateConverter.convertVectorToScreen(linearVelocity);

        double scaledAngularVelocity = coordinateConverter.scaleVectorToScreen(body.getAngularVelocity());

        Geometric geometric = (Geometric) node;
        geometric.setLinearVelocityX(convertedVelocity.getX());
        geometric.setLinearVelocityY(convertedVelocity.getY());
        geometric.setAngularVelocity(scaledAngularVelocity);
        geometric.setActive(body.isActive());
        geometric.setAwake(body.isAwake());
    }
}
