package foot.cv;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import foot.cv.paint.Paint;
import interfaces.ImageDraw;

public class Edge implements ImageDraw{
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
    @Override
    public void draw(Mat src,Paint paint) {
        paint.paintEdge(src,this);
    }

}
