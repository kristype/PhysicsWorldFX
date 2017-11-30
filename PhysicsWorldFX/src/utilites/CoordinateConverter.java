package utilites;

import framework.nodes.PhysicsWorld;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;

public class CoordinateConverter {
	
	private Region root;

	private OBBViewportTransform viewportTransform;
	private final float physicsScale;

	public CoordinateConverter(PhysicsWorld world) {
		this.root = world;
		physicsScale = (float)world.getPhysicsScale();
		OBBViewportTransform obb = new OBBViewportTransform();
		obb.setTransform(new Mat22(physicsScale, 0.0f, 0.0f, physicsScale));
		obb.setCenter((float) (world.getPrefWidth() / 2), (float) (world.getPrefHeight() / 2));
		obb.setExtents((float) (world.getPrefWidth() / 2), (float) (world.getPrefHeight() / 2));
		obb.setYFlip(true);
		this.viewportTransform = obb;
	}

	public OBBViewportTransform getViewportTransform() {
		return viewportTransform;
	}

	public Vec2 convertNodePointToWorld(double x, double y, Node context) {
		while (context != null && context != root) {
			Bounds bounds = context.getBoundsInParent();
			x += bounds.getMinX();
			y += bounds.getMinY();
			context = context.getParent();
		}
		Vec2 result = new Vec2();
		viewportTransform.getScreenToWorld(new Vec2((float) x, (float) y), result);
		return result;
	}

	public Point2D convertWorldPointToScreen(Vec2 point, Node context) {
		Vec2 result = new Vec2();
		viewportTransform.getWorldToScreen(point, result);

		float xResult = result.x;
		float yResult = result.y;

		while (context != null && context != root) {
			Bounds bounds = context.getBoundsInParent();
			xResult -= bounds.getMinX();
			yResult -= bounds.getMinY();
			context = context.getParent();
		}

		return new Point2D(xResult, yResult);
	}

	public Vec2 convertVectorToWorld(double x, double y) {
		Vec2 result = new Vec2();
		viewportTransform.getScreenVectorToWorld(new Vec2((float) x, (float) y), result);
		return result;
	}

	public Point2D convertVectorToScreen(Vec2 vector) {
		Vec2 result = new Vec2();
		viewportTransform.getWorldVectorToScreen(vector, result);
		return new Point2D(result.x, result.y);
	}

	public Vec2 scaleVectorToWorld(double x, double y){
		return new Vec2((float)x / physicsScale,(float) y /physicsScale);
	}

	public float scaleVectorToWorld(double x){
		return (float)x / physicsScale;
	}

	public double scaleVectorToScreen(float x){
		return x * physicsScale;
	}
}
