package foot.cv;

import java.awt.Point;

import org.opencv.core.Scalar;

public class Edge {
    Point startPoint;
    Point endPoint;
    Scalar color;
    public Edge(Point p1,Point p2){
        setEndPoint(p2);
        setStartPoint(p1);
        setColor(new Scalar(255, 0, 255));
    }
    public Point getStartPoint() {
        return startPoint;
    }
    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }
    public Point getEndPoint() {
        return endPoint;
    }
    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }
    public Scalar getColor() {
        return color;
    }
    public void setColor(Scalar color) {
        this.color = color;
    }
}
