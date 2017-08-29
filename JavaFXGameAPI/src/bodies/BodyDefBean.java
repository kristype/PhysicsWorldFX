package bodies;

import framework.SimulationType;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Point2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import utilites.SimulationTypeToBodyTypeConverter;

public class BodyDefBean<S extends Styleable> extends Box2dBean<S> {

	private final StyleableProperty<SimulationType> bodyType;
	private final StyleableProperty<Number> linearDamping;
	private final StyleableProperty<Number> linearVelocityX;
	private final StyleableProperty<Number> linearVelocityY;
	private final StyleableProperty<Number> angularDamping;
	private final StyleableProperty<Number> gravityScale;
	private final StyleableProperty<Boolean> allowSleep;
	private final StyleableProperty<Boolean> awake;
	private final StyleableProperty<Boolean> fixedRotation;
	private final StyleableProperty<Boolean> active;

	public BodyDefBean(S owner, StyleablePropertyFactory<S> spf) {
		super(owner, spf);
		this.bodyType = createStyleableEnumProperty("bodyType", s -> bodyTypeProperty(), SimulationType.class, SimulationType.Full);
		this.linearDamping = createStyleableNumberProperty("linearDamping", s -> ((BodyDefBean)s).linearDamping, 0.0);
		this.linearVelocityX = createStyleableNumberProperty("linearVelocityX", s -> linearVelocityXProperty(), 0.0);
		this.linearVelocityY = createStyleableNumberProperty("linearVelocityY", s -> linearVelocityYProperty(), 0.0);
		this.angularDamping = createStyleableNumberProperty("angularDamping", s -> angularDampingProperty(), 0.0);
		this.gravityScale = createStyleableNumberProperty("gravityScale", s -> gravityScaleProperty(), 1.0);
		this.allowSleep = createStyleableBooleanProperty("allowSleep", s -> allowSleepProperty(), true);
		this.awake = createStyleableBooleanProperty("awake", s -> awakeProperty(), true);
		this.fixedRotation = createStyleableBooleanProperty("fixedRotation", s -> fixedRotationProperty(), false);
		this.active = createStyleableBooleanProperty("active", s -> activeProperty(), true);
	}

	public BodyDef createBodyDef(SimulationTypeToBodyTypeConverter converter) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = converter.Convert(this.getBodyType());
		bodyDef.linearDamping= this.getLinearDamping();
		bodyDef.linearVelocity= new Vec2(getLinearVelocityX(), getLinearVelocityY());
		bodyDef.angularDamping = this.getAngularDamping();
		bodyDef.gravityScale= this.getGravityScale();
		bodyDef.allowSleep = this.isAllowSleep();
		bodyDef.awake = this.isAwake();
		bodyDef.fixedRotation = this.isFixedRotation();
		bodyDef.active = this.isActive();
		return bodyDef;
	}

	public StyleableProperty<SimulationType> bodyTypeProperty() {
		return bodyType;
	}
	public final SimulationType getBodyType() {
		return bodyTypeProperty().getValue();
	}
	public final void setBodyType(SimulationType bodyType) {
		bodyTypeProperty().setValue(bodyType);
	}
	
	public ObservableValue<Float> linearDampingProperty() {
		return (ObservableValue<Float>) linearDamping;
	}
	public final Float getLinearDamping() {
		return linearDamping.getValue().floatValue();
	}
	public final void setLinearDamping(Float linearDamping) {
        this.linearDamping.setValue(linearDamping);
	}

	public StyleableProperty<Number> linearVelocityXProperty() {
		return linearVelocityX;
	}
	public final Float getLinearVelocityX() {
		return linearVelocityXProperty().getValue().floatValue();
	}
	public final void setLinearVelocityX(Float linearVelocityX) {
		linearVelocityXProperty().setValue(linearVelocityX);
	}

	public StyleableProperty<Number> linearVelocityYProperty() {
		return linearVelocityY;
	}
	public final Float getLinearVelocityY() {
		return linearVelocityYProperty().getValue().floatValue();
	}
	public final void setLinearVelocityY(Float linearVelocityY) {
		linearVelocityYProperty().setValue(linearVelocityY);
	}

	public StyleableProperty<Number> angularDampingProperty() {
		return angularDamping;
	}
	public final Float getAngularDamping() {
		return angularDampingProperty().getValue().floatValue();
	}
	public final void setAngularDamping(Float angularDamping) {
		angularDampingProperty().setValue(angularDamping);
	}
	
	public StyleableProperty<Number> gravityScaleProperty() {
		return gravityScale;
	}
	public final Float getGravityScale() {
		return gravityScaleProperty().getValue().floatValue();
	}
	public final void setGravityScale(Float gravityScale) {
		gravityScaleProperty().setValue(gravityScale);
	}
	
	public StyleableProperty<Boolean> allowSleepProperty() {
		return allowSleep;
	}
	public final boolean isAllowSleep() {
		return allowSleepProperty().getValue();
	}
	public final void setAllowSleep(boolean allowSleep) {
		allowSleepProperty().setValue(allowSleep);
	}
	
	public StyleableProperty<Boolean> awakeProperty() {
		return awake;
	}
	public final boolean isAwake() {
		return awakeProperty().getValue();
	}
	public final void setAwake(boolean awake) {
		awakeProperty().setValue(awake);
	}
	
	public StyleableProperty<Boolean> fixedRotationProperty() {
		return fixedRotation;
	}
	public final boolean isFixedRotation() {
		return fixedRotationProperty().getValue();
	}
	public final void setFixedRotation(boolean fixedRotation) {
		fixedRotationProperty().setValue(fixedRotation);
	}
	
	public StyleableProperty<Boolean> activeProperty() {
		return active;
	}
	public final boolean isActive() {
		return activeProperty().getValue();
	}
	public final void setActive(boolean active) {
		activeProperty().setValue(active);
	}
}
