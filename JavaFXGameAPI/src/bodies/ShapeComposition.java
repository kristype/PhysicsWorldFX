package bodies;

import framework.SimulationType;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.PhysicsShapeHelper;

import java.util.List;

public class ShapeComposition extends Pane implements BodyPropertiesOwner {

    private final BodyPropertyDefinitions bodyPropertyDefinitions;
    private Body body;
    private PhysicsShapeHelper helper;

    public ShapeComposition() {
        getStyleClass().add("shapeComposition");
        this.bodyPropertyDefinitions = new BodyPropertyDefinitions<>(this, SPF);
    }

    public void setup(Body body, PhysicsShapeHelper helper){
        this.body = body;
        this.helper = helper;
    }

    public Point2D getSpeed() {
        return helper.getSpeed(body);
    }

    public void setSpeed(float vx, float vy) {
        helper.setSpeed(body, vx, vy);
    }

    public void applyForce(float vx, float vy) {
        helper.applyForce(body, new Vec2(), vx, vy);
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
}
