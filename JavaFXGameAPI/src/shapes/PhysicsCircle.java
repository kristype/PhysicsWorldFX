package shapes;

import bodies.BodyPropertyDefinitions;
import bodies.BodyPropertiesOwner;
import framework.ChangedEvent;
import framework.ChangedEventListener;
import framework.SimulationType;
import javafx.beans.value.ObservableValue;
import javafx.css.*;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.PhysicsShapeHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhysicsCircle extends Circle implements BodyPropertiesOwner, FixturePropertiesOwner, PhysicsShape {

    private Body body;
    private List<ChangedEventListener> sizeChangedListeners;
    private List<ChangedEventListener> layoutChangedListeners;
    private List<ChangedEventListener> velocityChangedListeners;
    private PhysicsShapeHelper helper;
    private Vec2 localCenterOffset = new Vec2();

    public void setup(Body body, PhysicsShapeHelper helper){
        this.body = body;
        this.helper = helper;
    }

    @Override
    public void applyForce(float vx, float vy) {
        helper.applyForce(body, localCenterOffset, vx, vy);
    }

    @Override
    public void applyForceUp(float vx, float vy) {
        helper.applyForceUp(body, localCenterOffset, vx, vy);
    }

    @Override
    public Point2D getSpeed() {
        return helper.getSpeed(body);
    }

    @Override
    public void setSpeed(float x, float y) {
        helper.setSpeed(body, x, y);
    }

    private void raiseEvent(List<ChangedEventListener> eventListeners){
        ChangedEvent event = new ChangedEvent(this);
        Iterator i = eventListeners.iterator();
        while(i.hasNext())  {
            ((ChangedEventListener) i.next()).handleChangedEvent(event);
        }
    }

    public void addLayoutChangedEventListener(ChangedEventListener listener)  {
        layoutChangedListeners.add(listener);
    }
    public void removeLayoutChangedListener(ChangedEventListener listener)   {
        layoutChangedListeners.remove(listener);
    }

    public void addSizeChangedEventListener(ChangedEventListener listener)  {
        sizeChangedListeners.add(listener);
    }
    public void removeSizeChangedEventListener(ChangedEventListener listener)   {
        sizeChangedListeners.remove(listener);
    }


    @Override
    public void addVelocityChangedEventListener(ChangedEventListener eventListener) {
        velocityChangedListeners.add(eventListener);
    }

    private static final StyleablePropertyFactory<PhysicsCircle> SPF = new StyleablePropertyFactory<>(Circle.getClassCssMetaData());

    public static  List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
    }

    private FixturePropertyDefinitions<PhysicsCircle> fixturePropertyDefinitions;
    private BodyPropertyDefinitions<PhysicsCircle> bodyPropertyDefinitions;

    public PhysicsCircle() {
        getStyleClass().add("circleBody");
        this.fixturePropertyDefinitions = new FixturePropertyDefinitions<PhysicsCircle>(this, SPF);
        this.bodyPropertyDefinitions = new BodyPropertyDefinitions<PhysicsCircle>(this, SPF);
        sizeChangedListeners = new ArrayList<>();
        layoutChangedListeners = new ArrayList<>();
        velocityChangedListeners = new ArrayList<>();
        layoutXProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        layoutYProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        rotateProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        radiusProperty().addListener((observable, oldValue, newValue) -> raiseEvent(sizeChangedListeners));
        ((SimpleStyleableObjectProperty<Number>) angularVelocityProperty()).addListener((observable, oldValue, newValue) -> raiseEvent(velocityChangedListeners));
        ((SimpleStyleableObjectProperty<Number>) linearVelocityXProperty()).addListener((observable, oldValue, newValue) -> raiseEvent(velocityChangedListeners));
        ((SimpleStyleableObjectProperty<Number>) linearVelocityYProperty()).addListener((observable, oldValue, newValue) -> raiseEvent(velocityChangedListeners));
    }

    public FixturePropertyDefinitions<? extends Styleable> getFixturePropertyDefinitions() {
        return fixturePropertyDefinitions;
    }

    public BodyPropertyDefinitions<? extends Styleable> getBodyPropertyDefinitions() {
        return bodyPropertyDefinitions;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public StyleableProperty<SimulationType> bodyTypeProperty() {
        return bodyPropertyDefinitions.bodyTypeProperty();
    }
    public final SimulationType getBodyType() {
        return bodyPropertyDefinitions.getBodyType();
    }
    public final void setBodyType(SimulationType bodyType) {
        bodyPropertyDefinitions.setBodyType(bodyType);
    }

    public StyleableProperty<Number> linearDampingProperty() {
        return bodyPropertyDefinitions.linearDampingProperty();
    }
    public final double getLinearDamping() {
        return bodyPropertyDefinitions.getLinearDamping();
    }
    public final void setLinearDamping(double linearDamping) {
        bodyPropertyDefinitions.setLinearDamping(linearDamping);
    }

    public StyleableProperty<Number> linearVelocityXProperty() {
        return bodyPropertyDefinitions.linearVelocityXProperty();
    }
    public final double getLinearVelocityX() {
        return bodyPropertyDefinitions.getLinearVelocityX();
    }
    public final void setLinearVelocityX(double linearVelocityX) {
        bodyPropertyDefinitions.setLinearVelocityX(linearVelocityX);
    }

    public StyleableProperty<Number> linearVelocityYProperty() {
        return bodyPropertyDefinitions.linearVelocityYProperty();
    }
    public final double getLinearVelocityY() {
        return bodyPropertyDefinitions.getLinearVelocityY();
    }
    public final void setLinearVelocityY(double linearVelocityY) {
        bodyPropertyDefinitions.setLinearVelocityY(linearVelocityY);
    }

    public StyleableProperty<Number> angularDampingProperty() {
        return bodyPropertyDefinitions.angularDampingProperty();
    }
    public final double getAngularDamping() {
        return bodyPropertyDefinitions.getAngularDamping();
    }
    public final void setAngularDamping(double angularDamping) {
        bodyPropertyDefinitions.setAngularDamping(angularDamping);
    }

    public StyleableProperty<Number> gravityScaleProperty() {
        return bodyPropertyDefinitions.gravityScaleProperty();
    }
    public final double getGravityScale() {
        return bodyPropertyDefinitions.getGravityScale();
    }
    public final void setGravityScale(double gravityScale) {
        bodyPropertyDefinitions.setGravityScale(gravityScale);
    }

    public StyleableProperty<Boolean> allowSleepProperty() {
        return bodyPropertyDefinitions.allowSleepProperty();
    }
    public final boolean isAllowSleep() {
        return bodyPropertyDefinitions.isAllowSleep();
    }
    public final void setAllowSleep(boolean allowSleep) {
        bodyPropertyDefinitions.setAllowSleep(allowSleep);
    }

    public StyleableProperty<Boolean> awakeProperty() {
        return bodyPropertyDefinitions.awakeProperty();
    }
    public final boolean isAwake() {
        return bodyPropertyDefinitions.isAwake();
    }
    public final void setAwake(boolean awake) {
        bodyPropertyDefinitions.setAwake(awake);
    }

    public StyleableProperty<Boolean> fixedRotationProperty() {
        return bodyPropertyDefinitions.fixedRotationProperty();
    }
    public final boolean isFixedRotation() {
        return bodyPropertyDefinitions.isFixedRotation();
    }
    public final void setFixedRotation(boolean fixedRotation) {
        bodyPropertyDefinitions.setFixedRotation(fixedRotation);
    }

    public StyleableProperty<Boolean> activeProperty() {
        return bodyPropertyDefinitions.activeProperty();
    }
    public final boolean isActive() {
        return bodyPropertyDefinitions.isActive();
    }
    public final void setActive(boolean active) {
        bodyPropertyDefinitions.setActive(active);
    }

    public StyleableProperty<Boolean> bulletProperty() {
        return bodyPropertyDefinitions.bulletProperty();
    }
    public boolean isBullet() {
        return bodyPropertyDefinitions.isBullet();
    }
    public void setBullet(boolean bullet) {
        bodyPropertyDefinitions.setBullet(bullet);
    }

    public StyleableProperty<Number> densityProperty() {
        return this.fixturePropertyDefinitions.densityProperty();
    }
    public final double getDensity() {
        return this.fixturePropertyDefinitions.getDensity();
    }
    public final void setDensity(double density) {
        this.fixturePropertyDefinitions.setDensity(density);
    }

    public StyleableProperty<Number> frictionProperty() {
        return this.fixturePropertyDefinitions.frictionProperty();
    }
    public final double getFriction() {
        return this.fixturePropertyDefinitions.getFriction();
    }
    public final void setFriction(double friction) {
        this.fixturePropertyDefinitions.setFriction(friction);
    }

    public StyleableProperty<Number> restitutionProperty() {
        return this.fixturePropertyDefinitions.restitutionProperty();
    }
    public final double getRestitution() {
        return this.fixturePropertyDefinitions.getRestitution();
    }
    public final void setRestitution(double restitution) {
        this.fixturePropertyDefinitions.setRestitution(restitution);
    }

    public StyleableProperty<Boolean> sensorProperty() {
        return this.fixturePropertyDefinitions.sensorProperty();
    }
    public final boolean isSensor() {
        return this.fixturePropertyDefinitions.isSensor();
    }
    public final void setSensor(boolean sensor) {
        this.fixturePropertyDefinitions.setSensor(sensor);
    }

    public StyleableProperty<Number> angularVelocityProperty() {
        return bodyPropertyDefinitions.angularVelocityProperty();
    }
    public double getAngularVelocity() {
        return bodyPropertyDefinitions.getAngularVelocity();
    }
    public void setAngularVelocity(double angularVelocity) {
        bodyPropertyDefinitions.setAngularVelocity(angularVelocity);
    }

    public void setLocalCenterOffset(Vec2 vec) {
        this.localCenterOffset = vec;
    }
}
