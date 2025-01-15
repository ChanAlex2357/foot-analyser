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

    public static Point[] createRectPoints(double x , double y , double w , double h){
        Point[] points = new Point[4];
        points[0] = new Point(x,y);
        points[1] = new Point(x, y+h);
        points[2] = new Point(x+w,y+h);
        points[3] = new Point(x+w,y);
        return points;
    }
}
