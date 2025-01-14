package foot.entity;
import javax.swing.*;

import java.awt.*;

public class Terrain {
    private Image image;
    private Dimension size;
    private boolean isVertical;
    private String imagePath;

    private Player[] players;
    private Ball ball;

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

    public void setImage(Image image) {
        this.image = image;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public void setVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }
}
