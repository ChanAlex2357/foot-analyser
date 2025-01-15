package foot.entity;

import org.opencv.core.Scalar;
import ui.color.BlackPalette;

public class Ball extends CircleEntitty {
    public Ball(int x, int y, int radius, Scalar color) {
        super(x, y, radius, color);
        Scalar yellow = new Scalar(0, 255, 255);
        borderColor = yellow;
    }

    public static boolean isBallColor(double[] color) {
        if (color.length < 3) {
            return false;
        }
        return isBallColor(new Scalar(color));
    }

    public static boolean isBallColor(Scalar colorScalar) {
        BlackPalette blackPalette = new BlackPalette();
        return blackPalette.contains(colorScalar);
    }
}
