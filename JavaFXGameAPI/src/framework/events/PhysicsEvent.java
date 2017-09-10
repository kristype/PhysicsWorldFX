package framework.events;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

public class PhysicsEvent extends Event {

    public static final EventType<PhysicsEvent> PHYSICS_STEP = new EventType<>(Event.ANY, "PHYSICS_STEP");

    private int currentStep;
    private final float fps;

    public PhysicsEvent(@NamedArg("eventType") EventType<? extends Event> eventType, int currentStep, float fps) {
        super(eventType);
        this.currentStep = currentStep;
        this.fps = fps;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public float getFps() {
        return fps;
    }
}

