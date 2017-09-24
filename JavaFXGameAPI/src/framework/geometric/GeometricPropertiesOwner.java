package framework.geometric;

import javafx.css.Styleable;

public interface GeometricPropertiesOwner {
	 GeometricPropertyDefinitions<? extends Styleable> getGeometricPropertyDefinitions();
 }
