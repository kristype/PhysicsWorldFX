package bodies;

import framework.ChangedEventListener;
import framework.SimulationType;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

 public interface BodyPropertiesOwner {
	 BodyPropertyDefinitions<? extends Styleable> getBodyPropertyDefinitions();
 }
