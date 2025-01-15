package foot.entity;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import foot.cv.paint.Paint;

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

    @Override
    public void draw(Mat src, Paint painter) {
        Scalar c = borderColor;
        if (c == null) {
            c = new Scalar(50, 50, 50);
        }
        painter.paintCircle(src, coord, (int) getRadius(), c);
    }
}
