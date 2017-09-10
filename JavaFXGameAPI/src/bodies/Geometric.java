package bodies;

import framework.SimulationType;
import javafx.beans.value.ObservableValue;


public interface Geometric {

    void applyForce(double vx, double vy);
    void applyForceToPoint(double px, double py, double vx, double vy);
    void applyForceUpToPoint(double px, double py, double vx, double vy);
    void applyForceUp(double vx, double vy);

    ObservableValue<SimulationType> bodyTypeProperty();
    SimulationType getBodyType();
    void setBodyType(SimulationType bodyType);

    ObservableValue<Double> linearDampingProperty();
    double getLinearDamping();
    void setLinearDamping(double linearDamping);

    ObservableValue<Double> linearVelocityXProperty();
    double getLinearVelocityX();
    void setLinearVelocityX(double linearVelocityX);

    ObservableValue<Double> linearVelocityYProperty();
    double getLinearVelocityY();
    void setLinearVelocityY(double linearVelocityY);

    ObservableValue<Double> angularVelocityProperty();
    double getAngularVelocity();
    void setAngularVelocity(double angularVelocity);

    ObservableValue<Double> angularDampingProperty();
    double getAngularDamping();
    void setAngularDamping(double angularDamping);

    ObservableValue<Double> gravityScaleProperty();
    double getGravityScale();
    void setGravityScale(double gravityScale);

    ObservableValue<Boolean> allowSleepProperty();
    boolean isAllowSleep();
    void setAllowSleep(boolean allowSleep);

    ObservableValue<Boolean> awakeProperty();
    boolean isAwake();
    void setAwake(boolean awake);

    ObservableValue<Boolean> fixedRotationProperty();
    boolean isFixedRotation();
    void setFixedRotation(boolean fixedRotation);

    ObservableValue<Boolean> activeProperty();
    boolean isActive();
    void setActive(boolean active);

    ObservableValue<Boolean> bulletProperty();
    boolean isBullet();
    void setBullet(boolean bullet);
}
