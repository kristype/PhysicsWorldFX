package shapes;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import javafx.scene.shape.Rectangle;

public class PhysicsRectangle extends Rectangle {
	private Body body;

	public void setBody(Body body) {
		this.body = body;
	}

	public void SetSpeed(float vx, float vy) {
		body.setLinearVelocity(new Vec2(vx, vy));
	}
}
