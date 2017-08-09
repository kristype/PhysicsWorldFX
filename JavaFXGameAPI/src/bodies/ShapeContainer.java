package bodies;

import framework.SimulationType;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.CoordinateConverter;

import java.util.List;

public class ShapeContainer extends Pane implements BodyDefBeanOwner {

    private final BodyDefBean bodyDefBean;
    private Body body;
    private CoordinateConverter coordinateConverter;

    public ShapeContainer() {
        getStyleClass().add("shapeContainer");
        this.bodyDefBean = new BodyDefBean<>(this, SPF);
    }

    public void setup(Body body, CoordinateConverter coordinateConverter){
        this.body = body;
        this.coordinateConverter = coordinateConverter;
    }

    public Point2D getSpeed() {
        if (body != null){
            Vec2 velocity = body.getLinearVelocity();
            return coordinateConverter.scaleWorldToFx(velocity.x, velocity.y);
        }
        return null;
    }

    public void setSpeed(float vx, float vy) {
        if (body != null){
            Vec2 scaled = coordinateConverter.scaleVecToWorld(vx, vy);
            body.setLinearVelocity(scaled);
        }
    }

    public void setRotationSpeed(float speed) {
        if (body != null){
            double scaled = coordinateConverter.fxScaleToWorld(speed);
            body.setAngularVelocity((float)scaled);
        }
    }

    public void applyForce(float vx, float vy) {
        if (body != null){
            Vec2 scaled = coordinateConverter.scaleVecToWorld(vx, vy);
            body.applyForceToCenter(scaled);
        }
    }

    @Override
    public BodyDefBean<? extends Styleable> getBodyDefBean() {
        return bodyDefBean;
    }

    private static final StyleablePropertyFactory<ShapeContainer> SPF = new StyleablePropertyFactory<>(Pane.getClassCssMetaData());

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

    public StyleableProperty<Number> linearDampingProperty() {
        return bodyDefBean.linearDampingProperty();
    }
    public final Float getLinearDamping() {
        return bodyDefBean.getLinearDamping();
    }
    public final void setLinearDamping(Float linearDamping) {
        bodyDefBean.setLinearDamping(linearDamping);
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
