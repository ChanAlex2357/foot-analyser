package foot.utils;

import org.opencv.core.Point;

import java.util.List;

public class PointUtils {

    public static Point getClosestPoint(Point referencePoint, List<Point> points) {
        if (points == null || points.isEmpty()) {
            return null;
        }

        Point closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Point point : points) {
            double distance = Math.sqrt(Math.pow(point.x - referencePoint.x, 2) + Math.pow(point.y - referencePoint.y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        return closestPoint;
    }
}
