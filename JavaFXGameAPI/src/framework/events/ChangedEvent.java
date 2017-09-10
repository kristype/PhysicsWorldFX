package framework.events;

import javafx.scene.Node;

import java.util.EventObject;

/**
 * Created by Kristian on 14/07/2017.
 */
public class ChangedEvent extends EventObject {
    private Node node;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ChangedEvent(Object source, Node node) {
        super(source);
        this.node = node;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param node The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ChangedEvent(Node node) {
        super(node);
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}
