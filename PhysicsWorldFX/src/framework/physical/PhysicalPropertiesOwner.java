package framework.physical;

import javafx.css.Styleable;

public interface PhysicalPropertiesOwner {
	public PhysicalPropertyDefinitions<? extends Styleable> getPhysicalPropertyDefinitions();
}
