package framework;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

public class PhysicsEvent extends Event {

    public static final EventType<PhysicsEvent> PHYSICS_STEP = new EventType<>(Event.ANY, "PHYSICS_STEP");

    public PhysicsEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
        super(eventType);
    }
}

