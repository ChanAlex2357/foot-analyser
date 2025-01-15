package ui.color;

import org.opencv.core.Scalar;

public class BluePalette extends ColorPalette {

    public BluePalette() {
        super();
        addColor(new Scalar(255, 0, 0)); // Pure blue
        addColor(new Scalar(200, 0, 0)); // Dark blue
        addColor(new Scalar(150, 0, 0)); // Another shade of blue
        addColor(new Scalar(100, 0, 0)); // Yet another shade of blue
    }
}
