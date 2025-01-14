package foot.entity;

import org.opencv.core.Scalar;

public class CircleEntitty extends TerrainEntity{
    double radius;
    public CircleEntitty(double[] data , Scalar color){
        this(data[0], data[1], data[2], color);   
    }
    public CircleEntitty(double x , double y , double radius , Scalar color){
        super(x, y, color);
        setRadius(radius);
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
