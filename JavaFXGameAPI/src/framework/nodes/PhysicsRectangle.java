package framework.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import framework.geometric.GeometricPropertyDefinitions;
import framework.events.ChangedEvent;
import framework.events.ChangedEventListener;
import framework.SimulationType;
import javafx.beans.value.ObservableValue;
import javafx.css.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import javafx.scene.shape.Rectangle;
import framework.physical.*;
import utilites.PhysicsShapeHelper;

public class PhysicsRectangle extends Rectangle implements SingleShape {

    private Body body;
    private List<ChangedEventListener> sizeChangedListeners;
    private PhysicsShapeHelper helper;
    private Vec2 localCenterOffset = new Vec2();

    private static final StyleablePropertyFactory<PhysicsRectangle> SPF = new StyleablePropertyFactory<>(Rectangle.getClassCssMetaData());
    private PhysicalPropertyDefinitions<PhysicsRectangle> physicalPropertyDefinitions;
    private GeometricPropertyDefinitions<PhysicsRectangle> geometricPropertyDefinitions;

    public PhysicsRectangle() {
        getStyleClass().add("physicsRectangle");
        this.physicalPropertyDefinitions = new PhysicalPropertyDefinitions<>(this, SPF);
        this.geometricPropertyDefinitions = new GeometricPropertyDefinitions<>(this, SPF);
        sizeChangedListeners = new ArrayList<>();
        widthProperty().addListener((observable, oldValue, newValue) -> raiseEvent(sizeChangedListeners));
        heightProperty().addListener((observable, oldValue, newValue) -> raiseEvent(sizeChangedListeners));
    }

    public PhysicalPropertyDefinitions<? extends Styleable> getPhysicalPropertyDefinitions() {
        return physicalPropertyDefinitions;
    }

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
    public void applyUpwardForceToCenter(double upwardForceX, double upwardForceY) {
        helper.applyForceUp(body, localCenterOffset, upwardForceX, upwardForceY);
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
    public void addSizeChangedEventListener(ChangedEventListener listener)  {
        sizeChangedListeners.add(listener);
    }

    @Override
    public void setLocalCenterOffset(Vec2 vec2) {
        localCenterOffset = vec2;
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

    @Override
    public ObservableValue<Double> densityProperty() {
        return physicalPropertyDefinitions.densityProperty();
    }

    @Override
    public double getDensity() {
        return physicalPropertyDefinitions.getDensity();
    }

    @Override
    public void setDensity(double density) {
        physicalPropertyDefinitions.setDensity(density);
    }

    @Override
    public ObservableValue<Double> frictionProperty() {
        return physicalPropertyDefinitions.frictionProperty();
    }

    @Override
    public double getFriction() {
        return physicalPropertyDefinitions.getFriction();
    }

    @Override
    public void setFriction(double friction) {
        physicalPropertyDefinitions.setFriction(friction);
    }

    @Override
    public ObservableValue<Double> restitutionProperty() {
        return physicalPropertyDefinitions.restitutionProperty();
    }

    @Override
    public double getRestitution() {
        return physicalPropertyDefinitions.getRestitution();
    }

    @Override
    public void setRestitution(double restitution) {
        physicalPropertyDefinitions.setRestitution(restitution);
    }

    @Override
    public ObservableValue<Boolean> sensorProperty() {
        return physicalPropertyDefinitions.sensorProperty();
    }

    @Override
    public boolean isSensor() {
        return physicalPropertyDefinitions.isSensor();
    }

    @Override
    public void setSensor(boolean sensor) {
        physicalPropertyDefinitions.setSensor(sensor);
    }

    @Override
    public ObservableValue<Integer> collisionFilterMaskProperty() {
        return physicalPropertyDefinitions.filterMaskProperty();
    }

    @Override
    public int getCollisionFilterMask() {
        return physicalPropertyDefinitions.getFilterMask();
    }

    @Override
    public void setCollisionFilterMask(int filterMask) {
        physicalPropertyDefinitions.setFilterMask(filterMask);
    }

    @Override
    public ObservableValue<Integer> collisionFilterCategoryProperty() {
        return physicalPropertyDefinitions.filterCategoryProperty();
    }

    @Override
    public int getCollisionFilterCategory() {
        return physicalPropertyDefinitions.getFilterCategory();
    }

    @Override
    public void setCollisionFilterCategory(int filterCategory) {
        physicalPropertyDefinitions.setFilterCategory(filterCategory);
    }

    @Override
    public ObservableValue<Integer> collisionFilterGroupProperty() {
        return physicalPropertyDefinitions.filterGroupProperty();
    }

    @Override
    public int getCollisionFilterGroup() {
        return physicalPropertyDefinitions.getFilterGroup();
    }

    @Override
    public void setCollisionFilterGroup(int filterGroup) {
        physicalPropertyDefinitions.setFilterGroup(filterGroup);
    }

    private void raiseEvent(List<ChangedEventListener> eventListeners){
        ChangedEvent event = new ChangedEvent(this);
        Iterator<ChangedEventListener> i = eventListeners.iterator();
        while(i.hasNext())  {
            ((ChangedEventListener) i.next()).handleChangedEvent(event);
        }
    }

}