package framework.nodes;

import framework.geometric.GeometricPropertiesOwner;
import framework.geometric.GeometricPropertyDefinitions;
import framework.geometric.Geometric;
import framework.SimulationType;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.layout.Pane;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import framework.physical.PhysicsShape;
import utilites.PhysicsShapeHelper;

import java.util.List;

/**
 * This class can be used to combine nodes into a single geometric node.
 * This node's geometric properties overrides child nodes geometric properties.
 */
public class GeometricComposition extends Pane implements GeometricPropertiesOwner, Geometric, PhysicsShape {

    private final GeometricPropertyDefinitions<GeometricComposition> geometricPropertyDefinitions;

    private Body body;
    private PhysicsShapeHelper helper;
    private Vec2 localCenterOffset = new Vec2();

    public GeometricComposition() {
        super();
        getStyleClass().add("geometricComposition");
        this.geometricPropertyDefinitions = new GeometricPropertyDefinitions<>(this, SPF);
    }

    private static final StyleablePropertyFactory<GeometricComposition> SPF = new StyleablePropertyFactory<>(Pane.getClassCssMetaData());

    public GeometricPropertyDefinitions<? extends Styleable> getGeometricPropertyDefinitions() {
        return geometricPropertyDefinitions;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
    }


    @Override
    public void setup(Body body, PhysicsShapeHelper helper){
        this.body = body;
        this.helper = helper;
    }

    @Override
    public void applyForceToCenter(double forceX, double forceY) {
        helper.applyForce(body, localCenterOffset, forceX, forceY);
    }

    @Override
    public void applyForceToPoint(double localPointX, double localPointY, double forceX, double forceY) {
        helper.applyForceToPoint(this, body, localPointX, localPointY, forceX, forceY);
    }

    @Override
    public void applyUpwardForceToPoint(double localPointX, double localPointY, double upwardForceX, double upwardForceY) {
        helper.applyUpwardForceToPoint(this, body, localPointX, localPointY, upwardForceX, upwardForceY);
    }

    @Override
    public void applyForceToCenterOfMass(double forceX, double forceY) {
        helper.applyForceToCenterOfMass(body, forceX, forceY);
    }

    @Override
    public void applyUpwardForceToCenterOfMass(double upwardForceX, double upwardForceY) {
        helper.applyForceUpToCenterOfMass(body, upwardForceX, upwardForceY);
    }

    @Override
    public void applyUpwardForceToCenter(double upwardForceX, double upwardForceY) {
        helper.applyForceUp(body, localCenterOffset, upwardForceX, upwardForceY);
    }

    @Override
    public ObservableValue<SimulationType> simulationTypeProperty() {
        return geometricPropertyDefinitions.simulationTypeProperty();
    }

    @Override
    public SimulationType getSimulationType() {
        return geometricPropertyDefinitions.getSimulationType();
    }

    @Override
    public void setSimulationType(SimulationType bodyType) {
        geometricPropertyDefinitions.setSimulationType(bodyType);
    }

    @Override
    public ObservableValue<Double> linearDampingProperty() {
        return geometricPropertyDefinitions.linearDampingProperty();
    }

    @Override
    public double getLinearDamping() {
        return geometricPropertyDefinitions.getLinearDamping();
    }

    @Override
    public void setLinearDamping(double linearDamping) {
        geometricPropertyDefinitions.setLinearDamping(linearDamping);
    }

    @Override
    public ObservableValue<Double> linearVelocityXProperty() {
        return geometricPropertyDefinitions.linearVelocityXProperty();
    }

    @Override
    public double getLinearVelocityX() {
        return geometricPropertyDefinitions.getLinearVelocityX();
    }

    @Override
    public void setLinearVelocityX(double linearVelocityX) {
        geometricPropertyDefinitions.setLinearVelocityX(linearVelocityX);
    }

    @Override
    public ObservableValue<Double> linearVelocityYProperty() {
        return geometricPropertyDefinitions.linearVelocityYProperty();
    }

    @Override
    public double getLinearVelocityY() {
        return geometricPropertyDefinitions.getLinearVelocityY();
    }

    @Override
    public void setLinearVelocityY(double linearVelocityY) {
        geometricPropertyDefinitions.setLinearVelocityY(linearVelocityY);
    }

    @Override
    public ObservableValue<Double> angularVelocityProperty() {
        return geometricPropertyDefinitions.angularVelocityProperty();
    }

    @Override
    public double getAngularVelocity() {
        return geometricPropertyDefinitions.getAngularVelocity();
    }

    @Override
    public void setAngularVelocity(double angularVelocity) {
        geometricPropertyDefinitions.setAngularVelocity(angularVelocity);
    }

    @Override
    public ObservableValue<Double> angularDampingProperty() {
        return geometricPropertyDefinitions.angularDampingProperty();
    }

    @Override
    public double getAngularDamping() {
        return geometricPropertyDefinitions.getAngularDamping();
    }

    @Override
    public void setAngularDamping(double angularDamping) {
        geometricPropertyDefinitions.setAngularDamping(angularDamping);
    }

    @Override
    public ObservableValue<Double> gravityScaleProperty() {
        return geometricPropertyDefinitions.gravityScaleProperty();
    }

    @Override
    public double getGravityScale() {
        return geometricPropertyDefinitions.getGravityScale();
    }

    @Override
    public void setGravityScale(double gravityScale) {
        geometricPropertyDefinitions.setGravityScale(gravityScale);
    }

    @Override
    public ObservableValue<Boolean> allowSleepProperty() {
        return geometricPropertyDefinitions.allowSleepProperty();
    }

    @Override
    public boolean isAllowSleep() {
        return geometricPropertyDefinitions.isAllowSleep();
    }

    @Override
    public void setAllowSleep(boolean allowSleep) {
        geometricPropertyDefinitions.setAllowSleep(allowSleep);
    }

    @Override
    public ObservableValue<Boolean> awakeProperty() {
        return geometricPropertyDefinitions.awakeProperty();
    }

    @Override
    public boolean isAwake() {
        return geometricPropertyDefinitions.isAwake();
    }

    @Override
    public void setAwake(boolean awake) {
        geometricPropertyDefinitions.setAwake(awake);
    }

    @Override
    public ObservableValue<Boolean> fixedRotationProperty() {
        return geometricPropertyDefinitions.fixedRotationProperty();
    }

    @Override
    public boolean isFixedRotation() {
        return geometricPropertyDefinitions.isFixedRotation();
    }

    @Override
    public void setFixedRotation(boolean fixedRotation) {
        geometricPropertyDefinitions.setFixedRotation(fixedRotation);
    }

    @Override
    public ObservableValue<Boolean> activeProperty() {
        return geometricPropertyDefinitions.activeProperty();
    }

    @Override
    public boolean isActive() {
        return geometricPropertyDefinitions.isActive();
    }

    @Override
    public void setActive(boolean active) {
        geometricPropertyDefinitions.setActive(active);
    }

    @Override
    public ObservableValue<Boolean> bulletProperty() {
        return geometricPropertyDefinitions.bulletProperty();
    }

    @Override
    public boolean isBullet() {
        return geometricPropertyDefinitions.isBullet();
    }

    @Override
    public void setBullet(boolean bullet) {
        geometricPropertyDefinitions.setBullet(bullet);
    }
}
