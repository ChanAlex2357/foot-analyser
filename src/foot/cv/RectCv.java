package foot.cv;


import org.opencv.core.Point;
import org.opencv.core.Rect;

public class RectCv extends Rect{
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
    private String printPoint(Point p , int index){
        return "P"+index+": "+p.x+";"+p.y;
    }
}
