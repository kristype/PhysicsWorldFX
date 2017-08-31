package shapes;

import bodies.StyleFactory;
import org.jbox2d.dynamics.FixtureDef;

import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;

public class FixturePropertyDefinitions<S extends Styleable> extends StyleFactory<S> {

	private final StyleableProperty<Number> density;
	private final StyleableProperty<Number> friction;
	private final StyleableProperty<Number> restitution;
	private final StyleableProperty<Boolean> sensor;

	public FixturePropertyDefinitions(S owner, StyleablePropertyFactory<S> spf) {
		super(owner, spf);
		this.density = createStyleableNumberProperty("density", s -> densityProperty(), 0.0);
		this.friction = createStyleableNumberProperty("friction", s -> frictionProperty(), 0.0);
		this.restitution = createStyleableNumberProperty("restitution", s -> restitutionProperty(), 1.0);
		this.sensor = createStyleableBooleanProperty("sensor", s -> sensorProperty(), false);
	}

	public FixtureDef createFixtureDef() {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = (float)this.getDensity();
		fixtureDef.friction = (float)this.getFriction();
		fixtureDef.restitution = (float)this.getRestitution();
		fixtureDef.isSensor = this.isSensor();
		return fixtureDef;
	}

	public StyleableProperty<Number> densityProperty() {
		return density;
	}
	public final double getDensity() {
		return densityProperty().getValue().floatValue();
	}
	public final void setDensity(double density) {
		densityProperty().setValue(density);
	}
	
	public StyleableProperty<Number> frictionProperty() {
		return friction;
	}
	public final double getFriction() {
		return frictionProperty().getValue().floatValue();
	}
	public final void setFriction(double friction) {
		frictionProperty().setValue(friction);
	}
	
	public StyleableProperty<Number> restitutionProperty() {
		return restitution;
	}
	public final double getRestitution() {
		return restitutionProperty().getValue().floatValue();
	}
	public final void setRestitution(double restitution) {
		restitutionProperty().setValue(restitution);
	}

	public StyleableProperty<Boolean> sensorProperty() {
		return sensor;
	}
	public final boolean isSensor() {
		return sensorProperty().getValue();
	}
	public final void setSensor(boolean sensor) {
		sensorProperty().setValue(sensor);
	}
}
