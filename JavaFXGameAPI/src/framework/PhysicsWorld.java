package framework;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhysicsWorld extends Pane {

    private float gravityX = 0f;
    private float gravityY = 0f;

    private float physicsScale = 30f;

    public PhysicsWorld(){
        setFocusTraversable(true);
        setFocused(true);
    }

    public float getPhysicsScale() {
		return physicsScale;
	}

	public void setPhysicsScale(float value){
        physicsScale = value;
    }

    public float getGravityX() {
        return gravityX;
    }

    public void setGravityX(float gravityX) {
        this.gravityX = gravityX;
    }

    public float getGravityY() {
        return gravityY;
    }

    public void setGravityY(float gravityY) {
        this.gravityY = gravityY;
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
}

