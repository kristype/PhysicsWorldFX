package framework;

import framework.geometric.GeometricPropertiesOwner;
import framework.geometric.GeometricPropertyDefinitions;
import framework.geometric.Geometric;
import framework.events.*;
import framework.nodes.GeometricComposition;
import framework.nodes.PhysicsWorld;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import framework.physical.PhysicalPropertiesOwner;
import framework.physical.PhysicsShape;
import framework.physical.Physical;
import framework.physical.SingleShape;
import utilites.*;
import utilites.debug.DebugDrawJavaFX;

import java.util.*;

public class PhysicsGame {

    private AnimationTimerFactory animationTimerFactory;
    private CoordinateConverter coordinateConverter;
    private PositionHelper positionHelper;
    private PhysicsToNodeSynchronizer physicsToNodeSynchronizer;
    private PhysicsShapeHelper physicsShapeHelper;
	private SimulationTypeToBodyTypeConverter typeConverter;
    private ShapeResolver shapeResolver;

    private DebugDrawJavaFX debugDraw;
    private Canvas overlay;
    private AnimationTimer worldTimer;
    private World world;
    private PhysicsWorld gameWorld;
    private final float fps = 60f;
    private float timeStep = 1000f / fps;
    private int currentStep = 0;
    private boolean drawDebug;
    private boolean isUpdating;

    private Map<Node, Body> nodeBodyMap = new HashMap<>();
    private Map<Node, Fixture> nodeFixtureMap = new HashMap<>();

    private Map<Node, FixtureListeners> fixtureListenersMap = new HashMap<>();
    private Collection<Node> addQueue = new ArrayList<>();
    private Collection<Node> layoutChangeQueue = new ArrayList<>();
    private Collection<Node> sizeChangeQueue = new ArrayList<>();
    private Collection<Node> typeChangeQueue = new ArrayList<>();
    private Collection<Node> activeChangeQueue = new ArrayList<>();
    private Collection<Body> destroyQueue = new ArrayList<>();


    private LevelFinishEventListener onLevelFailedListener;
    private LevelFinishEventListener onLevelCompleteListener;


    public PhysicsGame(){
	    animationTimerFactory = new AnimationTimerFactory();
        typeConverter = new SimulationTypeToBodyTypeConverter();
	}

    /**
     * Enables debug overlay
     */
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

    /**
     * Disables debug overlay
     */
    public void disableDebug(){
        overlay.setVisible(false);
        drawDebug = false;
    }

    /**
     * Loads the fxml and gives physical nodes physical behaviour
     * @param gameContainer the gameContainer must be a PhysicsWorld, or contain a PhysicsWorld
     */
	public void load(Region gameContainer){

        this.gameWorld = findWorld(gameContainer);

		if(this.gameWorld != null){
		    //Calculate the layout so non visible and scaled nodes get the correct center
		    gameWorld.layout();
            coordinateConverter = new CoordinateConverter(this.gameWorld);

			world = new World(getGravity());
            gameWorld.gravityXProperty().addListener((observable, oldValue, newValue) -> world.setGravity(getGravity()));
			gameWorld.gravityYProperty().addListener((observable, oldValue, newValue) -> world.setGravity(getGravity()));
			gameWorld.setOnLevelFinish(e -> {
			    if (e.getEventType() == LevelFinishedEvent.LEVEL_COMPLETE){
			        if (onLevelCompleteListener != null){
			            onLevelCompleteListener.handleLevelFinishedEvent(e);
                    }
                }
                if (e.getEventType() == LevelFinishedEvent.LEVEL_FAILED){
                    if (onLevelFailedListener != null){
                        onLevelFailedListener.handleLevelFinishedEvent(e);
                    }
                }
            });

			positionHelper = new PositionHelper();
            physicsShapeHelper = new PhysicsShapeHelper(coordinateConverter, positionHelper);
            shapeResolver = new ShapeResolver(coordinateConverter, positionHelper);
            physicsToNodeSynchronizer = new PhysicsToNodeSynchronizer(coordinateConverter, positionHelper);

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
                handleQueuedChanges();

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
			PhysicsWorldFunctions.setup(world, nodeBodyMap, nodeFixtureMap, coordinateConverter);
		}
	}

    private Vec2 getGravity() {
	    return coordinateConverter.convertVectorToWorld(gameWorld.getGravityX(), gameWorld.getGravityY());
    }

    //changes are queued because some things has to happen after a world step
    private void handleQueuedChanges() {
        addQueuedNodes();
        handleQueuedPositionUpdates();
        handleQueuedSizeUpdates();
        handleActiveQueue();
        handleTypeChangeQueue();
        destroyQueuedBodies();
    }

    private void handleActiveQueue() {
        if (!activeChangeQueue.isEmpty()){
            for (Node node : activeChangeQueue) {
                Body body = nodeBodyMap.get(node);
                Geometric geometric = (Geometric)node;
                body.setActive(geometric.isActive());
            }
        }
    }

    private void handleTypeChangeQueue() {
        if (!typeChangeQueue.isEmpty()){
            for (Node node : typeChangeQueue) {
                Body body = nodeBodyMap.get(node);
                Geometric geometric = (Geometric)node;
                body.setType( typeConverter.Convert(geometric.getSimulationType()));
            }
        }
    }

    private final ListChangeListener<Node> listChangeListener = c -> {
        while (c.next()) {
            List<? extends Node> addedSubList = c.getAddedSubList();
            if (addedSubList != null) {
                for (Node node : addedSubList) {
                    addQueue.add(node);
                }
            }
            List<? extends Node> removed = c.getRemoved();
            if (removed != null) {
                for (Node node : removed) {
                    if (nodeBodyMap.containsKey(node) || nodeFixtureMap.containsKey(node)) {
                        remove(node);
                    }
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
                Node object1 = node1.get().getKey();
                Node object2 = node2.get().getKey();
                handler.handle(new CollisionEvent(CollisionEvent.COLLISION, object1, object2));
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
                    updateShape(node);
                }else if(nodeBodyMap.containsKey(node) && node instanceof GeometricComposition){

                    GeometricComposition compositionNode = (GeometricComposition)node;
                    for (Node child : compositionNode.getChildrenUnmodifiable()) {
                        if (nodeFixtureMap.containsKey(child)) {
                            updateShape(child);
                        }
                    }
                }
            }
            sizeChangeQueue.clear();
        }
    }

    private void updateShape(Node node) {
        Fixture currentFixture = nodeFixtureMap.get(node);
        Shape shape = shapeResolver.updateShape(node, currentFixture);
        if (currentFixture.getShape() != shape){
            Body body = currentFixture.getBody();
            body.destroyFixture(currentFixture);
            //Clean up listeners with reference to old fixture
            fixtureListenersMap.get(node).RemoveListeners((Physical)node);

            createFixture(node, body, shape);
        }
    }

    private void createFixture(Node node, Body body, Shape shape) {
        PhysicalPropertiesOwner physical = (PhysicalPropertiesOwner)node;
        FixtureDef fixtureDef = physical.getPhysicalPropertyDefinitions().createFixtureDef();
        fixtureDef.shape = shape;
        Fixture fixture = body.createFixture(fixtureDef);
        fixtureListenersMap.put(node, new FixtureListeners((Physical) node, fixture));
        nodeFixtureMap.put(node, fixture);
    }

    private void addQueuedNodes() {
        if (!addQueue.isEmpty()){
            ArrayList<Node> addList = new ArrayList<>(addQueue);
            for (Node node : addList) {
                add(node);
            }
            addQueue.removeAll(addList);
        }
    }

    private void destroyQueuedBodies() {
        if (!destroyQueue.isEmpty()) {
            ArrayList<Body> removeList = new ArrayList<>(destroyQueue);
            for (Body body : removeList) {
                world.destroyBody(body);
                Optional<Map.Entry<Node, Body>> node = nodeBodyMap.entrySet().stream().filter(e -> e.getValue() == body).findFirst();
                node.ifPresent(nodeBodyEntry -> cleanUp(nodeBodyEntry.getKey()));

            }
            destroyQueue.removeAll(removeList);
        }
    }

    private void cleanUp(Node node) {
        nodeBodyMap.remove(node);
        nodeFixtureMap.remove(node);
        fixtureListenersMap.get(node).RemoveListeners((Physical) node);
        fixtureListenersMap.remove(node);
        if (node instanceof Pane){
            Pane pane = (Pane) node;
            pane.getChildren().removeListener(listChangeListener);
        }
    }

    private void remove(Node node) {
	    if (nodeBodyMap.containsKey(node)){
            Body body = nodeBodyMap.get(node);
            destroyQueue.add(body);
        }
    }

    private void add(Node node){
        if(node instanceof SingleShape){
            addBody((Node & SingleShape) node);
        }else if (node instanceof Parent){
            if (node instanceof Pane){
                Pane pane = (Pane) node;
                pane.getChildren().addListener(listChangeListener);
            }

            if (node instanceof GeometricComposition){
                addShapeContainer((GeometricComposition) node);
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

    private void addShapeContainer(GeometricComposition node) {
        Body body = createBody(node);
        for (Node child : node.getChildrenUnmodifiable()){
            if (child instanceof SingleShape)
                addFixtureToBody((Node & SingleShape) child, body);
        }
        node.setup(body, physicsShapeHelper);
    }

	private <T extends Node & SingleShape> void addBody(T node){
        Body body = createBody(node);
        addFixtureToBody(node, body);
	}

    private <T extends Node & PhysicalPropertiesOwner & Physical & PhysicsShape> void addFixtureToBody(T node, Body body) {
        createFixture(node, body, shapeResolver.ResolveShape(node));

        node.setup(body, physicsShapeHelper);
        node.addSizeChangedEventListener(e -> addToSizeChangedQueue(node));

        if (node.getParent() instanceof GeometricComposition){
            node.layoutXProperty().addListener((observable, oldValue, newValue) -> addToSizeChangedQueue(node));
            node.layoutYProperty().addListener((observable, oldValue, newValue) -> addToSizeChangedQueue(node));
            node.rotateProperty().addListener((observable, oldValue, newValue) -> addToSizeChangedQueue(node));
            node.scaleXProperty().addListener((observable, oldValue, newValue) -> addToSizeChangedQueue(node));
            node.scaleYProperty().addListener((observable, oldValue, newValue) -> addToSizeChangedQueue(node));
        }
    }

    private <T extends Node & PhysicalPropertiesOwner & Physical & PhysicsShape> void addToSizeChangedQueue(T node) {
        if (!isUpdating) {
            sizeChangeQueue.add(node);
        }
    }

    private <T extends Node & GeometricPropertiesOwner & Geometric> Body createBody(T node) {
        GeometricPropertyDefinitions<? extends Styleable> geometricPropertyDefinitions = node.getGeometricPropertyDefinitions();
        BodyDef bodyDefinition = geometricPropertyDefinitions.createBodyDef(typeConverter);
        Vec2 bodyPosition = getBodyPosition(node);
        bodyDefinition.position.set(bodyPosition);

        double rotate = -node.getRotate();
        bodyDefinition.angle = positionHelper.getBodyRadians(rotate);
        bodyDefinition.linearVelocity = coordinateConverter.convertVectorToWorld(geometricPropertyDefinitions.getLinearVelocityX(), geometricPropertyDefinitions.getLinearVelocityY());
        bodyDefinition.angularVelocity = coordinateConverter.scaleVectorToWorld(geometricPropertyDefinitions.getAngularVelocity());
        Body body = world.createBody(bodyDefinition);
        this.nodeBodyMap.put(node, body);

        //Geometric properties
        node.activeProperty().addListener((observable, oldValue, newValue) -> onActiveChanged(node));
        node.allowSleepProperty().addListener((observable, oldValue, newValue) -> onSleepingAllowed(body, newValue));
        node.awakeProperty().addListener((observable, oldValue, newValue) -> onAwakeChanged(body, newValue));
        node.simulationTypeProperty().addListener((observable, oldValue, newValue) -> onBodyTypeChanged(node));
        node.bulletProperty().addListener((observable, oldValue, newValue) -> onBulletChanged(body, newValue));
        node.fixedRotationProperty().addListener((observable, oldValue, newValue) -> onFixedRotationChanged(body, newValue));
        node.gravityScaleProperty().addListener((observable, oldValue, newValue) -> onGravityScaleChanged(body, newValue));
        node.angularDampingProperty().addListener((observable, oldValue, newValue) -> onAngularDampeningChanged(body, newValue));
        node.linearDampingProperty().addListener((observable, oldValue, newValue) -> onLinearDampeningChanged(body, newValue));
        node.linearVelocityXProperty().addListener((observable2, oldValue2, newValue2) -> onLinearVelocityChanged(node));
        node.linearVelocityYProperty().addListener((observable1, oldValue1, newValue1) -> onLinearVelocityChanged(node));
        node.angularVelocityProperty().addListener((observable, oldValue, newValue) -> onAngularVelocityChanged(node));

        //JavaFX properties
        node.layoutXProperty().addListener(getLayoutChangedListener(node));
        node.layoutYProperty().addListener(getLayoutChangedListener(node));
        node.rotateProperty().addListener(getLayoutChangedListener(node));
        node.scaleXProperty().addListener((observable, oldValue, newValue) -> onScaleChanged(node));
        node.scaleYProperty().addListener((observable, oldValue, newValue) -> onScaleChanged(node));

        return body;
    }

    private <T extends Node & GeometricPropertiesOwner & Geometric> void onScaleChanged(T node) {
        if (!sizeChangeQueue.contains(node))
	        sizeChangeQueue.add(node);
    }

    private void onLinearDampeningChanged(Body body, Double newValue) {
        if (!isUpdating)
            body.setLinearDamping(newValue.floatValue());
    }

    private void onAngularDampeningChanged(Body body, Double newValue) {
        if (!isUpdating)
            body.setAngularDamping(newValue.floatValue());
    }

    private void onGravityScaleChanged(Body body, Double newValue) {
        if (!isUpdating)
            body.setGravityScale(newValue.floatValue());
    }

    private void onFixedRotationChanged(Body body, Boolean newValue) {
        if (!isUpdating)
            body.setFixedRotation(newValue);
    }

    private void onBulletChanged(Body body, Boolean newValue) {
        if (!isUpdating)
	        body.setBullet(newValue);
    }

    private void onBodyTypeChanged(Node node) {
        if (!isUpdating)
            typeChangeQueue.add(node);
    }

    private void onAwakeChanged(Body body, Boolean newValue) {
        if (!isUpdating)
	        body.setAwake(newValue);
    }

    private void onSleepingAllowed(Body body, Boolean newValue) {
        if (!isUpdating)
            body.setSleepingAllowed(newValue);
    }

    private void onActiveChanged(Node node) {
        if (!isUpdating)
            activeChangeQueue.add(node);
    }

    private ChangeListener<Number> getLayoutChangedListener(Node node) {
        return (observable, oldValue, newValue) -> {
            if(!isUpdating)
                layoutChangeQueue.add(node);
        };
    }

    private void onLinearVelocityChanged(Geometric node) {
        if (!isUpdating){
            Body body = nodeBodyMap.get(node);
            body.setLinearVelocity(coordinateConverter.convertVectorToWorld(node.getLinearVelocityX(), node.getLinearVelocityY()));
        }
    }

    private void onAngularVelocityChanged(Geometric node) {
        if (!isUpdating){
            Body body = nodeBodyMap.get(node);
            body.setAngularVelocity(coordinateConverter.scaleVectorToWorld(node.getAngularVelocity()));
        }
    }

    private Vec2 getBodyPosition(Node node) {
        Point2D center = positionHelper.getGeometricCenter(node);
        double cX = center.getX() + node.getLayoutX();
        double cY = center.getY() + node.getLayoutY();
        return coordinateConverter.convertNodePointToWorld(cX, cY, node.getParent());
    }

    /**
     * Starts the loaded game
     */
    public void startGame(){
	    worldTimer.start();
    }

    /**
     * Pauses the game, call startGame() to continue
     */
    public void pauseGame(){
        worldTimer.stop();
    }

    /**
     * Stops the loaded game, it is not possible to restart without reloading using this function
     */
    public void stopGame(){
        worldTimer.stop();
        worldTimer = null;
    }
	
	private void updateNodeData(){
		for (Node node : nodeBodyMap.keySet()){
			Body body = nodeBodyMap.get(node);
            physicsToNodeSynchronizer.updateNodeData(node, body);
		}
	}

    /**
     * Sets the level complete event handler, this event will fire when PhysicsWorld.finishLevel is called with completeState true
     * @param onLevelCompleteListener event handler
     */
    public void setOnLevelComplete(LevelFinishEventListener onLevelCompleteListener) {
        this.onLevelCompleteListener = onLevelCompleteListener;
    }

    /**
     * Sets the level failed event handler, this event will fire when PhysicsWorld.finishLevel is called with completeState false
     * @param onLevelFailedListener event handler
     */
    public void setOnLevelFailed(LevelFinishEventListener onLevelFailedListener) {
        this.onLevelFailedListener = onLevelFailedListener;
    }

    /**
     * Checks if debug is enabled
     * @return true if debug is enabled, false if it is disabled
     */
    public boolean isDebugEnabled() {
        return drawDebug;
    }
}
