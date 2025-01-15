package ui.color;

import org.opencv.core.Scalar;

public class BlackPalette extends ColorPalette {

    public BlackPalette() {
        super();
        addColor(new Scalar(0, 0, 0)); // Pure black
        addColor(new Scalar(50, 50, 50)); // Dark gray
        addColor(new Scalar(30, 30, 30)); // Another shade of black
        addColor(new Scalar(20, 20, 20)); // Yet another shade of black
    }
}
