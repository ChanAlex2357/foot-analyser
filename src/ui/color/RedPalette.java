package ui.color;

import org.opencv.core.Scalar;

public class RedPalette extends ColorPalette {

    public RedPalette() {
        super();
        addColor(new Scalar(0, 0, 255)); // Pure red
        addColor(new Scalar(0, 0, 200)); // Dark red
        addColor(new Scalar(0, 0, 150)); // Another shade of red
        addColor(new Scalar(0, 0, 100)); // Yet another shade of red
    }
}
