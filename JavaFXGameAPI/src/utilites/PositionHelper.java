package utilites;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class PositionHelper {

    public float getCenter(float min, float max) {
        return min <= 0 && max <= 0 ?  max + (min - max) / 2 : min + (max - min) /2;
    }

    public double getCenter(double min, double max) {
        return min <= 0 && max <= 0 ?  max + (min - max) / 2 : min + (max - min) /2;
    }

    public Point2D getGeometricCenter(Node node){
        Bounds bounds = node.getLayoutBounds();
        return getCenter(bounds);
    }

    public Point2D getCenter(Bounds bounds){
        double centerX = getCenter(bounds.getMinX(), bounds.getMaxX());
        double centerY = getCenter(bounds.getMinY(), bounds.getMaxY());
        return new Point2D(centerX, centerY);
    }

    public void scaleAndRotate(double[] points, double scaleX, double scaleY, double rotate) {
        //Find bounds so center can be determined
        double minY = Double.MAX_VALUE;
        double minX = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            minX = Math.min(minX, points[i]);
            maxX = Math.max(maxX, points[i]);
            minY = Math.min(minY, points[i + 1]);
            maxY = Math.max(maxY, points[i + 1]);
        }

        double centerX = getCenter(minX, maxX);
        double centerY = getCenter(minY, maxY);

        double radians = getBodyRadians(rotate);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        for (int i = 0; i < points.length; i += 2) {
            //Subtract center
            points[i]  -= centerX;
            points[i+1] -= centerY;

            //scale normalised coordinates
            points[i] *= scaleX;
            points[i+1] *= scaleY;

            double x = points[i];
            double y = points[i + 1];

            points[i] = cos * x - sin * y;
            points[i+1] = sin * x + cos * y;

            //Add center
            points[i]  += centerX;
            points[i+1] += centerY;
        }
    }

    public void offsetPoints(double[] points, double dx, double dy, double cX, double cY) {
        double offsetX = dx - cX;
        double offsetY = cY - dy;

        for (int i = 0; i < points.length; i += 2) {
            points[i] += offsetX;
            points[i + 1] = offsetY - points[i + 1];
        }
    }

    public float getBodyRadians2(double rotate) {
        return rotate > 180 ? (float)Math.toRadians(rotate) : (float)Math.toRadians(-rotate);
    }

    public float getBodyRadians(double rotate) {
        return (float)Math.toRadians(rotate);
    }

    public void scaleWithParent(double[] points, double parentScaleX, double parentScaleY, double cX, double cY) {
        for (int i = 0; i < points.length; i += 2) {
            //Subtract center
            points[i]  -= cX;
            points[i+1] -= cY;

            //scale normalised coordinates
            points[i] *= parentScaleX;
            points[i+1] *= parentScaleY;

            //Add center
            points[i]  += cX;
            points[i+1] += cY;
        }
    }

    public double getAngle(double angle) {
        return (-angle * 180 / Math.PI) % 360;
    }
}
