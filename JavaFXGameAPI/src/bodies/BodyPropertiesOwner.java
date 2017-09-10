package bodies;

import javafx.css.Styleable;

public interface BodyPropertiesOwner {
	 BodyPropertyDefinitions<? extends Styleable> getBodyPropertyDefinitions();
 }
