package utilites;

import java.util.List;

import javafx.scene.layout.Region;
import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;

import framework.PhysicsWorld;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Line;

public class CoordinateConverter {
	
	private Region root;

	private OBBViewportTransform viewportTransform;
	private final float physicsScale;

	public CoordinateConverter(PhysicsWorld world, Region root) {
		this.root = root;
		physicsScale = (float)world.getPhysicsScale();
		OBBViewportTransform obb = new OBBViewportTransform();
		obb.setTransform(new Mat22((float) physicsScale, 0.0f, 0.0f, (float) physicsScale));
		obb.setCenter((float) (root.getWidth() / 2), (float) (root.getHeight() / 2));
		obb.setExtents((float) (root.getWidth() / 2), (float) (root.getHeight() / 2));
		obb.setYFlip(true);
		this.viewportTransform = obb;
	}

	public OBBViewportTransform getViewportTransform() {
		return viewportTransform;
	}

	private Vec2 fx2worldTemp1 = new Vec2(), fx2worldTemp2 = new Vec2();
	
	public Point2D fxPoint2world(double x, double y, Parent context) {
		while (context != null && context != root) {
			Bounds bounds = context.getBoundsInParent();
			x += bounds.getMinX();
			y += bounds.getMinY();
			context = context.getParent();
		}
		Vec2 result = fx2worldTemp1;
		fx2worldTemp2.set((float) x, (float) y);
		viewportTransform.getScreenToWorld(fx2worldTemp2, result);
		return new Point2D(result.x, result.y);
	}

	public double fxScaleToWorld(double value){
		Vec2 result = fx2worldTemp1;
		fx2worldTemp2.set((float) value, (float) 0);
		viewportTransform.getScreenVectorToWorld(fx2worldTemp2, result);
		return result.x;
	}

	public Point2D fxVec2world(double x, double y) {
		Vec2 result = fx2worldTemp1;
		fx2worldTemp2.set((float) x, (float) y);
		viewportTransform.getScreenVectorToWorld(fx2worldTemp2, result);
		return new Point2D(result.x, result.y);
	}

	private Vec2 world2fxTemp1 = new Vec2(), world2fxTemp2 = new Vec2();
	
	public Point2D world2fx(double x, double y, Node context) {
		Vec2 result = world2fxTemp1;
		world2fxTemp2.set((float) x, (float) y);
		viewportTransform.getWorldToScreen(new Vec2((float) x, (float) y), result);

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
	
	public Vec2 scaleVecToWorld(double x, double y){
		return new Vec2((float)x / physicsScale,(float) y /physicsScale);
	}

	public float scaleVecToWorld(double x){
		return (float)x / physicsScale;
	}

	public Vec2 convertToPhysicsCoordinates(double x, double y){
		Vec2 argWorld = new Vec2();
		viewportTransform.getWorldToScreen(new Vec2((float)x, (float)y), argWorld);
		return argWorld;
	}
}
