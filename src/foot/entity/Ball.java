package foot.entity;

import org.opencv.core.Scalar;

public class Ball extends CircleEntitty{
    public Ball(int x , int y , int radius , Scalar color) {
        super(x, y, radius,color);
        Scalar yellow = new Scalar(0, 255, 255);
        borderColor = yellow;
    }
}
