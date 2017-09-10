package framework;

import bodies.BodyPropertiesOwner;
import bodies.BodyPropertyDefinitions;
import bodies.Geometric;
import framework.events.*;
import framework.nodes.ShapeComposition;
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
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import shapes.FixturePropertiesOwner;
import shapes.PhysicsShape;
import shapes.Physical;
import shapes.SingleShape;
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
    private Collection<Node> typeChangeQueue = new ArrayList<>();
    private Collection<Node> activeChangeQueue = new ArrayList<>();

    private Collection<Body> destroyQueue = new ArrayList<>();
    private ShapeResolver shapeResolver;

    private Region gameContainer;
    private boolean isUpdating;
    private LevelFinishEventListener onLevelFailedListener;
    private LevelFinishEventListener onLevelCompleteListener;

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

		if(this.gameWorld != null){
		    //Calculate the layout so non visible and scaled nodes get the correct center
		    gameWorld.layout();

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
            coordinateConverter = new CoordinateConverter(this.gameWorld);
            physicsShapeHelper = new PhysicsShapeHelper(coordinateConverter, positionHelper);
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
        return new Vec2((float)gameWorld.getGravityX(), (float)gameWorld.getGravityY());
    }

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
                body.setType( typeConverter.Convert(geometric.getBodyType()));
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
                }else if(nodeBodyMap.containsKey(node) && node instanceof ShapeComposition){

                    ShapeComposition compositionNode = (ShapeComposition)node;
                    for (Node child: compositionNode.getChildrenUnmodifiable()) {
                        if (nodeFixtureMap.containsKey(node)) {
                            shapeResolver.updateShape(child, nodeFixtureMap.get(node)); //TODO bytt ut shape hvis den er endret. Muligens bytt ut fixture
                        }
                    }
                }
            }
            sizeChangeQueue.clear();
        }
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
                if (node.isPresent()){
                    cleanUp(node.get().getKey(), body);
                }

            }
            destroyQueue.removeAll(removeList);
        }
    }

    private void cleanUp(Node node, Body body) {
        nodeBodyMap.remove(node);
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

    private <T extends Node & FixturePropertiesOwner & Physical & PhysicsShape> void addFixtureToBody(T node, Body body) {
        FixtureDef fixtureDef = node.getFixturePropertyDefinitions().createFixtureDef();
        fixtureDef.shape = shapeResolver.ResolveShape(node);
        Fixture fixture = body.createFixture(fixtureDef);

        //Shape and collision properties
        node.densityProperty().addListener((observable, oldValue, newValue) -> onDensityChanged(fixture, newValue));
        node.frictionProperty().addListener((observable, oldValue, newValue) -> onFrictionChanged(fixture, newValue));
        node.restitutionProperty().addListener((observable, oldValue, newValue) -> onRestitutionChanged(fixture, newValue));
        node.filterMaskProperty().addListener((observable, oldValue, newValue) -> onFilterChanged(fixture, node));
        node.filterCategoryProperty().addListener((observable, oldValue, newValue) -> onFilterChanged(fixture, node));
        node.filterGroupProperty().addListener((observable, oldValue, newValue) -> onFilterChanged(fixture, node));
        node.sensorProperty().addListener((observable, oldValue, newValue) -> onSensorChanged(fixture, newValue));

        node.setup(body, physicsShapeHelper);

        node.addSizeChangedEventListener(e -> {
            if (!isUpdating)
                sizeChangeQueue.add(e.getNode());
        });
/*        physicsShape.addLayoutChangedEventListener(e -> { Shapes with bodies only for now!
            if(!isUpdating)
                layoutChangeQueue.add(e.getNode());
        });*/
        nodeFixtureMap.put(node, fixture);
    }

    private void onSensorChanged(Fixture fixture, Boolean newValue) {
        fixture.setSensor(newValue);
    }

    private void onFilterChanged(Fixture fixture, Physical node) {
        Filter filter = new Filter();
        filter.maskBits = node.getFilterMask();
        filter.groupIndex = node.getFilterGroup();
        filter.categoryBits = node.getFilterCategory();
        fixture.getFilterData().set(filter);
    }

    private void onRestitutionChanged(Fixture fixture, Double newValue) {
        fixture.setRestitution(newValue.floatValue());
    }

    private void onFrictionChanged(Fixture fixture, Double newValue) {
        fixture.setFriction(newValue.floatValue());
    }

    private void onDensityChanged(Fixture fixture, Double newValue) {
        fixture.setDensity(newValue.floatValue());
    }

    private <T extends Node & BodyPropertiesOwner & Geometric> Body createBody(T node) {
        BodyPropertyDefinitions<? extends Styleable> bodyPropertyDefinitions = node.getBodyPropertyDefinitions();
        BodyDef bodyDefinition = bodyPropertyDefinitions.createBodyDef(typeConverter);
        Vec2 bodyPosition = getBodyPosition(node);
        bodyDefinition.position.set(bodyPosition);

        double rotate = node.getRotate();
        bodyDefinition.angle = positionHelper.getBodyRadians(rotate);
        bodyDefinition.linearVelocity = coordinateConverter.convertVectorToWorld(bodyPropertyDefinitions.getLinearVelocityX(), bodyPropertyDefinitions.getLinearVelocityY());
        bodyDefinition.angularVelocity = coordinateConverter.scaleVectorToWorld(bodyPropertyDefinitions.getAngularVelocity());
        Body body = world.createBody(bodyDefinition);
        this.nodeBodyMap.put(node, body);

        //Geometric properties
        node.activeProperty().addListener((observable, oldValue, newValue) -> onActiveChanged(node));
        node.allowSleepProperty().addListener((observable, oldValue, newValue) -> onSleepingAllowed(body, newValue));
        node.awakeProperty().addListener((observable, oldValue, newValue) -> onAwakeChanged(body, newValue));
        node.bodyTypeProperty().addListener((observable, oldValue, newValue) -> onBodyTypeChanged(node));
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

    private <T extends Node & BodyPropertiesOwner & Geometric> void onScaleChanged(T node) {
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
        if (!isUpdating) //Must be queued
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
        Bounds bounds = node.getLayoutBounds();
        Point2D center = positionHelper.getCenter(bounds);
        double cX = center.getX() + node.getLayoutX();
        double cY = center.getY() + node.getLayoutY();
        return coordinateConverter.convertNodePointToWorld(cX, cY, node.getParent());
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
			Bounds bounds = node.getLayoutBounds();

			Point2D nodePosition = coordinateConverter.convertWorldPointToScreen(body.getPosition(), node.getParent());
            Point2D center = positionHelper.getCenter(bounds);
			double x = nodePosition.getX() - center.getX();
            double y = nodePosition.getY() - center.getY();
            node.setLayoutX(x);
            node.setLayoutY(y);
			double fxAngle = positionHelper.getAngle(body.getAngle());
			node.setRotate(fxAngle);

            Vec2 linearVelocity = body.getLinearVelocity();
            Point2D convertedVelocity = coordinateConverter.convertVectorToScreen(linearVelocity);

            double scaledAngularVelocity = coordinateConverter.scaleVectorToScreen(body.getAngularVelocity());

            Geometric geometric = (Geometric) node;
            geometric.setLinearVelocityX(convertedVelocity.getX());
            geometric.setLinearVelocityY(convertedVelocity.getY());
            geometric.setAngularVelocity(scaledAngularVelocity);
            geometric.setActive(body.isActive());
            geometric.setAwake(body.isAwake());
		}
	}

    public void setOnLevelComplete(LevelFinishEventListener onLevelCompleteListener) {
        this.onLevelCompleteListener = onLevelCompleteListener;
    }

    public void setOnLevelFailed(LevelFinishEventListener onLevelFailedListener) {
        this.onLevelFailedListener = onLevelFailedListener;
    }

    public boolean isDebugEnabled() {
        return drawDebug;
    }
}
