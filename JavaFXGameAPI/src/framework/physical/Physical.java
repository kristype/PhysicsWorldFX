package framework.physical;

import framework.events.ChangedEventListener;
import javafx.beans.value.ObservableValue;
import org.jbox2d.common.Vec2;


public interface Physical {

    /**
     * Density property
     * Default value: 1
     * @return the density property
     */
    ObservableValue<Double> densityProperty();

    /**
     * Gets the density value
     * @return the density value
     */
    double getDensity();

    /**
     * Sets the density value, this value controls how much density the mass has
     * @param density the density value
     */
    void setDensity(double density);

    /**
     * Friction property
     * Default value: 0.2
     * @return the friction property
     */
    ObservableValue<Double> frictionProperty();

    /**
     * Gets the friction value
     * @return the friction value
     */
    double getFriction();

    /**
     * Sets the friction value, this value controls how much velocity an object loses
     * when in contact with another object
     * @param friction the friction value
     */
    void setFriction(double friction);

    /**
     * Restitution property
     * Default value: 0
     * @return the restitution property
     */
    ObservableValue<Double> restitutionProperty();

    /**
     * Gets the restitution value
     * @return the restitution value
     */
    double getRestitution();

    /**
     * Sets the restitution value, this value controls how much of the energy is lost when colliding with an object.
     * A restitution value of 1 will make the object retain all it's energy when colliding with another object,
     * this can make the object bouncy
     * A restitution value of 0 will make the object lose all it's energy when colliding with an object
     * @param restitution the restitution value
     */
    void setRestitution(double restitution);

    /**
     * Sensor property
     * Default value: false
     * @return the sensor property
     */
    ObservableValue<Boolean> sensorProperty();

    /**
     * Gets the sensor value
     * @return true if it is a sensor, false if it is not a sensor
     */
    boolean isSensor();

    /**
     * Sets the sensor value, this value determines if the object should interact with other objects.
     * A sensor value of true will make the object never interact with other object
     * and can be used to check if another object is within this sensor object.
     * @param sensor the sensor value
     */
    void setSensor(boolean sensor);

    /**
     * Collision filter mask property
     * Default value: 0x0001
     * @return The collision filter category property
     */
    ObservableValue<Integer> collisionFilterCategoryProperty();

    /**
     * Gets the collision filter category
     * @return the collision filter category value
     */
    int getCollisionFilterCategory();

    /**
     * The collision category bits. Normally you would just set one bit.
     * @param filterCategory the filter category value
     */
    void setCollisionFilterCategory(int filterCategory);

    /**
     * Collision filter mask property
     * Default value: 0xFFFF
     * @return The collision filter mask property
     */
    ObservableValue<Integer> collisionFilterMaskProperty();

    /**
     * Gets the collision filter mask
     * @return the collision filter mask value
     */
    int getCollisionFilterMask();

    /**
     * The collision mask bits. This states the categories that this
     * node should accept for collision.
     * @param filterMask the filter mask value
     */
    void setCollisionFilterMask(int filterMask);

    /**
     * Collision filter group property
     * Default value: 0
     * @return The collision filter group property
     */
    ObservableValue<Integer> collisionFilterGroupProperty();

    /**
     * Gets the collision filter group
     * @return the collision filter group value
     */
    int getCollisionFilterGroup();

    /**
     * Collision groups allow a certain group of objects to never collide (negative)
     * or always collide (positive). Zero means no collision group. Non-zero group
     * filtering always wins against the mask bits.
     * @param filterGroup the filter group value
     */
    void setCollisionFilterGroup(int filterGroup);

    /**
     * For internal use
     */
    void addSizeChangedEventListener(ChangedEventListener eventListener);

    /**
     * For internal use
     */
    void setLocalCenterOffset(Vec2 vec2);
}
