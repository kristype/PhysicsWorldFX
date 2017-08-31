package bodies;

import javafx.css.Styleable;

public interface BodyPropertiesOwner {
	public BodyPropertyDefinitions<? extends Styleable> getBodyPropertyDefinitions();
}
