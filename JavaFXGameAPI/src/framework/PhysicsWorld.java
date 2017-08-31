package framework;

import bodies.StyleFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhysicsWorld extends Pane {

    private StyleableProperty<Number> physicsScale;
    private StyleableProperty<Number> gravityX;
    private StyleableProperty<Number> gravityY;

    private SimpleObjectProperty<EventHandler<? super PhysicsEvent>> physicsStepProperty = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<EventHandler<? super CollisionEvent>> collisionProperty = new SimpleObjectProperty<>(null);
    private Action onLevelEnd;

    public PhysicsWorld(){
        StyleFactory<PhysicsWorld> styleDefinition = new StyleFactory<>(this, SPF);
        setFocusTraversable(true);
        setFocused(true);

        physicsScale = styleDefinition.createStyleableNumberProperty("physicsScale", s -> physicsScaleProperty(), 30.0);
        gravityX = styleDefinition.createStyleableNumberProperty("gravityX", s -> gravityXProperty(), 0.0);
        gravityY = styleDefinition.createStyleableNumberProperty("gravityY", s -> gravityYProperty(), 0.0);

    }

    private StyleableProperty<Number> gravityXProperty() {
        return gravityX;
    }
    public double getGravityX() {
        return gravityX.getValue().doubleValue();
    }
    public void setGravityX(double gravityX) {
        this.gravityX.setValue(gravityX);
    }

    private StyleableProperty<Number> gravityYProperty() {
        return gravityY;
    }
    public double getGravityY() {
        return gravityY.getValue().doubleValue();
    }
    public void setGravityY(double gravityY) {
        this.gravityY.setValue(gravityY);
    }

    private StyleableProperty<Number> physicsScaleProperty() {
        return physicsScale;
    }
    public double getPhysicsScale() {
        return physicsScaleProperty().getValue().doubleValue();
    }
    public void setPhysicsScale(double value){
        physicsScaleProperty().setValue(value);
    }

    private static final StyleablePropertyFactory<PhysicsWorld> SPF = new StyleablePropertyFactory<>(Pane.getClassCssMetaData());

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return SPF.getCssMetaData();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    private List<ChangedEventListener> addListeners = new ArrayList<>();

    public void addAddEventListener(ChangedEventListener listener)  {
        addListeners.add(listener);
    }
    public void removeAddEventListener(ChangedEventListener listener)   {
        addListeners.remove(listener);
    }

    public void add(Node shape) {
        ChangedEvent event = new ChangedEvent(this, shape);
        Iterator i = addListeners.iterator();
        while(i.hasNext())  {
            ((ChangedEventListener) i.next()).handleChangedEvent(event);
        }
    }

    private List<ChangedEventListener> removeListeners = new ArrayList<>();

    public void addRemoveEventListener(ChangedEventListener listener)  {
        removeListeners.add(listener);
    }

    public void removeRemoveEventListener(ChangedEventListener listener)   {
        removeListeners.remove(listener);
    }

    public void remove(Node shape) {
        ChangedEvent event = new ChangedEvent(this, shape);
        Iterator i = removeListeners.iterator();
        while(i.hasNext())  {
            ((ChangedEventListener) i.next()).handleChangedEvent(event);
        }
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

    public final void setOnCollision(EventHandler<? super CollisionEvent> value) {
        onCollisionProperty().set(value);
    }

    public final EventHandler<? super CollisionEvent> getOnCollision() {
        return onCollisionProperty().get();
    }

    public final ObjectProperty<EventHandler<? super CollisionEvent>> onCollisionProperty() {
        return collisionProperty;
    }

    public void endLevel(){
        if (onLevelEnd != null){
            onLevelEnd.action();
        }
    }

    void setOnLevelEnd(Action onLevelEnd) {
        this.onLevelEnd = onLevelEnd;
    }
}

