package shapes;

import framework.events.ChangedEventListener;
import javafx.beans.value.ObservableValue;
import org.jbox2d.common.Vec2;


public interface Physical {
    ObservableValue<Double> densityProperty();
    double getDensity();
    void setDensity(double density);

    ObservableValue<Double> frictionProperty();
    double getFriction();
    void setFriction(double friction);

    ObservableValue<Double> restitutionProperty();
    double getRestitution();
    void setRestitution(double restitution);

    ObservableValue<Boolean> sensorProperty();
    boolean isSensor();
    void setSensor(boolean sensor);

    ObservableValue<Integer> filterMaskProperty();
    int getFilterMask();
    void setFilterMask(int filterMask);

    ObservableValue<Integer> filterCategoryProperty();
    int getFilterCategory();
    void setFilterCategory(int filterCategory);

    ObservableValue<Integer> filterGroupProperty();
    int getFilterGroup();
    void setFilterGroup(int filterGroup);

    void addSizeChangedEventListener(ChangedEventListener eventListener);
    void setLocalCenterOffset(Vec2 vec2);
}
