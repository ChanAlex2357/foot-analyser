package foot.entity;
import javax.swing.*;

import org.opencv.core.Mat;

import foot.cv.Edge;
import foot.cv.RectCv;
import foot.cv.paint.Paint;

import java.awt.*;

public class Terrain extends TerrainEntity{
    private Image image;
    private Dimension size;
    private boolean isVertical;
    private String imagePath;
    private RectCv rectangle;
    private Player[] players;
    private Ball ball;
    private Team[] teams;

    public Terrain(ImageIcon imageIcon, Dimension panelSize) {
        super(0, 0, null);
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

    public Team[] getTeams() {
        return teams;
    }

    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

    @Override
    public void draw(Mat src) {
        Paint paint = new Paint();
        paint.paintRectangles(src, getRectangle());
    }

    public int getMaxX(){
        return this.getX() + this.getSize().width;
    }
    public int getMaxY(){
        return this.getY() + this.getSize().height;
    }

    public RectCv getRectangle() {
        return rectangle;
    }

    public void setRectangle(RectCv rectangle) {
        this.rectangle = rectangle;
    }

    public Edge[] getGoalEdges(){
        Edge[] edges = null;
        if (isVertical) {
            
        }

        return edges;
    }

    public Edge[] getSideEdges(){
        return null;
    }
}
