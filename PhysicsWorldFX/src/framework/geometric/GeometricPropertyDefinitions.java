package framework.geometric;

import framework.SimulationType;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import org.jbox2d.dynamics.BodyDef;
import utilites.SimulationTypeToBodyTypeConverter;
import utilites.StyleFactory;

public class GeometricPropertyDefinitions<S extends Styleable> extends StyleFactory<S> {

	private final StyleableProperty<SimulationType> simulationType;
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

    public GeometricPropertyDefinitions(S owner, StyleablePropertyFactory<S> spf) {
		super(owner, spf);
		this.simulationType = createStyleableEnumProperty("simulationType", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().simulationType, SimulationType.class, SimulationType.Full);
		this.linearDamping = createStyleableNumberProperty("linearDamping", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().linearDamping, 0.0);
		this.linearVelocityX = createStyleableNumberProperty("linearVelocityX", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().linearVelocityX, 0.0);
		this.linearVelocityY = createStyleableNumberProperty("linearVelocityY", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().linearVelocityY, 0.0);
		this.angularDamping = createStyleableNumberProperty("angularDamping", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().angularDamping, 0.0);
		this.angularVelocity = createStyleableNumberProperty("angularVelocity", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().angularVelocity, 0.0);
		this.gravityScale = createStyleableNumberProperty("gravityScale", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().gravityScale, 1.0);
		this.allowSleep = createStyleableBooleanProperty("allowSleep", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().allowSleep, true);
		this.awake = createStyleableBooleanProperty("awake", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().awake, true);
		this.fixedRotation = createStyleableBooleanProperty("fixedRotation", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().fixedRotation, false);
		this.active = createStyleableBooleanProperty("active", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().active, true);
		this.bullet = createStyleableBooleanProperty("bullet", s -> ((GeometricPropertiesOwner)s).getGeometricPropertyDefinitions().bullet, false);
	}

	public BodyDef createBodyDef(SimulationTypeToBodyTypeConverter converter) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = converter.Convert(this.getSimulationType());
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

	public ObservableValue<SimulationType> simulationTypeProperty() {
		return (ObservableValue<SimulationType>) simulationType;
	}
	public final SimulationType getSimulationType() {
		return simulationType.getValue();
	}
	public final void setSimulationType(SimulationType simulationType) {
		this.simulationType.setValue(simulationType);
	}
	
	public ObservableValue<Double> linearDampingProperty() {
	    return (ObservableValue<Double>)linearDamping;
	}
	public final double getLinearDamping() {
		return linearDamping.getValue().doubleValue();
	}
	public final void setLinearDamping(double linearDamping) {
        this.linearDamping.setValue(linearDamping);
	}

	public ObservableValue<Double> linearVelocityXProperty() {
		return (ObservableValue<Double>)linearVelocityX;
	}
	public final double getLinearVelocityX() {
		return linearVelocityX.getValue().doubleValue();
	}
	public final void setLinearVelocityX(double linearVelocityX) {
		this.linearVelocityX.setValue(linearVelocityX);
	}

	public ObservableValue<Double> linearVelocityYProperty() {
		return (ObservableValue<Double>)linearVelocityY;
	}
	public final double getLinearVelocityY() {
		return linearVelocityY.getValue().doubleValue();
	}
	public final void setLinearVelocityY(double linearVelocityY) {
		this.linearVelocityY.setValue(linearVelocityY);
	}

    public ObservableValue<Double> angularVelocityProperty() {
        return (ObservableValue<Double>)angularVelocity;
    }
    public final double getAngularVelocity() {
        return angularVelocity.getValue().doubleValue();
    }
    public final void setAngularVelocity(double angularVelocity) {
	    this.angularVelocity.setValue(angularVelocity);
    }

	public ObservableValue<Double> angularDampingProperty() {
		return (ObservableValue<Double>)angularDamping;
	}
	public final double getAngularDamping() {
		return angularDamping.getValue().doubleValue();
	}
	public final void setAngularDamping(double angularDamping) {
		this.angularDamping.setValue(angularDamping);
	}
	
	public ObservableValue<Double> gravityScaleProperty() {
		return (ObservableValue<Double>)gravityScale;
	}
	public final double getGravityScale() {
		return gravityScale.getValue().doubleValue();
	}
	public final void setGravityScale(double gravityScale) {
		this.gravityScale.setValue(gravityScale);
	}

	public ObservableValue<Boolean> allowSleepProperty() {
		return (ObservableValue<Boolean>) allowSleep;
	}
	public final boolean isAllowSleep() {
		return allowSleep.getValue();
	}
	public final void setAllowSleep(boolean allowSleep) {
		this.allowSleep.setValue(allowSleep);
	}
	
	public ObservableValue<Boolean> awakeProperty() {
		return (ObservableValue<Boolean>)  awake;
	}
	public final boolean isAwake() {
		return awake.getValue();
	}
	public final void setAwake(boolean awake) {
		this.awake.setValue(awake);
	}
	
	public ObservableValue<Boolean> fixedRotationProperty() {
		return (ObservableValue<Boolean>)  fixedRotation;
	}
	public final boolean isFixedRotation() {
		return fixedRotation.getValue();
	}
	public final void setFixedRotation(boolean fixedRotation) {
		this.fixedRotation.setValue(fixedRotation);
	}
	
	public ObservableValue<Boolean> activeProperty() {
		return (ObservableValue<Boolean>)  active;
	}
	public final boolean isActive() {
		return active.getValue();
	}
	public final void setActive(boolean active) {
		this.active.setValue(active);
	}

    public ObservableValue<Boolean> bulletProperty() {
        return (ObservableValue<Boolean>)  bullet;
    }
    public final boolean isBullet() {
        return bullet.getValue();
    }
    public final void setBullet(boolean bullet) {
        this.bullet.setValue(bullet);
    }
}
