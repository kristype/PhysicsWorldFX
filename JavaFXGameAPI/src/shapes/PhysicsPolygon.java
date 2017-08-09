package shapes;

import bodies.BodyDefBean;
import bodies.BodyDefBeanOwner;
import framework.ChangedEvent;
import framework.ChangedEventListener;
import framework.SimulationType;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import utilites.CoordinateConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhysicsPolygon extends Polygon implements BodyDefBeanOwner, FixtureDefBeanOwner, PhysicsShape {

    private Body body;
    private CoordinateConverter coordinateConverter;

    private List<ChangedEventListener> sizeChangedListeners;
    private List<ChangedEventListener> layoutChangedListeners;

    public void setup(Body body, CoordinateConverter coordinateConverter){
        this.body = body;
        this.coordinateConverter = coordinateConverter;
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

    public Point2D getSpeed() {
        if (body != null){
            Vec2 velocity = body.getLinearVelocity();
            return coordinateConverter.fxVec2world(velocity.x, velocity.y);
        }
        return null;
    }

    public void setSpeed(float vx, float vy) {
        if (body != null){
            Vec2 scaled = coordinateConverter.scaleVecToWorld(vx, vy);
            body.setLinearVelocity(scaled);
        }
    }

    public void applyForce(float vx, float vy) {
        if (body != null){
            Vec2 scaled = coordinateConverter.scaleVecToWorld(vx, vy);
            body.applyForceToCenter(scaled);
        }
    }

    private static final StyleablePropertyFactory<PhysicsPolygon> SPF = new StyleablePropertyFactory<>(Polygon.getClassCssMetaData());

    public static  List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
    }

    private FixtureDefBean<PhysicsPolygon> fixtureDefBean;
    private BodyDefBean<PhysicsPolygon> bodyDefBean;

    public PhysicsPolygon() {
        getStyleClass().add("physicsPolygon");
        this.fixtureDefBean = new FixtureDefBean<>(this, SPF);
        this.bodyDefBean = new BodyDefBean<>(this, SPF);

        sizeChangedListeners = new ArrayList<>();
        layoutChangedListeners = new ArrayList<>();
        layoutXProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        layoutYProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        rotateProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
        getPoints().addListener((ListChangeListener<? super Double>) e -> raiseEvent(sizeChangedListeners));
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
