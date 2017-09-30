package utilites;

import java.util.function.Function;

import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;

public class StyleFactory<S extends Styleable> {

	protected final static String CSS_PREFIX = "-bx-";
	
	private final S owner;
	private final StyleablePropertyFactory<S> spf;
	
	public StyleFactory(S owner, StyleablePropertyFactory<S> spf) {
		this.owner = owner;
		this.spf = spf;
	}
	
	public StyleableProperty<Boolean> createStyleableBooleanProperty(String propertyName, Function<S, StyleableProperty<Boolean>> propFun, boolean initial) {
		return spf.createStyleableBooleanProperty(owner, propertyName, CSS_PREFIX + propertyName, propFun, initial);
	}
	
	public StyleableProperty<Number> createStyleableNumberProperty(String propertyName, Function<S, StyleableProperty<Number>> propFun, Number initial) {
		return spf.createStyleableNumberProperty(owner, propertyName, CSS_PREFIX + propertyName, propFun, initial);
	}
	
	public final <E extends Enum<E>> StyleableProperty<E> createStyleableEnumProperty(String propertyName, Function<S, StyleableProperty<E>> propFun, Class<E> enumClass, E initial) {
		return spf.createStyleableEnumProperty(owner, propertyName, CSS_PREFIX + propertyName, propFun, enumClass, initial);
	}
}