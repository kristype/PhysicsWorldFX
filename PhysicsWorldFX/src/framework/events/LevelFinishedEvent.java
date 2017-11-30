package framework.events;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

public class LevelFinishedEvent extends Event {

    public static final EventType<LevelFinishedEvent> LEVEL_FAILED = new EventType<>(Event.ANY, "LEVEL_FAILED");
    public static final EventType<LevelFinishedEvent> LEVEL_COMPLETE = new EventType<>(Event.ANY, "LEVEL_COMPLETE");


    public LevelFinishedEvent(@NamedArg("eventType") EventType<? extends Event> eventType, int endState) {
        super(eventType);

    }
}
