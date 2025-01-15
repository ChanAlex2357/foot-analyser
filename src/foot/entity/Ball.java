package foot.entity;

import org.opencv.core.Scalar;

public class Ball extends CircleEntitty{
    public Ball(int x , int y , int radius , Scalar color) {
        super(x, y, radius,color);
        Scalar yellow = new Scalar(0, 255, 255);
        borderColor = yellow;
    }

    public static boolean isBallColor(double[] color){
        if (color.length < 3) {return false;}
        if (color[0] == 0 && color[1] == 0 && color[2] == 0 ) {
            return true;
        }
        return false;
    }
}
