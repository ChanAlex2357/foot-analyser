package foot.cv;


import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import foot.cv.paint.Paint;
import interfaces.ImageDraw;

public class RectCv extends Rect implements ImageDraw{
    Point[] points ;
    public RectCv(Point[] points){
        super(points[0],points[2]);
        setPoints(points);
    }
    public Point[] getPoints() {
        return points;
    }
    public void setPoints(Point[] points) {
        this.points = points;
    }

    public String printPoints(){
        String points = "";
        for (int i = 0 ; i < getPoints().length ; i++) {
            points += printPoint(getPoints()[i], i) +"\n";
        }
        return points;
    }
    public static String printPoint(Point p , int index){
        return "P"+index+": "+p.x+";"+p.y;
    }

    public Edge getTopEdge(){
        return new Edge(points[0],points[3]);
    }
    public Edge getBottomEdge(){
        return new Edge(points[1],points[2]);
    }
    public Edge getLeftEdge(){
        return new Edge(points[0],points[1]);
    }
    public Edge getRightEdge(){
        return new Edge(points[3],points[2]);
    }
    @Override
    public void draw(Mat src,Paint paint) {
        paint.paintRectangles(src,this);
    }
}
