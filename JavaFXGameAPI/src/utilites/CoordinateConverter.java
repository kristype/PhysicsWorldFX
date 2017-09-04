package utilites;

import framework.PhysicsWorld;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
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

	private Vec2 fx2worldTemp1 = new Vec2(), fx2worldTemp2 = new Vec2();
	
	public Vec2 convertNodePointToWorld(double x, double y, Parent context) {
		while (context != null && context != root) {
			Bounds bounds = context.getBoundsInParent();
			x += bounds.getMinX();
			y += bounds.getMinY();
			context = context.getParent();
		}
		Vec2 result = fx2worldTemp1;
		fx2worldTemp2.set((float) x, (float) y);
		viewportTransform.getScreenToWorld(fx2worldTemp2, result);
		return result;
	}

	public Point2D convertWorldPointToScreen(Vec2 point, Node context) {
		Vec2 result = world2fxTemp1;
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
		Vec2 result = fx2worldTemp1;
		fx2worldTemp2.set((float) x, (float) y);
		viewportTransform.getScreenVectorToWorld(fx2worldTemp2, result);
		return result;
	}

	public Point2D convertVectorToScreen(Vec2 vector) {
		Vec2 result = fx2worldTemp1;
		fx2worldTemp2.set(vector);
		viewportTransform.getWorldVectorToScreen(fx2worldTemp2, result);
		return new Point2D(result.x, result.y);
	}

	private Vec2 world2fxTemp1 = new Vec2(), world2fxTemp2 = new Vec2();

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
