package framework;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import shapes.PhysicsRectangle;
import shapes.PhysicsShape;
import utilites.AnimationTimerFactory;
import utilites.CoordinateConverter;
import utilites.ShapeResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PhysicsGame {
	
	private AnimationTimerFactory animationTimerFactory;
	private CoordinateConverter coordinateConverter;
	
	private AnimationTimer worldTimer;
	private AnimationTimer renderTimer;
	
	private World world;
	PhysicsWorld gameWorld;
	
	private float timeStep = 1000f / 60f;
	
	HashMap<Node, Body> nodeBodyMap = new HashMap<Node, Body>();
	private ShapeResolver shapeResolver;

	public PhysicsGame(){
		animationTimerFactory = new AnimationTimerFactory();
	}

	public void load(Parent gameContainer){

	    this.gameWorld = findWorld(gameContainer);

		if(this.gameWorld != null){
			world = new World(new Vec2(gameWorld.getGravityX(), gameWorld.getGravityY()));
			world.setContactListener(new ContactListener() {
                @Override
                public void beginContact(Contact contact) {
                    Body body1 = contact.getFixtureA().getBody();
                    Body body2 = contact.getFixtureB().getBody();
                    Optional<Map.Entry<Node, Body>> node1 = nodeBodyMap.entrySet().stream().filter(e -> e.getValue() == body1).findFirst();
                    Optional<Map.Entry<Node, Body>> node2 = nodeBodyMap.entrySet().stream().filter(e -> e.getValue() == body2).findFirst();

                    if (node1.isPresent() && node2.isPresent()) {
                        gameWorld.fireEvent(new CollisionEvent(CollisionEvent.COLLISION, node1.get().getKey(), node2.get().getKey()));
                    }
                }

                @Override
                public void endContact(Contact contact) {

                }

                @Override
                public void preSolve(Contact contact, Manifold oldManifold) {

                }

                @Override
                public void postSolve(Contact contact, ContactImpulse impulse) {

                }
            });

            coordinateConverter = new CoordinateConverter(this.gameWorld);
			shapeResolver = new ShapeResolver(coordinateConverter);
			
			for (Node node : this.gameWorld.getChildrenUnmodifiable()){
				if(node instanceof PhysicsShape){
					addBody((PhysicsShape) node, node);
				}
			}
			
			worldTimer = animationTimerFactory.CreateTimer(timeStep,t -> {
				world.step(t / 1000f, 8, 3);
                updateNodePositions();
				gameWorld.fireEvent(new PhysicsEvent(PhysicsEvent.PHYSICS_STEP));
			});
			
			renderTimer = animationTimerFactory.CreateTimer(timeStep,t -> {

			});
		}
	}

    private PhysicsWorld findWorld(Parent gameContainer) {
	    if (gameContainer instanceof PhysicsWorld){
	        return (PhysicsWorld) gameContainer;
        }
        for (Node node : gameContainer.getChildrenUnmodifiable()) {
            if (node instanceof Parent){
                PhysicsWorld foundWorld = findWorld((Parent)node);
                if (foundWorld != null){
                    return foundWorld;
                }
            }
        }
        return null;
    }

    public void debugStep() {
		System.out.println("Step:");
		world.step(timeStep, 8, 3);
		updateNodePositions();
		for(Body body : nodeBodyMap.values()){
			Vec2 pos = body.getPosition();
			System.out.println(pos.x + " "+ pos.y);
		}
	}

	private void addBody(PhysicsShape physicsShape, Node node){
		BodyDef bodyDefinition = physicsShape.getBodyDefBean().createBodyDef();
		Bounds bounds = node.getBoundsInLocal();
		double cx = (bounds.getMinX() + bounds.getMaxX()) / 2, cy = (bounds.getMinY() + bounds.getMaxY()) / 2;
		double childX = node.getLayoutX() + cx, childY = node.getLayoutY() + cy;

		Point2D bodyPosition = coordinateConverter.fxPoint2world(childX, childY, node.getParent());
		bodyDefinition.position.set((float) bodyPosition.getX(), (float) bodyPosition.getY());

		Body body = world.createBody(bodyDefinition);

		FixtureDef fixtureDef = physicsShape.getFixtureDefBean().createFixtureDef();
		fixtureDef.shape = shapeResolver.ResolveShape(node);
		
		body.createFixture(fixtureDef);

		this.nodeBodyMap.put(node, body);

		physicsShape.setup(body);
	}
	
	public void startWorld(){
		System.out.println("Starting world");
		renderTimer.start();
	}
	
	public void startRenderer() {
		System.out.println("Starting renderer");
		worldTimer.start();
	}
	
	public void startGame(){
		startWorld();
		startRenderer();
	}
	
	public void stopWorld(){
		System.out.println("Stopping world");
		worldTimer.stop();
	}
	
	public void stopRenderer() {
		System.out.println("Stopping renderer");
		renderTimer.stop();
	}
	
	public void stopGame(){
		stopWorld();
		stopRenderer();
	}
	
	private void updateNodePositions(){
		for (Node node : nodeBodyMap.keySet()){
			Body body = nodeBodyMap.get(node);
			Bounds bounds = node.getBoundsInLocal();
			
			Point2D nodePosition = coordinateConverter.world2fx(body.getPosition().x, body.getPosition().y, node.getParent());
			node.relocate(nodePosition.getX() - (bounds.getWidth()/2), nodePosition.getY() - (bounds.getHeight()/2));
			double fxAngle = (- body.getAngle() * 180 / Math.PI) % 360;
			node.setRotate(fxAngle);
		}
	}
}
