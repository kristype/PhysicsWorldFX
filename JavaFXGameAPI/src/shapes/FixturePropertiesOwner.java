package shapes;

import javafx.css.Styleable;

public interface FixturePropertiesOwner {
	public FixturePropertyDefinitions<? extends Styleable> getFixturePropertyDefinitions();
}
