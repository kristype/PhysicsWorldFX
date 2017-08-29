package shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bodies.BodyDefBean;
import bodies.BodyDefBeanOwner;
import framework.ChangedEvent;
import framework.ChangedEventListener;
import framework.SimulationType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.shape.Rectangle;
import utilites.CoordinateConverter;
import utilites.PhysicsShapeHelper;

public class PhysicsRectangle extends Rectangle implements BodyDefBeanOwner, FixtureDefBeanOwner, PhysicsShape {

    private Body body;

	private List<ChangedEventListener> sizeChangedListeners;
	private List<ChangedEventListener> layoutChangedListeners;
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

	public void setLocalCenterOffset(Vec2 vec) {
		this.localCenterOffset = vec;
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

	private static final StyleablePropertyFactory<PhysicsRectangle> SPF = new StyleablePropertyFactory<>(Rectangle.getClassCssMetaData());

	public static  List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
    }

	private FixtureDefBean<PhysicsRectangle> fixtureDefBean;
	private BodyDefBean<PhysicsRectangle> bodyDefBean;
	
	public PhysicsRectangle() {
		getStyleClass().add("rectangleBody");
		this.fixtureDefBean = new FixtureDefBean<>(this, SPF);
		this.bodyDefBean = new BodyDefBean<>(this, SPF);

		sizeChangedListeners = new ArrayList<>();
		layoutChangedListeners = new ArrayList<>();
		layoutXProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
		layoutYProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
		rotateProperty().addListener((observable, oldValue, newValue) -> raiseEvent(layoutChangedListeners));
		widthProperty().addListener((observable, oldValue, newValue) -> raiseEvent(sizeChangedListeners));
		heightProperty().addListener((observable, oldValue, newValue) -> raiseEvent(sizeChangedListeners));
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
