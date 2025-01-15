package foot.entity;

import org.opencv.core.Scalar;
public class Player  extends CircleEntitty{
    public Player(int x , int y , int radius , Scalar color) {
        super(x, y, radius,color);
        setBorderColor(new Scalar(new  double[]{0,0,0}));
    }
}
