package framework.events;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * The order of the objects is not predictable
 */
public class CollisionEvent extends Event {

    public static final EventType<CollisionEvent> COLLISION = new EventType<>(Event.ANY, "COLLISION");
    private Node object1;
    private Node object2;

    public CollisionEvent(@NamedArg("eventType") EventType<? extends Event> eventType, Node object1, Node object2) {
        super(eventType);
        this.object1 = object1;
        this.object2 = object2;
    }

    public Node getObject1() {
        return object1;
    }

    public Node getObject2() {
        return object2;
    }
}
