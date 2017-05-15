package framework;

import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import javafx.animation.AnimationTimer;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import shapes.PhysicsRectangle;

public class PhysicsWorld extends Pane {
	
	private World world = new World(new Vec2(1.0f, 1.0f));
	
	HashMap<Node, Body> nodeBodyMap = new HashMap<Node, Body>();
	
	AnimationTimer worldTimer = new AnimationTimer() {
		private long last;

		@Override
		public void handle(long now) {
			if (last >= 0) {
				long milliseconds = (now - last) / 1_000_000;
				if (milliseconds > 1000.0 / 60.0 && getWorld() != null) {
					getWorld().step(((float) milliseconds) / 1000.0f, 8, 3);
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
				if (milliseconds > 1000.0 / 60.0 && getWorld() != null) {
					UpdateRenderPosition();
					last = now;
				}
			} else {
				last = now;
			}
		}
	};
	
	private void UpdateRenderPosition() {
		updatePositions();
	}
	
	public PhysicsWorld(){
		
		float scale = (float) 40;
		
		ObservableList<Node> children = this.getChildren();
		children.addListener((ListChangeListener.Change<? extends Node> c) -> {
			while (c.next()) {
		        if (c.wasAdded()) {
		            for(Node node : c.getAddedSubList()) {
		  
						BodyDef bodyDefinition = new BodyDef();
						bodyDefinition.type = BodyType.DYNAMIC;
						Bounds bounds = node.getLayoutBounds();
						float hWidth = (float) bounds.getWidth() / 2f;
						float hHeight = (float) bounds.getHeight() / 2f;
						
						float x = (float)((node.getLayoutX() + hWidth )  / scale);
						float y = (float)((node.getLayoutY() + hHeight)  / scale);
						bodyDefinition.position = new Vec2(x, y);
						bodyDefinition.linearDamping = 0f;
						
						
						Body body = this.getWorld().createBody(bodyDefinition);
						PolygonShape shape = new PolygonShape();
						shape.setAsBox(hWidth / scale, hHeight / scale);
						Fixture fixture = body.createFixture(shape, 1f);
						this.nodeBodyMap.put(node, body);
						
						((PhysicsRectangle)node).setBody(body);
						
						System.out.println("Body added to world");
						System.out.println(body.getPosition().x);
						System.out.println(body.getPosition().y);
		            }
		        }
		    }
		});
		

	}
	
	public void StartWorld(){
		System.out.println("Starting world");
		worldTimer.start();
	}
	
	public void Start(){
		System.out.println("Starting world");
		worldTimer.start();
		renderTimer.start();
	}
	
	public void Stop(){
		System.out.println("Stopping world");
		worldTimer.stop();
		renderTimer.stop();
	}
	
	public void updatePositions(){
		for (Node node : this.nodeBodyMap.keySet()) {
			Body body = this.nodeBodyMap.get(node);
			Vec2 position = body.getPosition();
			Point2D nodePosition = this.world2fx(position.x, position.y);
			Bounds boundsInLocal = node.getBoundsInLocal();
			node.setLayoutX(nodePosition.getX() - (boundsInLocal.getWidth() / 2));
			node.setLayoutY(nodePosition.getY() - (boundsInLocal.getHeight() / 2));
			double fxAngle = (- body.getAngle() * 180 / Math.PI) % 360;
			node.setRotate(fxAngle);
		}
	}
	
	public Point2D world2fx(double x, double y) {
		return new Point2D(x * (double)40, y * (double)40);
	}

	public World getWorld() {
		return world;
	}
}
