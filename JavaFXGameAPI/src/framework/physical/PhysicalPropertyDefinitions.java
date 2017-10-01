package framework.physical;

import utilites.StyleFactory;
import javafx.beans.value.ObservableValue;
import org.jbox2d.dynamics.FixtureDef;

import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;

public class PhysicalPropertyDefinitions<S extends Styleable> extends StyleFactory<S> {

	private final StyleableProperty<Number> density;
	private final StyleableProperty<Number> friction;
	private final StyleableProperty<Number> restitution;
	private final StyleableProperty<Boolean> sensor;
    private final StyleableProperty<Number> filterMask;
    private final StyleableProperty<Number> filterCategory;
    private final StyleableProperty<Number> filterGroup;

    public PhysicalPropertyDefinitions(S owner, StyleablePropertyFactory<S> spf) {
		super(owner, spf);
		this.filterMask = createStyleableNumberProperty("collisionFilterMask", s -> ((PhysicalPropertiesOwner)s).getPhysicalPropertyDefinitions().filterMask, 0xFFFF);
		this.filterCategory = createStyleableNumberProperty("collisionFilterCategory", s -> ((PhysicalPropertiesOwner)s).getPhysicalPropertyDefinitions().filterCategory, 0x0001);
		this.filterGroup = createStyleableNumberProperty("collisionFilterGroup", s -> ((PhysicalPropertiesOwner)s).getPhysicalPropertyDefinitions().filterGroup, 0);
		this.density = createStyleableNumberProperty("density", s -> ((PhysicalPropertiesOwner)s).getPhysicalPropertyDefinitions().density, 1.0);
		this.friction = createStyleableNumberProperty("friction", s -> ((PhysicalPropertiesOwner)s).getPhysicalPropertyDefinitions().friction, 0.2);
		this.restitution = createStyleableNumberProperty("restitution", s -> ((PhysicalPropertiesOwner)s).getPhysicalPropertyDefinitions().restitution, 0.0);
		this.sensor = createStyleableBooleanProperty("sensor", s -> ((PhysicalPropertiesOwner)s).getPhysicalPropertyDefinitions().sensor, false);
	}

	public FixtureDef createFixtureDef() {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = (float)getDensity();
		fixtureDef.friction = (float)getFriction();
		fixtureDef.restitution = (float)getRestitution();
		fixtureDef.isSensor = isSensor();
		fixtureDef.filter.maskBits = getFilterMask();
		fixtureDef.filter.categoryBits = getFilterCategory();
        fixtureDef.filter.groupIndex = getFilterGroup();
        return fixtureDef;
	}

	public ObservableValue<Double> densityProperty() {
		return (ObservableValue<Double>)density;
	}
	public final double getDensity() {
		return density.getValue().doubleValue();
	}
	public final void setDensity(double density) {
		this.density.setValue(density);
	}
	
	public ObservableValue<Double> frictionProperty() {
		return (ObservableValue<Double>)friction;
	}
	public final double getFriction() {
		return friction.getValue().doubleValue();
	}
	public final void setFriction(double friction) {
		this.friction.setValue(friction);
	}
	
	public ObservableValue<Double> restitutionProperty() {
		return (ObservableValue<Double>)restitution;
	}
	public final double getRestitution() {
		return restitution.getValue().floatValue();
	}
	public final void setRestitution(double restitution) {
		this.restitution.setValue(restitution);
	}

	public ObservableValue<Boolean> sensorProperty() {
		return (ObservableValue<Boolean>)sensor;
	}
	public final boolean isSensor() {
		return sensor.getValue();
	}
	public final void setSensor(boolean sensor) {
		this.sensor.setValue(sensor);
	}

    public ObservableValue<Integer> filterMaskProperty() {
        return (ObservableValue<Integer>)filterMask;
    }
    public final int getFilterMask() {
        return filterMask.getValue().intValue();
    }
    public final void setFilterMask(int filterMask) {
        this.filterMask.setValue(filterMask);
    }

    public ObservableValue<Integer> filterCategoryProperty() {
        return (ObservableValue<Integer>)filterCategory;
    }
    public final int getFilterCategory() {
        return filterCategory.getValue().intValue();
    }
    public final void setFilterCategory(double filterCategory) {
        this.filterCategory.setValue(filterCategory);
    }

    public ObservableValue<Integer> filterGroupProperty() {
        return (ObservableValue<Integer>)filterGroup;
    }
    public final int getFilterGroup() {
        return filterGroup.getValue().intValue();
    }
    public final void setFilterGroup(int filterGroup) {
        this.filterGroup.setValue(filterGroup);
    }
}
