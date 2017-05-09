package framework;

import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import shapes.PhysicsRectangle;

public class PhysicsWorld extends Pane {
	
	private World world = new World(new Vec2());
	
	HashMap<Node, Body> nodeBodyMap = new HashMap<Node, Body>();
	
	AnimationTimer worldTimer = new AnimationTimer() {
		private long last;

		@Override
		public void handle(long now) {
			if (last >= 0) {
				long milliseconds = (now - last) / 1_000_000;
				if (milliseconds > 1000.0 / 60.0 && world != null) {
					world.step(((float) milliseconds) / 1000.0f, 8, 3);
					last = now;
				}
			} else {
				last = now;
			}
		}
	};
	
	AnimationTimer renderTimer = new AnimationTimer() {
		private long last;

		@Override
		public void handle(long now) {
			if (last >= 0) {
				long milliseconds = (now - last) / 1_000_000;
				if (milliseconds > 1000.0 / 60.0 && world != null) {
					
					last = now;
				}
			} else {
				last = now;
			}
		}
	};

	private OBBViewportTransform viewportTransform;
	
	public PhysicsWorld(){
		OBBViewportTransform obb = new OBBViewportTransform();
		float scale = (float) 40;
		obb.setTransform(new Mat22(scale, 0.0f, 0.0f, scale));
		obb.setCenter((float) (this.getWidth() / 2), (float) (this.getHeight() / 2));
		obb.setExtents((float) (this.getWidth() / 2), (float) (this.getHeight() / 2));
		obb.setYFlip(true);
		this.viewportTransform = obb;
		
		ObservableList<Node> children = this.getChildren();
		for (Node node : children) {
			if (node.getClass().isInstance(PhysicsRectangle.class)){
				BodyDef bodyDefinition = new BodyDef();
				bodyDefinition.type = BodyType.DYNAMIC;
				
				Body body = this.world.createBody(bodyDefinition);
				PolygonShape shape = new PolygonShape();
				shape.setAsBox(1f, 1f);
				Fixture fixture = body.createFixture(shape,1f);
				this.nodeBodyMap.put(node, body);
			}
		}
		this.worldTimer.start();
		this.renderTimer.start();
	}
	
	public void updatePositions(){
		for (Node node : this.getChildren()) {
			Body body = nodeBodyMap.get(node);
			Point2D nodePosition = this.world2fx(body.getPosition().x, body.getPosition().y);
			node.setLayoutX(nodePosition.getX());
			node.setLayoutY(nodePosition.getY());
			double fxAngle = (- body.getAngle() * 180 / Math.PI) % 360;
			node.setRotate(fxAngle);
		}
	}
	
	private Vec2 world2fxTemp1 = new Vec2(), world2fxTemp2 = new Vec2();
	
	public Point2D world2fx(double x, double y) {
		Vec2 result = world2fxTemp1;
		world2fxTemp2.set((float) x, (float) y);
		viewportTransform.getWorldToScreen(new Vec2((float) x, (float) y), result);
		return new Point2D(result.x, result.y);
	}


}
