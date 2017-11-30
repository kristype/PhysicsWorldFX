package utilites.debug;

import java.util.*;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.*;
import org.jbox2d.pooling.arrays.Vec2Array;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * Implementation of {@link DebugDraw} that uses JavaFX! Hooray!</br>
 *
 * @author Daniel Murphy Initial Java 2D implementation
 * @author Hallvard Traetteberg JavaFX port
 */
public class DebugDrawJavaFX extends DebugDraw {

    private final Canvas canvas;
    private Rectangle2D circle = new Rectangle2D(-1, -1, 2, 2);

    public DebugDrawJavaFX(Canvas canvas, OBBViewportTransform viewportTransform) {
        super(viewportTransform);
        viewportTransform.setYFlip(true);
        this.canvas = canvas;
        this.stroke = 1.0;
    }

    private List<Color> colors = new ArrayList<Color>();

    private Color getColor(float x, float y, float z) {
        return getColor(x, y, z, 1.0f);
    }

    private Color getColor(float x, float y, float z, float alpha) {
        for (Color color : colors) {
            if (color.getRed() == x && color.getGreen() == y && color.getBlue() == z && color.getOpacity() == alpha) {
                return color;
            }
        }
        Color newColor = new Color(x, y, z, alpha);
        colors.add(newColor);
        return newColor;
    }

    private final Vec2Array vec2Array = new Vec2Array();

    @Override
    public void drawPoint(Vec2 argPoint, float argRadiusOnScreen, Color3f argColor) {
        getWorldToScreenToOut(argPoint, sp1);
        GraphicsContext g = getGraphics();

        Color c = getColor(argColor.x, argColor.y, argColor.z);
        g.setStroke(c);
        sp1.x -= argRadiusOnScreen;
        sp1.y -= argRadiusOnScreen;
        g.fillOval((int) sp1.x, (int) sp1.y, (int) argRadiusOnScreen * 2, (int) argRadiusOnScreen * 2);
    }

    private final Vec2 sp1 = new Vec2();
    private final Vec2 sp2 = new Vec2();

    @Override
    public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {
        getWorldToScreenToOut(p1, sp1);
        getWorldToScreenToOut(p2, sp2);

        Color c = getColor(color.x, color.y, color.z);
        GraphicsContext g = getGraphics();
        g.setStroke(c);
        g.setLineWidth(0.0);
        g.strokeLine(sp1.x, sp1.y, sp2.x, sp2.y);
    }

    public void drawAABB(AABB argAABB, Color3f color) {
        Vec2 vecs[] = vec2Array.get(4);
        argAABB.getVertices(vecs);
        drawPolygon(vecs, 4, color);
    }

    private Affine tr = new Affine(), oldTrans = new Affine();
    private double stroke, oldStroke;

    private void saveState(GraphicsContext g) {
        oldTrans = g.getTransform();
        oldStroke = g.getLineWidth();
    }

    private void restoreState(GraphicsContext g) {
        g.setTransform(oldTrans);
        g.setLineWidth(oldStroke);
    }

    private double transformGraphics(GraphicsContext g, Vec2 center) {
        Vec2 e = viewportTransform.getExtents();
        Vec2 vc = viewportTransform.getCenter();
        Mat22 vt = ((OBBViewportTransform)viewportTransform).getTransform();

        tr.setToTransform(vt.ex.x, - vt.ex.y, e.x, vt.ey.x, - vt.ey.y, e.y);
        tr.appendTranslation(-vc.x, -vc.y);
        tr.appendTranslation(center.x, center.y);
        g.setTransform(tr);
        return vt.ex.x;
    }

    @Override
    public void drawCircle(Vec2 center, float radius, Color3f color) {
        GraphicsContext g = getGraphics();
        Color s = getColor(color.x, color.y, color.z, 1.0f);
        saveState(g);
        double scaling = transformGraphics(g, center) * radius;
        g.setLineWidth(stroke / scaling);
        g.scale(radius, radius);
        g.setStroke(s);
        g.strokeOval(circle.getMinX(), circle.getMinX(), circle.getWidth(), circle.getHeight());
        restoreState(g);
    }

    @Override
    public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color3f color) {
        GraphicsContext g = getGraphics();
        Color f = getColor(color.x, color.y, color.z, .4f);
        Color s = getColor(color.x, color.y, color.z, 1f);
        saveState(g);
        double scaling = transformGraphics(g, center) * radius;
        g.setLineWidth(stroke / scaling);
        g.scale(radius, radius);
        g.setFill(f);
        g.fillOval(circle.getMinX(), circle.getMinX(), circle.getWidth(), circle.getHeight());
        g.setStroke(s);
        g.strokeOval(circle.getMinX(), circle.getMinX(), circle.getWidth(), circle.getHeight());
        if (axis != null) {
            g.rotate(MathUtils.atan2(axis.y, axis.x));
            g.strokeLine(0, 0, 1, 0);
        }
        restoreState(g);
    }

    private final Vec2 zero = new Vec2();
    private final Color pcolorA = new Color(1f, 1f, 1f, .4f);


    private final Color pcolor = new Color(1f, 1f, 1f, 1f);

    private final Vec2 temp = new Vec2();
    private final static DoubleArray xDoublePool = new DoubleArray();
    private final static DoubleArray yDoublePool = new DoubleArray();

    @Override
    public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
        Color f = getColor(color.x, color.y, color.z, .4f);
        Color s = getColor(color.x, color.y, color.z, 1f);
        GraphicsContext g = getGraphics();
        saveState(g);
        double[] xs = xDoublePool.get(vertexCount);
        double[] ys = yDoublePool.get(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            getWorldToScreenToOut(vertices[i], temp);
            xs[i] = temp.x;
            ys[i] = temp.y;
        }
        g.setLineWidth(stroke);
        g.setFill(f);
        g.fillPolygon(xs, ys, vertexCount);
        g.setStroke(s);
        g.strokePolygon(xs, ys, vertexCount);
        restoreState(g);
    }

    @Override
    public void drawPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
        Color s = getColor(color.x, color.y, color.z, 1f);
        GraphicsContext g = getGraphics();
        saveState(g);
        double[] xs = xDoublePool.get(vertexCount);
        double[] ys = yDoublePool.get(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            getWorldToScreenToOut(vertices[i], temp);
            xs[i] = temp.x;
            ys[i] = temp.y;
        }
        g.setLineWidth(stroke);
        g.setStroke(s);
        g.strokePolygon(xs, ys, vertexCount);
        restoreState(g);
    }

    @Override
    public void drawString(float x, float y, String s, Color3f color) {
        GraphicsContext g = getGraphics();
        if (g == null) {
            return;
        }
        Color c = getColor(color.x, color.y, color.z);
        g.setFill(c);
        g.fillText(s, x, y);
    }

    private GraphicsContext getGraphics() {
        return canvas.getGraphicsContext2D();
    }

    private final Vec2 temp2 = new Vec2();

    @Override
    public void drawTransform(Transform xf) {
        GraphicsContext g = getGraphics();
        getWorldToScreenToOut(xf.p, temp);
        temp2.setZero();
        float k_axisScale = 0.4f;

        Color c = getColor(1, 0, 0);
        g.setStroke(c);

        temp2.x = xf.p.x + k_axisScale * xf.q.c;
        temp2.y = xf.p.y + k_axisScale * xf.q.s;
        getWorldToScreenToOut(temp2, temp2);
        g.strokeLine(temp.x, temp.y, temp2.x, temp2.y);

        c = getColor(0, 1, 0);
        g.setStroke(c);
        temp2.x = xf.p.x + -k_axisScale * xf.q.s;
        temp2.y = xf.p.y + k_axisScale * xf.q.c;
        getWorldToScreenToOut(temp2, temp2);
        g.strokeLine(temp.x, temp.y, temp2.x, temp2.y);
    }
}


