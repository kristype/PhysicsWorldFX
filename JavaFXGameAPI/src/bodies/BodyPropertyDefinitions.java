package bodies;

import framework.SimulationType;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import utilites.SimulationTypeToBodyTypeConverter;

public class BodyPropertyDefinitions<S extends Styleable> extends StyleFactory<S> {

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
    private final StyleableProperty<Number> angularVelocity;
    private final StyleableProperty<Boolean> bullet;

    public BodyPropertyDefinitions(S owner, StyleablePropertyFactory<S> spf) {
		super(owner, spf);
		this.bodyType = createStyleableEnumProperty("bodyType", s -> bodyTypeProperty(), SimulationType.class, SimulationType.Full);
		this.linearDamping = createStyleableNumberProperty("linearDamping", s -> linearDampingProperty(), 0.0);
		this.linearVelocityX = createStyleableNumberProperty("linearVelocityX", s -> linearVelocityXProperty(), 0.0);
		this.linearVelocityY = createStyleableNumberProperty("linearVelocityY", s -> linearVelocityYProperty(), 0.0);
		this.angularDamping = createStyleableNumberProperty("angularDamping", s -> angularDampingProperty(), 0.0);
		this.angularVelocity = createStyleableNumberProperty("angularVelocity", s -> angularVelocityProperty(), 0.0);
		this.gravityScale = createStyleableNumberProperty("gravityScale", s -> gravityScaleProperty(), 1.0);
		this.allowSleep = createStyleableBooleanProperty("allowSleep", s -> allowSleepProperty(), true);
		this.awake = createStyleableBooleanProperty("awake", s -> awakeProperty(), true);
		this.fixedRotation = createStyleableBooleanProperty("fixedRotation", s -> fixedRotationProperty(), false);
		this.active = createStyleableBooleanProperty("active", s -> activeProperty(), true);
		this.bullet = createStyleableBooleanProperty("bullet", s -> bulletProperty(), false);
	}

	public BodyDef createBodyDef(SimulationTypeToBodyTypeConverter converter) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = converter.Convert(this.getBodyType());
		bodyDef.linearDamping= (float)this.getLinearDamping();
		bodyDef.angularDamping = (float)this.getAngularDamping();
		bodyDef.gravityScale= (float)this.getGravityScale();
		bodyDef.allowSleep = this.isAllowSleep();
		bodyDef.awake = this.isAwake();
		bodyDef.fixedRotation = this.isFixedRotation();
		bodyDef.active = this.isActive();
		bodyDef.bullet = this.isBullet();
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
	
	public StyleableProperty<Number> linearDampingProperty() {
	    return linearDamping;
	}
	public final double getLinearDamping() {
		return linearDamping.getValue().doubleValue();
	}
	public final void setLinearDamping(double linearDamping) {
        this.linearDamping.setValue(linearDamping);
	}

	public StyleableProperty<Number> linearVelocityXProperty() {
		return linearVelocityX;
	}
	public final double getLinearVelocityX() {
		return linearVelocityXProperty().getValue().doubleValue();
	}
	public final void setLinearVelocityX(double linearVelocityX) {
		linearVelocityXProperty().setValue(linearVelocityX);
	}

	public StyleableProperty<Number> linearVelocityYProperty() {
		return linearVelocityY;
	}
	public final double getLinearVelocityY() {
		return linearVelocityYProperty().getValue().doubleValue();
	}
	public final void setLinearVelocityY(double linearVelocityY) {
		linearVelocityYProperty().setValue(linearVelocityY);
	}

    public StyleableProperty<Number> angularVelocityProperty() {
        return angularVelocity;
    }
    public final double getAngularVelocity() {
        return angularVelocityProperty().getValue().doubleValue();
    }
    public final void setAngularVelocity(double angularVelocity) {
	    angularVelocityProperty().setValue(angularVelocity);
    }

	public StyleableProperty<Number> angularDampingProperty() {
		return angularDamping;
	}
	public final double getAngularDamping() {
		return angularDampingProperty().getValue().doubleValue();
	}
	public final void setAngularDamping(double angularDamping) {
		angularDampingProperty().setValue(angularDamping);
	}
	
	public StyleableProperty<Number> gravityScaleProperty() {
		return gravityScale;
	}
	public final double getGravityScale() {
		return gravityScaleProperty().getValue().doubleValue();
	}
	public final void setGravityScale(double gravityScale) {
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

    public StyleableProperty<Boolean> bulletProperty() {
        return bullet;
    }
    public final boolean isBullet() {
        return bulletProperty().getValue();
    }
    public final void setBullet(boolean bullet) {
        bulletProperty().setValue(bullet);
    }
}
