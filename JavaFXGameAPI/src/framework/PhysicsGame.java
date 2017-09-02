package framework;

import bodies.BodyPropertiesOwner;
import bodies.BodyPropertyDefinitions;
import framework.nodes.ShapeComposition;
import javafx.animation.AnimationTimer;
import javafx.collections.ListChangeListener;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import shapes.PhysicsShape;
import shapes.PhysicsShapeWithLayout;
import utilites.*;
import utilites.debug.DebugDrawJavaFX;

import java.util.*;

public class PhysicsGame {
	
	private AnimationTimerFactory animationTimerFactory;
	private CoordinateConverter coordinateConverter;
    private PhysicsShapeHelper physicsShapeHelper;

	private SimulationTypeToBodyTypeConverter typeConverter;

	private AnimationTimer worldTimer;
    private World world;

    private PhysicsWorld gameWorld;

    private final float fps = 60f;
    private int currentStep = 0;
    private float timeStep = 1000f / fps;
    private Map<Node, Body> nodeBodyMap = new HashMap<>();

    private Map<Node, Fixture> nodeFixtureMap = new HashMap<>();
    private Collection<Node> addQueue = new ArrayList<>();

    private Collection<Node> layoutChangeQueue = new ArrayList<>();
    private Collection<Node> sizeChangeQueue = new ArrayList<>();

    private Collection<Body> destroyQueue = new ArrayList<>();
    private ShapeResolver shapeResolver;

    private Region gameContainer;
    private boolean isUpdating;
    private Action onLevelEndListener;

    private DebugDrawJavaFX debugDraw;
    private Canvas overlay;
    private boolean drawDebug;
    private PositionHelper positionHelper;

    public PhysicsGame(){
	    animationTimerFactory = new AnimationTimerFactory();
        typeConverter = new SimulationTypeToBodyTypeConverter();
	}

	public void enableDebug(){
        if (overlay == null){
            overlay = new Canvas();
            overlay.setWidth(gameWorld.getPrefWidth());
            overlay.setHeight(gameWorld.getPrefHeight());

            debugDraw = new DebugDrawJavaFX(overlay, coordinateConverter.getViewportTransform());
            debugDraw.setFlags(DebugDraw.e_shapeBit);

            gameWorld.getChildren().add(overlay);
            world.setDebugDraw(debugDraw);
        }
        overlay.setVisible(true);
        drawDebug = true;
    }

    public void disableDebug(){
        overlay.setVisible(false);
        drawDebug = false;
    }

	public void load(Region gameContainer){

	    this.gameContainer = gameContainer;
	    this.gameWorld = findWorld(gameContainer);
/*	    this.gameWorld.setPrefHeight(gameContainer.getPrefHeight());
	    this.gameWorld.setPrefWidth(gameContainer.getPrefWidth()); Should be adjusted by the user*/

		if(this.gameWorld != null){
			world = new World(new Vec2((float)gameWorld.getGravityX(), (float)gameWorld.getGravityY()));
			gameWorld.addAddEventListener(e -> addQueue.add(e.getNode()));
			gameWorld.addRemoveEventListener(e -> remove(e.getNode()));
			gameWorld.setOnLevelEnd(() -> {
                if (onLevelEndListener != null){
                    stopGame();
                    onLevelEndListener.action();
                }
            });

			positionHelper = new PositionHelper();
            coordinateConverter = new CoordinateConverter(this.gameWorld);
            physicsShapeHelper = new PhysicsShapeHelper(coordinateConverter);
            shapeResolver = new ShapeResolver(coordinateConverter, positionHelper);

			world.setContactListener(new ContactListener() {
                @Override
                public void beginContact(Contact contact) {
                    handleContact(contact, PhysicsGame.this.gameWorld.getOnBeginCollision());
                }

                @Override
                public void endContact(Contact contact) {
                    handleContact(contact, PhysicsGame.this.gameWorld.getOnEndCollision());
                }

                @Override
                public void preSolve(Contact contact, Manifold oldManifold) {

                }

                @Override
                public void postSolve(Contact contact, ContactImpulse impulse) {

                }
            });
			
            add(gameWorld);
            currentStep = 0;
			worldTimer = animationTimerFactory.CreateTimer(timeStep,t -> {

                destroyQueuedBodies();
                addQueuedNodes();
                handleQueuedPositionUpdates();
                handleQueuedSizeUpdates();

				//Update physics
			    world.step(t / 1000f, 8, 3);
                //Update render positions
                isUpdating = true;
			    updateNodeData();
			    isUpdating = false;

                EventHandler<? super PhysicsEvent> handler = gameWorld.getOnPhysicsStep();
                if (handler != null){
                    currentStep = currentStep == fps ? 1 : ++currentStep;
                    handler.handle(new PhysicsEvent(PhysicsEvent.PHYSICS_STEP, currentStep, fps));
                }
                if (drawDebug)
                    updateCanvas();
			});

			//instantiate the static class
			PhysicsWorldFunctions.setup(world, nodeBodyMap, nodeFixtureMap);
		}
	}

    private final ListChangeListener<Node> listChangeListener = c -> {
        if (c.getAddedSubList() != null) {
            for (Node node : c.getAddedSubList()) {
                if (nodeBodyMap.containsKey(node) || nodeFixtureMap.containsKey(node)) {
                    addQueue.add(node);
                }
            }
        }
        if (c.getRemoved() != null) {
            for (Node node : c.getRemoved()) {
                if (nodeBodyMap.containsKey(node) || nodeFixtureMap.containsKey(node)) {
                    remove(node);
                }
            }
        }
    };

    private void handleContact(Contact contact, EventHandler<? super CollisionEvent> handler) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        Optional<Map.Entry<Node, Body>> node1 = nodeBodyMap.entrySet().stream().filter(e -> e.getValue() == body1).findFirst();
        Optional<Map.Entry<Node, Body>> node2 = nodeBodyMap.entrySet().stream().filter(e -> e.getValue() == body2).findFirst();

        if (node1.isPresent() && node2.isPresent()) {
            if (handler != null){
                handler.handle(new CollisionEvent(CollisionEvent.COLLISION, node1.get().getKey(), node2.get().getKey()));
            }
        }
    }

    private void updateCanvas() {
        GraphicsContext gc = overlay.getGraphicsContext2D();
        Bounds bounds = overlay.getBoundsInLocal();
        gc.clearRect(bounds.getMinX(), bounds.getMinX(), bounds.getWidth(), bounds.getHeight());
        world.drawDebugData();
    }

    private void handleQueuedPositionUpdates() {
        if (!layoutChangeQueue.isEmpty()) {
            for (Node node : layoutChangeQueue) {
                if (nodeBodyMap.containsKey(node)){
                    Body body = nodeBodyMap.get(node);
                    Vec2 bodyPosition = getBodyPosition(node);
                    float angle = (float)(Math.toRadians(-node.getRotate()));
                    body.setTransform(bodyPosition, angle);
                }
            }
            layoutChangeQueue.clear();
        }
    }

    private void handleQueuedSizeUpdates() {
        if (!sizeChangeQueue.isEmpty()) {
            for (Node node : sizeChangeQueue) {
                if (nodeFixtureMap.containsKey(node)){
                    shapeResolver.updateShape(node, nodeFixtureMap.get(node));
                }
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
                Optional<Map.Entry<Node, Body>> node = nodeBodyMap.entrySet().stream().filter(e -> e.getValue() == body).findFirst();
                if (node.isPresent()){
                    nodeBodyMap.remove(node.get().getKey());
                }
            }
            destroyQueue.clear();
        }
    }

    private void remove(Node node) {
	    if (nodeBodyMap.containsKey(node)){
            Body body = nodeBodyMap.get(node);
            destroyQueue.add(body);
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

    private void addShapeContainer(ShapeComposition node) {
        Body body = createBody(node, node);
        for (Node child : node.getChildrenUnmodifiable()){
            if (child instanceof PhysicsShape)
                addFixtureToBody((PhysicsShape) child, child, body);
        }
        node.setup(body, physicsShapeHelper);
    }

	private void addBody(PhysicsShape physicsShape, Node node){
        Body body = createBody(physicsShape, node);
        addFixtureToBody(physicsShape, node, body);
	}

    private void addFixtureToBody(PhysicsShape physicsShape, Node node, Body body) {
        FixtureDef fixtureDef = physicsShape.getFixturePropertyDefinitions().createFixtureDef();
        fixtureDef.shape = shapeResolver.ResolveShape(node);
        Fixture fixture = body.createFixture(fixtureDef);

        physicsShape.setup(body, physicsShapeHelper);

        physicsShape.addSizeChangedEventListener(e -> {
            if (!isUpdating)
                sizeChangeQueue.add(e.getNode());
        });
/*        physicsShape.addLayoutChangedEventListener(e -> { Shapes with bodies only for now!
            if(!isUpdating)
                layoutChangeQueue.add(e.getNode());
        });*/
        nodeFixtureMap.put(node, fixture);
    }

    private <T extends BodyPropertiesOwner & PhysicsShapeWithLayout> Body createBody(T bodyOwner, Node node) {
        BodyPropertyDefinitions<? extends Styleable> bodyPropertyDefinitions = bodyOwner.getBodyPropertyDefinitions();
        BodyDef bodyDefinition = bodyPropertyDefinitions.createBodyDef(typeConverter);
        Vec2 bodyPosition = getBodyPosition(node);
        bodyDefinition.position.set(bodyPosition);

        double rotate = node.getRotate();
        bodyDefinition.angle = positionHelper.getBodyRadians(rotate);
        bodyDefinition.linearVelocity = coordinateConverter.convertVectorToWorld(bodyPropertyDefinitions.getLinearVelocityX(), bodyPropertyDefinitions.getLinearVelocityY());
        bodyDefinition.angularVelocity = coordinateConverter.scaleVectorToWorld(bodyPropertyDefinitions.getAngularVelocity());
        bodyOwner.addVelocityChangedEventListener(onVelocityChanged());
        bodyOwner.addLayoutChangedEventListener(e -> {
            if(!isUpdating)
                layoutChangeQueue.add(e.getNode());
        });

        Body body = world.createBody(bodyDefinition);
        this.nodeBodyMap.put(node, body);

        return body;
    }

    private ChangedEventListener onVelocityChanged() {
        return e ->{
            if (!isUpdating){
                BodyPropertiesOwner eNode = (BodyPropertiesOwner)e.getNode();
                Body body = nodeBodyMap.get(e.getNode());
                body.setLinearVelocity(coordinateConverter.convertVectorToWorld(eNode.getLinearVelocityX(), eNode.getLinearVelocityY()));
                body.setAngularVelocity(coordinateConverter.scaleVectorToWorld(eNode.getAngularVelocity()));
            }
        };
    }

    private Vec2 getBodyPosition(Node node) {
        Bounds bounds = node.getBoundsInLocal();
        Point2D center = positionHelper.getCenter2(bounds);
        double childX = center.getX() + node.getLayoutX();
        double childY = center.getY() + node.getLayoutY();
        return coordinateConverter.convertNodePointToWorld(childX, childY, node.getParent());
    }

    public void startGame(){
	    worldTimer.start();
    }

    public void stopGame(){
        worldTimer.stop();
        worldTimer = null;
    }
	
	private void updateNodeData(){
		for (Node node : nodeBodyMap.keySet()){
			Body body = nodeBodyMap.get(node);
			Bounds bounds = node.getBoundsInLocal();

			Point2D nodePosition = coordinateConverter.convertWorldPointToScreen(body.getPosition().x, body.getPosition().y, node.getParent());
            Point2D center = positionHelper.getCenter2(bounds);
			double x = nodePosition.getX() - (center.getX());
            double y = nodePosition.getY() - (center.getY());
            node.setLayoutX(x);
            node.setLayoutY(y);
			double fxAngle = positionHelper.getAngle(body.getAngle());
			node.setRotate(fxAngle);

            Vec2 linearVelocity = body.getLinearVelocity();
            Point2D convertedVelocity = coordinateConverter.convertVectorToScreen(linearVelocity);

            double scaledAngularVelocity = coordinateConverter.scaleVectorToScreen(body.getAngularVelocity());

            BodyPropertiesOwner bodyPropertiesOwner = (BodyPropertiesOwner) node;
            bodyPropertiesOwner.setLinearVelocityX(convertedVelocity.getX());
            bodyPropertiesOwner.setLinearVelocityY(convertedVelocity.getY());
            bodyPropertiesOwner.setAngularVelocity(scaledAngularVelocity);
		}
	}

    public void setOnFinish(Action onLevelEndListener) {
        this.onLevelEndListener = onLevelEndListener;
    }
}
