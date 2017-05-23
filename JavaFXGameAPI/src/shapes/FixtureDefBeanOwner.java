package shapes;

import javafx.css.Styleable;

public interface FixtureDefBeanOwner {
	public FixtureDefBean<? extends Styleable> getFixtureDefBean();
}
