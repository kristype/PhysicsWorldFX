package framework;

import bodies.BodyDefBeanOwner;
import bodies.ShapeComposition;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import shapes.PhysicsShape;
import utilites.AnimationTimerFactory;
import utilites.CoordinateConverter;
import utilites.ShapeResolver;
import utilites.SimulationTypeToBodyTypeConverter;

import java.util.*;

public class PhysicsGame {
	
	private AnimationTimerFactory animationTimerFactory;
	private CoordinateConverter coordinateConverter;
	private SimulationTypeToBodyTypeConverter typeConverter;

	private AnimationTimer worldTimer;

	private World world;
    private PhysicsWorld gameWorld;
	
	private float timeStep = 1000f / 60f;

    private Map<Node, Body> nodeBodyMap = new HashMap<>();
    private Map<Node, Fixture> nodeFixtureMap = new HashMap<>();

    private Collection<Node> addQueue = new ArrayList<>();
    private Collection<Node> layoutChangeQueue = new ArrayList<>();
    private Collection<Node> sizeChangeQueue = new ArrayList<>();
    private Collection<Body> destroyQueue = new ArrayList<>();

	private ShapeResolver shapeResolver;
    private Region gameContainer;

    private boolean isUpdating;

    public PhysicsGame(){
	    animationTimerFactory = new AnimationTimerFactory();
        typeConverter = new SimulationTypeToBodyTypeConverter();
	}

	public void load(Region gameContainer){

	    this.gameContainer = gameContainer;
	    this.gameWorld = findWorld(gameContainer);

		if(this.gameWorld != null){
			world = new World(new Vec2(gameWorld.getGravityX(), gameWorld.getGravityY()));

			gameWorld.addAddEventListener(e -> addQueue.add(e.getNode()));
			gameWorld.addRemoveEventListener(e -> remove(e.getNode()));

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

            coordinateConverter = new CoordinateConverter(this.gameWorld, this.gameContainer);
			shapeResolver = new ShapeResolver(coordinateConverter);
			
            add(gameWorld);
			
			worldTimer = animationTimerFactory.CreateTimer(timeStep,t -> {

				//Update physics
			    world.step(t / 1000f, 8, 3);
                //Update render positions
                isUpdating = true;
			    updateNodePositions();
			    isUpdating = false;

                destroyQueuedBodies();
                addQueuedNodes();
                handleQueuedPositionUpdates();
                handleQueuedSizeUpdates();

                gameWorld.fireEvent(new PhysicsEvent(PhysicsEvent.PHYSICS_STEP));
			});

			//instantiate the static class
			PhysicsWorldHelper.setup(world, nodeBodyMap);
		}
	}

    private void handleQueuedPositionUpdates() {
        if (!layoutChangeQueue.isEmpty()) {
            for (Node node : layoutChangeQueue) {
                Body body = nodeBodyMap.get(node);
                Point2D bodyPosition = getBodyPosition(node);
                float angle = (float)(Math.toRadians(node.getRotate()));

                body.setTransform(new Vec2((float) bodyPosition.getX(), (float) bodyPosition.getY()), angle);
            }
            layoutChangeQueue.clear();
        }
    }

    private void handleQueuedSizeUpdates() {
        if (!sizeChangeQueue.isEmpty()) {
            for (Node node : sizeChangeQueue) {
                shapeResolver.updateShape(node, nodeFixtureMap.get(node));
            }
            sizeChangeQueue.clear();
        }
    }

    private void addQueuedNodes() {
        if (!addQueue.isEmpty()){
            for (Node node : addQueue) {
                add(node);
            }
            addQueue.clear();
        }
    }

    private void destroyQueuedBodies() {
        if (!destroyQueue.isEmpty()) {
            for (Body body : destroyQueue) {
                world.destroyBody(body);
            }
            destroyQueue.clear();
        }
    }

    private void remove(Node node) {
	    if (nodeBodyMap.containsKey(node)){
            Body body = nodeBodyMap.get(node);
            destroyQueue.add(body);
            nodeBodyMap.remove(node);
        }
    }

    private void add(Node node){
        if(node instanceof PhysicsShape){
            addBody((PhysicsShape) node, node);
        }else if (node instanceof Parent){
            if (node instanceof ShapeComposition){
                addShapeContainer((ShapeComposition) node);
            }else{
                for(Node childNode : ((Parent)node).getChildrenUnmodifiable()){
                    add(childNode);
                }
            }

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

    private void addShapeContainer(ShapeComposition node) {
        Body body = createBody(node, node);
        for (Node child : node.getChildrenUnmodifiable()){
            if (child instanceof PhysicsShape)
                addFixtureToBody((PhysicsShape) child, child, body);
        }
        node.setup(body, coordinateConverter);
    }

	private void addBody(PhysicsShape physicsShape, Node node){
        Body body = createBody(physicsShape, node);
        addFixtureToBody(physicsShape, node, body);
	}

    private void addFixtureToBody(PhysicsShape physicsShape, Node node, Body body) {
        FixtureDef fixtureDef = physicsShape.getFixtureDefBean().createFixtureDef();
        fixtureDef.shape = shapeResolver.ResolveShape(node);
        Fixture fixture = body.createFixture(fixtureDef);

        physicsShape.setup(body, coordinateConverter);

        physicsShape.addSizeChangedEventListener(e -> {
            if (!isUpdating)
                sizeChangeQueue.add(e.getNode());
        });
        physicsShape.addLayoutChangedEventListener(e -> {
            if(!isUpdating)
                layoutChangeQueue.add(e.getNode());
        });
        nodeFixtureMap.put(node, fixture);
    }

    private Body createBody(BodyDefBeanOwner bodyDefOwner, Node node) {
        BodyDef bodyDefinition = bodyDefOwner.getBodyDefBean().createBodyDef(typeConverter);
        Point2D bodyPosition = getBodyPosition(node);
        bodyDefinition.position.set((float) bodyPosition.getX(), (float) bodyPosition.getY());
        Body body = world.createBody(bodyDefinition);
        this.nodeBodyMap.put(node, body);
        return body;
    }

    private Point2D getBodyPosition(Node node) {
        Bounds bounds = node.getBoundsInLocal();
        double cx = (bounds.getMinX() + bounds.getMaxX()) / 2, cy = (bounds.getMinY() + bounds.getMaxY()) / 2;
        double childX = node.getLayoutX() + cx, childY = node.getLayoutY() + cy;
        return coordinateConverter.fxPoint2world(childX, childY, node.getParent());
    }

    public void startRenderer() {
		System.out.println("Starting renderer");
		worldTimer.start();
	}

	public void startGame(){
		startRenderer();
	}
	
	public void stopWorld(){
		System.out.println("Stopping world");
		worldTimer.stop();
	}
	
	public void stopGame(){
		stopWorld();
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
