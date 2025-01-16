package pann;

import org.opencv.core.Rect;

public class Joueur {
    private Rect position;

    public Joueur(Rect position) {
        this.position = position;
    }

    public Rect getPosition() {
        return position;
    }

    public void setPosition(Rect position) {
        this.position = position;
    }
}
