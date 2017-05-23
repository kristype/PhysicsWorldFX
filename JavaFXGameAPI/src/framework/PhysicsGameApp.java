package framework;

import java.io.IOException;
import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import shapes.PhysicsRectangle;
import utilites.AnimationTimerFactory;
import utilites.CoordinateConverter;

public abstract class PhysicsGameApp extends Application {
	
	private AnimationTimerFactory animationTimerFactory;
	private CoordinateConverter coordinateConvertor;
	
	private AnimationTimer worldTimer;
	private AnimationTimer renderTimer;
	
	private World world = new World(new Vec2(0.0f, 0.0f));
	PhysicsWorld gameWorld;
	
	private float timeStep = 1000f / 60f;
	
	HashMap<Node, Body> nodeBodyMap = new HashMap<Node, Body>();

	public PhysicsGameApp(){
		animationTimerFactory = new AnimationTimerFactory();
	}

	@Override
	public final void start(javafx.stage.Stage primaryStage) throws Exception {
		try{
			gameWorld = Load();
			
		    Scene scene = new Scene(gameWorld);
		    scene.setOnKeyPressed(this::handleKey);
			primaryStage.setScene(scene);
		    primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(gameWorld != null){
			coordinateConvertor = new CoordinateConverter(gameWorld);
			
			for (Node node : gameWorld.getChildrenUnmodifiable()){
				if(node instanceof PhysicsRectangle){
					addBody((PhysicsRectangle) node);
				}
			}
			
			worldTimer = animationTimerFactory.CreateTimer(timeStep,t -> {
				world.step(timeStep, 8, 3);
			});
			
			renderTimer = animationTimerFactory.CreateTimer(timeStep,t -> {
				updateNodePositions();
			});
		}
	}
	
	protected void handleKey(KeyEvent keyEvent) {

	}

	protected void debugStep() {
		System.out.println("Step:");
		world.step(timeStep, 8, 3);
		updateNodePositions();
		for(Body body : nodeBodyMap.values()){
			Vec2 pos = body.getPosition();
			System.out.println(pos.x + " "+ pos.y);
		}
	}

	public abstract PhysicsWorld Load() throws IOException;
	
	private void addBody(PhysicsRectangle node){
		BodyDef bodyDefinition = node.getBodyDefBean().createBodyDef();
		Bounds bounds = node.getBoundsInLocal();
		double cx = (bounds.getMinX() + bounds.getMaxX()) / 2, cy = (bounds.getMinY() + bounds.getMaxY()) / 2;
		double childX = node.getLayoutX() + cx, childY = node.getLayoutY() + cy;
		//node.setLayoutX(childX);
		//node.setLayoutY(childY);
		Point2D bodyPosition = coordinateConvertor.fxPoint2world(childX, childY, node.getParent());
		bodyDefinition.position.set((float) bodyPosition.getX(), (float) bodyPosition.getY());
		
		float hWidth = (float) bounds.getWidth() / 2f;
		float hHeight = (float) bounds.getHeight() / 2f;
		
		Body body = world.createBody(bodyDefinition);
		PolygonShape shape = new PolygonShape();
		
		float scale = gameWorld.getGameWorldScale();
		shape.setAsBox(hWidth / (float)scale, hHeight / (float)scale);
		
		FixtureDef fixtureDef = node.getFixtureDefBean().createFixtureDef();
		fixtureDef.shape = shape;
		
		body.createFixture(fixtureDef);

		this.nodeBodyMap.put(node, body);
		
		((PhysicsRectangle)node).setup(body);
	}
	
	public void StartWorld(){
		System.out.println("Starting world");
		renderTimer.start();
	}
	
	public void StartRenderer() {
		System.out.println("Starting renderer");
		worldTimer.start();
	}
	
	public void startGame(){
		StartWorld();
		StartRenderer();
	}
	
	public void StopWorld(){
		System.out.println("Stopping world");
		worldTimer.stop();
	}
	
	public void StopRenderer() {
		System.out.println("Stopping renderer");
		renderTimer.stop();
	}
	
	public void stopGame(){
		StopWorld();
		StopRenderer();
	}
	
	private void updateNodePositions(){
		for (Node node : nodeBodyMap.keySet()){
			Body body = nodeBodyMap.get(node);
			Bounds bounds = node.getBoundsInLocal();
			
			Point2D nodePosition = coordinateConvertor.world2fx(body.getPosition().x, body.getPosition().y, node.getParent());
			node.setLayoutX(nodePosition.getX() - (bounds.getWidth()/2));
			node.setLayoutY(nodePosition.getY() - (bounds.getHeight()/2));
			double fxAngle = (- body.getAngle() * 180 / Math.PI) % 360;
			node.setRotate(fxAngle);
		}
	}
}
