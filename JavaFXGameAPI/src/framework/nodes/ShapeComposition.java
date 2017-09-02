package framework.nodes;

import bodies.BodyPropertiesOwner;
import bodies.BodyPropertyDefinitions;
import framework.ChangedEvent;
import framework.ChangedEventListener;
import framework.SimulationType;
import javafx.beans.DefaultProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import shapes.PhysicsShapeWithLayout;
import utilites.PhysicsShapeHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapeComposition extends Pane implements BodyPropertiesOwner, PhysicsShapeWithLayout {

    private final BodyPropertyDefinitions bodyPropertyDefinitions;
    private Body body;
    private PhysicsShapeHelper helper;
    private List<ChangedEventListener> velocityChangedListeners;
    private List<ChangedEventListener> layoutChangedListeners;

    public ShapeComposition() {
        super();
        getStyleClass().add("shapeComposition");
        this.bodyPropertyDefinitions = new BodyPropertyDefinitions<>(this, SPF);
        velocityChangedListeners = new ArrayList<>();
        layoutChangedListeners = new ArrayList<>();
        layoutXProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        layoutYProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        ((SimpleStyleableObjectProperty<Number>) angularVelocityProperty()).addListener((observable, oldValue, newValue) -> raiseEvent(velocityChangedListeners));
        ((SimpleStyleableObjectProperty<Number>) linearVelocityXProperty()).addListener((observable, oldValue, newValue) -> raiseEvent(velocityChangedListeners));
        ((SimpleStyleableObjectProperty<Number>) linearVelocityYProperty()).addListener((observable, oldValue, newValue) -> raiseEvent(velocityChangedListeners));
    }

    public void setup(Body body, PhysicsShapeHelper helper){
        this.body = body;
        this.helper = helper;
    }

    public Point2D getSpeed() {
        return helper.getSpeed(body);
    }

    private void raiseEvent(List<ChangedEventListener> eventListeners){
        ChangedEvent event = new ChangedEvent(this);
        Iterator i = eventListeners.iterator();
        while(i.hasNext())  {
            ((ChangedEventListener) i.next()).handleChangedEvent(event);
        }
    }

    public void setSpeed(float vx, float vy) {
        helper.setSpeed(body, vx, vy);
    }

    public void applyForce(float vx, float vy) {
        helper.applyForce(body, new Vec2(), vx, vy);
    }

    public void applyForceUp(float vx, float vy) {
        helper.applyForceUp(body, new Vec2(), vx, vy);
    }

    public BodyPropertyDefinitions<? extends Styleable> getBodyPropertyDefinitions() {
        return bodyPropertyDefinitions;
    }

    private static final StyleablePropertyFactory<ShapeComposition> SPF = new StyleablePropertyFactory<>(Pane.getClassCssMetaData());

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
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

    public StyleableProperty<Number> angularVelocityProperty() {
        return bodyPropertyDefinitions.angularVelocityProperty();
    }

    public double getAngularVelocity() {
        return bodyPropertyDefinitions.getAngularVelocity();
    }
    public void setAngularVelocity(double angularVelocity) {
        bodyPropertyDefinitions.setAngularVelocity(angularVelocity);
    }

    public void addVelocityChangedEventListener(ChangedEventListener eventListener) {
        velocityChangedListeners.add(eventListener);
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

    @Override
    public void addLayoutChangedEventListener(ChangedEventListener eventListener) {
        layoutChangedListeners.add(eventListener);
    }
}
