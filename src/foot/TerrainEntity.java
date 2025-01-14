package foot;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class TerrainEntity {
    Point coord;
    Scalar color;
    public TerrainEntity(int x , int y , Scalar color) {
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

    private void setCoord(int x , int y){
        setCoord(new Point(x, y));
    }
}
