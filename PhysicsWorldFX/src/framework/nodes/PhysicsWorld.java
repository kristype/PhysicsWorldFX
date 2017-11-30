package framework.nodes;

import utilites.StyleFactory;
import framework.events.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.layout.Pane;

import java.util.List;

public class PhysicsWorld extends Pane {

    private StyleableProperty<Number> physicsScale;
    private StyleableProperty<Number> gravityX;
    private StyleableProperty<Number> gravityY;

    private SimpleObjectProperty<EventHandler<? super PhysicsEvent>> physicsStepProperty = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<EventHandler<? super CollisionEvent>> beginCollisionProperty = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<EventHandler<? super CollisionEvent>> endCollisionProperty = new SimpleObjectProperty<>(null);
    private LevelFinishEventListener onLevelFinish;

    public PhysicsWorld(){
        StyleFactory<PhysicsWorld> styleDefinition = new StyleFactory<>(this, SPF);
        setFocusTraversable(true);
        setFocused(true);

        physicsScale = styleDefinition.createStyleableNumberProperty("physicsScale", s -> s.physicsScale, 30.0);
        gravityX = styleDefinition.createStyleableNumberProperty("gravityX", s -> s.gravityX, 0.0);
        gravityY = styleDefinition.createStyleableNumberProperty("gravityY", s -> s.gravityY, 0.0);

    }

    /**
     * Gravity x property
     * @return the gravity x property
     */
    public ObservableValue<Double> gravityXProperty() {
        return (ObservableValue<Double>) gravityX;
    }

    /**
     * Gets the gravity x value
     * @return the gravity x value
     */
    public double getGravityX() {
        return gravityX.getValue().doubleValue();
    }

    /**
     * Sets the gravity x value, this value determines the gravity along the x axis in the world
     * @param gravityX the gravity x value
     */
    public void setGravityX(double gravityX) {
        this.gravityX.setValue(gravityX);
    }

    /**
     * Gravity y property
     * @return the gravity y property
     */
    public ObservableValue<Double> gravityYProperty() {
        return (ObservableValue<Double>) gravityY;
    }

    /**
     * Gets the gravity y value
     * @return the gravity y value
     */
    public double getGravityY() {
        return gravityY.getValue().doubleValue();
    }

    /**
     * Sets the gravity y value, this value determines the gravity along the y axis in the world
     * A positive gravity y value will give gravity down
     * @param gravityY the gravity y value
     */
    public void setGravityY(double gravityY) {
        this.gravityY.setValue(gravityY);
    }


    /**
     * Physics scale property
     * @return physics scale property
     */
    public ObservableValue<Double> physicsScaleProperty() {
        return (ObservableValue<Double>)physicsScale;
    }

    /**
     * Gets the physics scale value
     * @return the physics scale value
     */
    public double getPhysicsScale() {
        return physicsScaleProperty().getValue().doubleValue();
    }

    /**
     * Can only be set before loading
     * Sets the physics scale value
     * This value determines how many pixels one meter is in the physics world,
     * this impacts the performance of the physics calculations,
     * and how much an object moves across the screen at a given velocity
     * @param value the physics scale value
     */
    public void setPhysicsScale(double value){
        physicsScale.setValue(value);
    }

    private static final StyleablePropertyFactory<PhysicsWorld> SPF = new StyleablePropertyFactory<>(Pane.getClassCssMetaData());

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }


    /**
     * Sets the physics step handler, this event will fire after each physics calculation
     * @param value physics step handler
     */
    public final void setOnPhysicsStep(EventHandler<? super PhysicsEvent> value) {
        onPhysicsStepProperty().set(value);
    }

    /**
     * Gets the physics step handler
     * @return the physics step handler
     */
    public final EventHandler<? super PhysicsEvent> getOnPhysicsStep() {
        return onPhysicsStepProperty().get();
    }


    /**
     * Physics step property
     * @return the physics step property
     */
    public final ObjectProperty<EventHandler<? super PhysicsEvent>> onPhysicsStepProperty() {
        return physicsStepProperty;
    }

    /**
     * Sets the begin collision handler, this event will fire when two objects collide,
     * but before collision forces have been calculated
     * @param value
     */
    public final void setOnBeginCollision(EventHandler<? super CollisionEvent> value) {
        onBeginCollisionProperty().set(value);
    }

    /**
     * Gets the begin collision event handler
     * @return the begin collision event handler
     */
    public final EventHandler<? super CollisionEvent> getOnBeginCollision() {
        return onBeginCollisionProperty().get();
    }

    /**
     * Begin collision property
     * @return the begin collision property
     */
    public final ObjectProperty<EventHandler<? super CollisionEvent>> onBeginCollisionProperty() {
        return beginCollisionProperty;
    }

    /**
     * Sets the end collision event handler, this event will fire after two objects have separated and are no longer touching
     * @param value end collision event handler
     */
    public final void setOnEndCollision(EventHandler<? super CollisionEvent> value) {
        onEndCollisionProperty().set(value);
    }

    /**
     * Gets the end collision event handler
     * @return the end collision event handler
     */
    public final EventHandler<? super CollisionEvent> getOnEndCollision() {
        return onEndCollisionProperty().get();
    }

    /**
     * End collision property
     * @return the end collision property
     */
    public final ObjectProperty<EventHandler<? super CollisionEvent>> onEndCollisionProperty() {
        return endCollisionProperty;
    }

    /**
     * Sends a message to the PhysicsGame that holds this PhysicsWorld that the level has ended
     * @param completed has the level finished because of failure or success
     * @param endState the end state, this can be points earned on the level
     */
    public void finishLevel(boolean completed, int endState){
        if (onLevelFinish != null){
            EventType<LevelFinishedEvent> eventType = completed ?
                    LevelFinishedEvent.LEVEL_COMPLETE
                    : LevelFinishedEvent.LEVEL_FAILED;
            onLevelFinish.handleLevelFinishedEvent(new LevelFinishedEvent(eventType, endState));
        }
    }

    public void setOnLevelFinish(LevelFinishEventListener onLevelFinish) {
        this.onLevelFinish = onLevelFinish;
    }
}

