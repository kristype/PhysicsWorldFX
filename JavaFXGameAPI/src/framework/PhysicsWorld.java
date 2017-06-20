package framework;

import javafx.scene.layout.Pane;

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
}

