package foot.entity;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class TerrainEntity {
    Point coord;
    Scalar color;
    public TerrainEntity(double x , double y , Scalar color) {
        setCoord(x , y);
        setColor(color);
    }
    
    public Point getCoord() {
        return coord;
    }
    public void setCoord(Point coord) {
        this.coord = coord;
    }
    public Scalar getColor() {
        return color;
    }
    public void setColor(Scalar color) {
        this.color = color;
    }

    private void setCoord(double x , double y){
        setCoord(new Point(x, y));
    }
}
