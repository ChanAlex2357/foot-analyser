package ui.color;

import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

public class ColorPalette {
    private List<Scalar> colors;

    public ColorPalette() {
        colors = new ArrayList<>();
    }

    public void addColor(Scalar color) {
        colors.add(color);
    }

    public boolean contains(Scalar color) {
        for (Scalar paletteColor : colors) {
            if (isSimilarColor(paletteColor, color)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSimilarColor(Scalar color1, Scalar color2) {
        double threshold = 50.0; // Adjust this threshold as needed
        double distance = Math.sqrt(Math.pow(color1.val[0] - color2.val[0], 2) +
                                    Math.pow(color1.val[1] - color2.val[1], 2) +
                                    Math.pow(color1.val[2] - color2.val[2], 2));
        return distance < threshold;
    }
}
