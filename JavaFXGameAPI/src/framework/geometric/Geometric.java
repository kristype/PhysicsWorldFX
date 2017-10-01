package framework.geometric;

import framework.SimulationType;
import javafx.beans.value.ObservableValue;


public interface Geometric {

    /**
     * Applies a directional force to the rotation center of a node,
     * this can cause the node to rotate if the rotation center is not the same as the center of mass.
     *
     * @param  forceX  the force applied in the x axis
     * @param  forceY the force applied in the y axis
     */
    void applyForceToCenter(double forceX, double forceY);

    /**
     * Applies a directional force that is adjusted from the direction the node is facing
     * (which is 0 degrees when the node has no rotation), to the rotation center of a node,
     * this can cause the node to rotate if the rotation center is not the same as the center of mass.
     *
     * @param  upwardForceX  the upward force applied in the x axis
     * @param  upwardForceY the upward force applied in the y axis
     */
    void applyUpwardForceToCenter(double upwardForceX, double upwardForceY);

    /**
     * Applies a directional force to a given point in the layout of the node,
     * this can cause the node to rotate if the point is off center.
     *
     * @param  forceX  the force applied in the x axis
     * @param  forceY the force applied in the y axis
     * @param  localPointX the point on the x axis the force is applied
     * @param  localPointY the point on the y axis the force is applied
     */
    void applyForceToPoint(double localPointX, double localPointY, double forceX, double forceY);

    /**
     * Applies a directional force that is adjusted from the direction the node is facing
     * (which is 0 degrees when the node has no rotation), to a given point in the layout of the node,
     * this can cause the node to rotate if the point is off center.
     *
     * @param  upwardForceX  the upward force applied in the x axis
     * @param  upwardForceY the upward force applied in the y axis
     * @param  localPointX the point on the x axis the force is applied
     * @param  localPointY the point on the y axis the force is applied
     */
    void applyUpwardForceToPoint(double localPointX, double localPointY, double upwardForceX, double upwardForceY);

    /**
     * Applies a directional force to the center of mass
     *
     * @param  forceX  the force applied in the x axis
     * @param  forceY the force applied in the y axis
     */
    void applyForceToCenterOfMass(double forceX, double forceY);

    /**
     * Applies a directional force that is adjusted from the direction the node is facing
     * (which is 0 degrees when the node has no rotation), to the center of mass.
     *
     * @param  upwardForceX  the upward force applied in the x axis
     * @param  upwardForceY the upward force applied in the y axis
     */
    void applyUpwardForceToCenterOfMass(double upwardForceX, double upwardForceY);

    /**
     * Simulation type property
     * Default value: Full
     * @return the simulation type property
     */
    ObservableValue<SimulationType> simulationTypeProperty();

    /**
     * Gets the simulation type for the node.
     * @return the simulation type
     */
    SimulationType getSimulationType();

    /**
     * Sets the simulation type to one of the following options;
     *
     * Full: The node is fully simulated and will interact with the world and other nodes
     * NonMovable: Gives the node infinite mass witch results in that the node cannot be moved,
     *             but will interact with nodes that are fully simulated
     * Movable: Gives the node infinite mass, but can be moved and will interact with fully simulated nodes.
     *
     * NonMovable and Movable nodes cannot interact with other NonMovable or Movable nodes because they have infinite mass,
     * and it is therefore impossible to calculate interaction.
     * @param bodyType the body type this node should have
     */
    void setSimulationType(SimulationType bodyType);

    /**
     * Linear dampening property
     * Default value: 0
     * @return the linear dampening property
     */
    ObservableValue<Double> linearDampingProperty();

    /**
     * Gets the linear dampening value
     * @return the linear dampening value
     */
    double getLinearDamping();

    /**
     * Sets the linear dampening value
     * This value slows the linear velocity for each physics step
     * @param linearDamping
     */
    void setLinearDamping(double linearDamping);

    /**
     * Linear velocity property for the direction along the x axis
     * Default value: 0
     * @return the linear velocity x property
     */
    ObservableValue<Double> linearVelocityXProperty();

    /**
     * Linear velocity value for the direction along the x axis.
     * This value is updated after each physics step
     * @return the linear velocity x value
     */
    double getLinearVelocityX();

    /**
     * Sets the linear velocity for the direction along the x axis,
     * where negative numbers are left and positive numbers are right
     */
    void setLinearVelocityX(double linearVelocityX);

    /**
     * Linear velocity property for the direction along the y axis
     * Default value: 0
     * @return the linear velocity y property
     */
    ObservableValue<Double> linearVelocityYProperty();

    /**
     * Linear velocity value for the direction along the y axis.
     * This value is updated after each physics step
     * @return the linear velocity y value
     */
    double getLinearVelocityY();

    /**
     * Sets the linear velocity for the direction along the y axis,
     * where negative numbers are up and positive numbers are down
     */
    void setLinearVelocityY(double linearVelocityY);

    /**
     * Angular velocity property
     * Default value: 0
     * @return the angular velocity property
     */
    ObservableValue<Double> angularVelocityProperty();

    /**
     * Angular velocity value for the rotation speed the node currently has.
     * This value is updated after each physics step
     * @return the angular velocity value
     */
    double getAngularVelocity();

    /**
     * Sets the angular velocity which will make the node rotate,
     * Positive numbers makes the node rotate clockwise and negative numbers make the node rotate counter-clockwise.
     */
    void setAngularVelocity(double angularVelocity);

    /**
     * Angular dampening property
     * Default value: 0
     * @return the linear angular property
     */
    ObservableValue<Double> angularDampingProperty();

    /**
     * Gets the angular dampening value
     * @return the angular dampening value
     */
    double getAngularDamping();

    /**
     * Sets the angular dampening value
     * This value slows the angular velocity for each physics step
     * @param angularDamping
     */
    void setAngularDamping(double angularDamping);

    /**
     * Gravity scale property
     * Default value: 1
     * @return the gravity scale property
     */
    ObservableValue<Double> gravityScaleProperty();

    /**
     * Gravity scale value for the node
     * @return the gravity scale value
     */
    double getGravityScale();

    /**
     * Sets the gravity scale for the node
     *
     * Examples:
     * 0: the node will not be affected by gravity
     * 1: the node will be affected by gravity as normal
     * 2: the node will be affected by gravity twice as much as normal
     * @param gravityScale gravity scale value
     */
    void setGravityScale(double gravityScale);

    /**
     * Allow sleep property
     * Default value: true
     * @return the allow sleep property
     */
    ObservableValue<Boolean> allowSleepProperty();

    /**
     * Gets the allow sleep value
     * @return true if allow sleep and false if not
     */
    boolean isAllowSleep();

    /**
     * Set the allow sleep value.
     * This value allows the node to skip physics simulations when not needed
     * @param allowSleep
     */
    void setAllowSleep(boolean allowSleep);

    /**
     * Awake property
     * Default value: true
     * @return the awake property
     */
    ObservableValue<Boolean> awakeProperty();

    /**
     * Gets the awake value
     * @return true if awake and false if not
     */
    boolean isAwake();

    /**
     * Set the awake value.
     * This value allows the node to skip physics simulations when not needed,
     * but is woken up when a node collides with this node
     * @param awake
     */
    void setAwake(boolean awake);

    /**
     * Fixed rotation property
     * Default value: false
     * @return the fixed rotation property
     */
    ObservableValue<Boolean> fixedRotationProperty();

    /**
     * Gets the fixed rotation value
     * @return true if the rotation is fixed and false if it is not fixed
     */
    boolean isFixedRotation();

    /**
     * Sets the fixed rotation value, this makes the node unaffected by angular velocity calculated by the physics engine
     * @param fixedRotation true for fixed rotation, false for free rotation during physics calculations
     */
    void setFixedRotation(boolean fixedRotation);

    /**
     * Active property
     * Default value: true
     * @return the active property
     */
    ObservableValue<Boolean> activeProperty();

    /**
     * Gets the active value
     * @return true if active and false if not
     */
    boolean isActive();

    /**
     * Set the active value.
     * This value allows the node to skip physics simulations
     * @param active
     */
    void setActive(boolean active);

    /**
     * Bullet property
     * Default value: false
     * @return the bullet property
     */
    ObservableValue<Boolean> bulletProperty();

    /**
     * Gets the bullet value
     * @return the bullet value
     */
    boolean isBullet();

    /**
     * Sets the bullet value, the bullet value is a special property intended for objects traveling so fast
     * that they can skip an object between two physics steps,
     * this property will make the node collide where it would otherwise skip the collision because of the speed.
     * Physics calculations are more costly with this property enabled
     * @param bullet true for bullet state, and false for normal state
     */
    void setBullet(boolean bullet);
}
