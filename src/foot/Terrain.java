package foot;
import javax.swing.*;

import java.awt.*;

public class Terrain {
    private Image image;
    private Dimension size;
    private boolean isVertical;

    public Terrain(ImageIcon imageIcon, Dimension panelSize) {
        this.size = new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        this.isVertical = size.height > size.width;
        this.image = imageIcon.getImage().getScaledInstance(panelSize.width, panelSize.height, Image.SCALE_SMOOTH);
    }

    public Dimension getSize() {
        return size;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public Image getImage() {
        return image;
    }
}
