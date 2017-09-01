package bodies;

import framework.ChangedEventListener;
import framework.SimulationType;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

 public interface BodyPropertiesOwner {
	 BodyPropertyDefinitions<? extends Styleable> getBodyPropertyDefinitions();

	 StyleableProperty<SimulationType> bodyTypeProperty();
     SimulationType getBodyType();
     void setBodyType(SimulationType bodyType);

	 StyleableProperty<Number> linearDampingProperty();
     double getLinearDamping();
     void setLinearDamping(double linearDamping);

	 StyleableProperty<Number> linearVelocityXProperty();
     double getLinearVelocityX();
     void setLinearVelocityX(double linearVelocityX);

	 StyleableProperty<Number> linearVelocityYProperty();
     double getLinearVelocityY();
     void setLinearVelocityY(double linearVelocityY);

	 StyleableProperty<Number> angularVelocityProperty();
     double getAngularVelocity();
     void setAngularVelocity(double angularVelocity);

	 StyleableProperty<Number> angularDampingProperty();
     double getAngularDamping();
     void setAngularDamping(double angularDamping);

	 StyleableProperty<Number> gravityScaleProperty();
     double getGravityScale();
     void setGravityScale(double gravityScale);

	 StyleableProperty<Boolean> allowSleepProperty();
     boolean isAllowSleep();
     void setAllowSleep(boolean allowSleep);

	 StyleableProperty<Boolean> awakeProperty();
     boolean isAwake();
     void setAwake(boolean awake);

	 StyleableProperty<Boolean> fixedRotationProperty();
     boolean isFixedRotation();
     void setFixedRotation(boolean fixedRotation);

	 StyleableProperty<Boolean> activeProperty();
     boolean isActive();
     void setActive(boolean active);

     StyleableProperty<Boolean> bulletProperty();
     boolean isBullet();
     void setBullet(boolean bullet);

     void addVelocityChangedEventListener(ChangedEventListener eventListener);
 }
