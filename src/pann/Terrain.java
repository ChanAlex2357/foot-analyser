package pann;

import org.opencv.core.Rect;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private List<Rect> contours;

    public Terrain() {
        this.contours = new ArrayList<>();
    }

    public void addContour(Rect contour) {
        this.contours.add(contour);
    }

    public List<Rect> getContours() {
        return contours;
    }
}
