package framework;

import bodies.StyleFactory;
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

    public ObservableValue<Double> gravityXProperty() {
        return (ObservableValue<Double>) gravityX;
    }
    public double getGravityX() {
        return gravityX.getValue().doubleValue();
    }
    public void setGravityX(double gravityX) {
        this.gravityX.setValue(gravityX);
    }

    public ObservableValue<Double> gravityYProperty() {
        return (ObservableValue<Double>) gravityY;
    }
    public double getGravityY() {
        return gravityY.getValue().doubleValue();
    }
    public void setGravityY(double gravityY) {
        this.gravityY.setValue(gravityY);
    }


    /** Can only be set before loading
     * @return physics scale property
     */
    public ObservableValue<Double> physicsScaleProperty() {
        return (ObservableValue<Double>)physicsScale;
    }
    public double getPhysicsScale() {
        return physicsScaleProperty().getValue().doubleValue();
    }
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

    public final void setOnPhysicsStep(
            EventHandler<? super PhysicsEvent> value) {
        onPhysicsStepProperty().set(value);
    }

    public final EventHandler<? super PhysicsEvent> getOnPhysicsStep() {
        return onPhysicsStepProperty().get();
    }

    public final ObjectProperty<EventHandler<? super PhysicsEvent>>
    onPhysicsStepProperty() {
        return physicsStepProperty;
    }

    public final void setOnBeginCollision(EventHandler<? super CollisionEvent> value) {
        onBeginCollisionProperty().set(value);
    }

    public final EventHandler<? super CollisionEvent> getOnBeginCollision() {
        return onBeginCollisionProperty().get();
    }

    public final ObjectProperty<EventHandler<? super CollisionEvent>> onBeginCollisionProperty() {
        return beginCollisionProperty;
    }

    public final void setOnEndCollision(EventHandler<? super CollisionEvent> value) {
        onEndCollisionProperty().set(value);
    }

    public final EventHandler<? super CollisionEvent> getOnEndCollision() {
        return onEndCollisionProperty().get();
    }

    public final ObjectProperty<EventHandler<? super CollisionEvent>> onEndCollisionProperty() {
        return endCollisionProperty;
    }

    public void finishLevel(boolean completed, int endState){
        if (onLevelFinish != null){
            EventType<LevelFinishedEvent> eventType = completed ?
                    LevelFinishedEvent.LEVEL_COMPLETE
                    : LevelFinishedEvent.LEVEL_FAILED;
            onLevelFinish.handleLevelFinishedEvent(new LevelFinishedEvent(eventType, endState));
        }
    }

    void setOnLevelFinish(LevelFinishEventListener onLevelFinish) {
        this.onLevelFinish = onLevelFinish;
    }
}

