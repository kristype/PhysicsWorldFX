package shapes;

import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.shape.Circle;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import java.util.List;

public class PhysicsCircle extends Circle implements BodyDefBeanOwner, FixtureDefBeanOwner, PhysicsShape {
    private Body body;

    public void setup(Body body) {
        this.body = body;
    }

    public void setSpeed(float vx, float vy) {
        body.setLinearVelocity(new Vec2(vx, vy));
    }

    public void applyForce(float vx, float vy) {
        //body.applyLinearImpulse(new Vec2(vx, vy), body.getPosition());
        body.applyForceToCenter(new Vec2(vx, vy));
    }

    private static final StyleablePropertyFactory<PhysicsCircle> SPF = new StyleablePropertyFactory<>(Circle.getClassCssMetaData());

    public static  List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
    }

    private FixtureDefBean<PhysicsCircle> fixtureDefBean;
    private BodyDefBean<PhysicsCircle> bodyDefBean;

    public PhysicsCircle() {
        getStyleClass().add("circleBody");
        this.fixtureDefBean = new FixtureDefBean<PhysicsCircle>(this, SPF);
        this.bodyDefBean = new BodyDefBean<PhysicsCircle>(this, SPF);
    }

    @Override
    public FixtureDefBean<? extends Styleable> getFixtureDefBean() {
        return fixtureDefBean;
    }

    @Override
    public BodyDefBean<? extends Styleable> getBodyDefBean() {
        return bodyDefBean;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public StyleableProperty<BodyType> bodyTypeProperty() {
        return bodyDefBean.bodyTypeProperty();
    }
    public final BodyType getBodyType() {
        return bodyDefBean.getBodyType();
    }
    public final void setBodyType(BodyType bodyType) {
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

    @SuppressWarnings("unchecked")
    public ObservableValue<Number> densityProperty() {
        return (ObservableValue<Number>) this.fixtureDefBean.densityProperty();
    }
    public final Float getDensity() {
        return this.fixtureDefBean.densityProperty().getValue().floatValue();
    }
    public final void setDensity(Float density) {
        this.fixtureDefBean.densityProperty().setValue(density);
    }

    @SuppressWarnings("unchecked")
    public ObservableValue<Number> frictionProperty() {
        return (ObservableValue<Number>) this.fixtureDefBean.frictionProperty();
    }
    public final Float getFriction() {
        return this.fixtureDefBean.getFriction();
    }
    public final void setFriction(Float friction) {
        this.fixtureDefBean.setFriction(friction);
    }

    @SuppressWarnings("unchecked")
    public ObservableValue<Number> restitutionProperty() {
        return (ObservableValue<Number>) this.fixtureDefBean.restitutionProperty();
    }
    public final Float getRestitution() {
        return this.fixtureDefBean.getRestitution();
    }
    public final void setRestitution(Float restitution) {
        this.fixtureDefBean.setRestitution(restitution);
    }

    @SuppressWarnings("unchecked")
    public ObservableValue<Boolean> sensorProperty() {
        return (ObservableValue<Boolean>) this.fixtureDefBean.sensorProperty();
    }
    public final boolean isSensor() {
        return this.fixtureDefBean.isSensor();
    }
    public final void setSensor(boolean sensor) {
        this.fixtureDefBean.setSensor(sensor);
    }
}
