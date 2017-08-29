package bodies;

import framework.SimulationType;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.CoordinateConverter;
import utilites.PhysicsShapeHelper;

import java.util.List;

public class ShapeComposition extends Pane implements BodyDefBeanOwner {

    private final BodyDefBean bodyDefBean;
    private Body body;
    private PhysicsShapeHelper helper;

    public ShapeComposition() {
        getStyleClass().add("shapeComposition");
        this.bodyDefBean = new BodyDefBean<>(this, SPF);
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

    @Override
    public BodyDefBean<? extends Styleable> getBodyDefBean() {
        return bodyDefBean;
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
        return bodyDefBean.bodyTypeProperty();
    }
    public final SimulationType getBodyType() {
        return bodyDefBean.getBodyType();
    }
    public final void setBodyType(SimulationType bodyType) {
        bodyDefBean.setBodyType(bodyType);
    }

    public ObservableValue<Float> linearDampingProperty() {
        return bodyDefBean.linearDampingProperty();
    }
    public final Float getLinearDamping() {
        return bodyDefBean.getLinearDamping();
    }
    public final void setLinearDamping(Float linearDamping) {
        bodyDefBean.setLinearDamping(linearDamping);
    }

    public StyleableProperty<Number> linearVelocityXProperty() {
        return bodyDefBean.linearVelocityXProperty();
    }
    public final Float getLinearVelocityX() {
        return bodyDefBean.getLinearVelocityX();
    }
    public final void setLinearVelocityX(Float linearVelocityX) {
        bodyDefBean.setLinearVelocityX(linearVelocityX);
    }

    public StyleableProperty<Number> linearVelocityYProperty() {
        return bodyDefBean.linearVelocityYProperty();
    }
    public final Float getLinearVelocityY() {
        return bodyDefBean.getLinearVelocityY();
    }
    public final void setLinearVelocityY(Float linearVelocityY) {
        bodyDefBean.setLinearVelocityY(linearVelocityY);
    }

    public StyleableProperty<Number> angularDampingProperty() {
        return bodyDefBean.angularDampingProperty();
    }
    public final Float getAngularDamping() {
        return bodyDefBean.getAngularDamping();
    }
    public final void setAngularDamping(Float angularDamping) {
        bodyDefBean.setAngularDamping(angularDamping);
    }

    public StyleableProperty<Number> gravityScaleProperty() {
        return bodyDefBean.gravityScaleProperty();
    }
    public final Float getGravityScale() {
        return bodyDefBean.getGravityScale();
    }
    public final void setGravityScale(Float gravityScale) {
        bodyDefBean.setGravityScale(gravityScale);
    }

    public StyleableProperty<Boolean> allowSleepProperty() {
        return bodyDefBean.allowSleepProperty();
    }
    public final boolean isAllowSleep() {
        return bodyDefBean.isAllowSleep();
    }
    public final void setAllowSleep(boolean allowSleep) {
        bodyDefBean.setAllowSleep(allowSleep);
    }

    public StyleableProperty<Boolean> awakeProperty() {
        return bodyDefBean.awakeProperty();
    }
    public final boolean isAwake() {
        return bodyDefBean.isAwake();
    }
    public final void setAwake(boolean awake) {
        bodyDefBean.setAwake(awake);
    }

    public StyleableProperty<Boolean> fixedRotationProperty() {
        return bodyDefBean.fixedRotationProperty();
    }
    public final boolean isFixedRotation() {
        return bodyDefBean.isFixedRotation();
    }
    public final void setFixedRotation(boolean fixedRotation) {
        bodyDefBean.setFixedRotation(fixedRotation);
    }

    public StyleableProperty<Boolean> activeProperty() {
        return bodyDefBean.activeProperty();
    }
    public final boolean isActive() {
        return bodyDefBean.isActive();
    }
    public final void setActive(boolean active) {
        bodyDefBean.setActive(active);
    }
}
