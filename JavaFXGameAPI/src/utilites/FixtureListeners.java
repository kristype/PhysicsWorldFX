package utilites;

import javafx.beans.value.ChangeListener;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import shapes.Physical;

public class FixtureListeners {

    private ChangeListener<Double> densityChangedListener ;
    private ChangeListener<Double> frictionChangedListener;
    private ChangeListener<Boolean> sensorChangedListener;
    private ChangeListener<Double> restitutionChangedListener;
    private ChangeListener<Integer> filterChangedListener;

    public FixtureListeners(Physical node, Fixture fixture){
        densityChangedListener = (observable, oldValue, newValue) -> onDensityChanged(fixture, newValue);
        frictionChangedListener = (observable, oldValue, newValue) -> onFrictionChanged(fixture, newValue);
        sensorChangedListener = (observable, oldValue, newValue) -> onSensorChanged(fixture, newValue);
        restitutionChangedListener = (observable, oldValue, newValue) -> onRestitutionChanged(fixture, newValue);
        filterChangedListener = (observable, oldValue, newValue) -> onFilterChanged(fixture, node);


        node.densityProperty().addListener(densityChangedListener);
        node.frictionProperty().addListener(frictionChangedListener);
        node.restitutionProperty().addListener(restitutionChangedListener);
        node.filterMaskProperty().addListener(filterChangedListener);
        node.filterCategoryProperty().addListener(filterChangedListener);
        node.filterGroupProperty().addListener(filterChangedListener);
        node.sensorProperty().addListener(sensorChangedListener);
    }

    public void RemoveListeners(Physical node){
        node.densityProperty().removeListener(densityChangedListener);
        node.frictionProperty().removeListener(frictionChangedListener);
        node.restitutionProperty().removeListener(restitutionChangedListener);
        node.filterMaskProperty().removeListener(filterChangedListener);
        node.filterCategoryProperty().removeListener(filterChangedListener);
        node.filterGroupProperty().removeListener(filterChangedListener);
        node.sensorProperty().removeListener(sensorChangedListener);
    }

    private void onSensorChanged(Fixture fixture, Boolean newValue) {
        fixture.setSensor(newValue);
    }

    private void onFilterChanged(Fixture fixture, Physical node) {
        Filter filter = new Filter();
        filter.maskBits = node.getFilterMask();
        filter.groupIndex = node.getFilterGroup();
        filter.categoryBits = node.getFilterCategory();
        fixture.getFilterData().set(filter);
    }

    private void onRestitutionChanged(Fixture fixture, Double newValue) {
        fixture.setRestitution(newValue.floatValue());
    }

    private void onFrictionChanged(Fixture fixture, Double newValue) {
        fixture.setFriction(newValue.floatValue());
    }

    private void onDensityChanged(Fixture fixture, Double newValue) {
        fixture.setDensity(newValue.floatValue());
    }

}
