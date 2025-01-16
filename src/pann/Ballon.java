package pann;

import org.opencv.core.Rect;

public class Ballon {
    private Rect position;

    public Ballon(Rect position) {
        this.position = position;
    }

    public Rect getPosition() {
        return position;
    }

    public void setPosition(Rect position) {
        this.position = position;
    }
}
