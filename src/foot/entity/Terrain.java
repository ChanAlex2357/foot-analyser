package foot.entity;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import foot.comparator.SortByDistanceOnEdge;
import foot.cv.Edge;
import foot.cv.RectCv;
import foot.cv.paint.Paint;

public class Terrain extends TerrainEntity{
    private Image image;
    private Dimension size;
    private boolean isVertical;
    private String imagePath;
    private RectCv rectangle;
    private List<Player> players;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
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
        Edge[] edges = new Edge[2];

        if (isVertical) {
            edges[0] = getRectangle().getTopEdge();
            edges[1] = getRectangle().getBottomEdge();
        }
        else {
            edges[0] = getRectangle().getLeftEdge();
            edges[1] = getRectangle().getRightEdge();
        }
        return edges;
    }

    public Edge[] getSideEdges(){
        Edge[] edges = new Edge[2];

        if (isVertical) {
            edges[0] = getRectangle().getLeftEdge();
            edges[1] = getRectangle().getRightEdge();
        }
        else {
            edges[0] = getRectangle().getTopEdge();
            edges[1] = getRectangle().getBottomEdge();
        }
        return edges;
    }

    @Override
    public void draw(Mat src, Paint painter) {
        painter.paintRectangles(src, rectangle);
    }
    public double calcDistance(Player player , Edge edge){
        return player.calculerDistanceFromEdge(edge,isVertical);
    }
    public Player findPlayerCloseToEdge(Edge edge){
        Player closest = null;
        sortPlayersByEdge(edge);
        closest = getPlayers().get(0);
        return closest;
    }

    public void sortPlayersByEdge(Edge edge ){
        getPlayers().sort( new SortByDistanceOnEdge(edge));
    }

    public void dispatchEdges(){
        Edge[] edges = getGoalEdges();
        sortPlayersByEdge(edges[0]);
        Player p_close = getPlayers().get(0);
        Player p_far = getPlayers().get(getPlayers().size()-1);
        p_close.getTeam().setTeamEdge(edges[0]);
        p_close.setGoal();
        p_far.getTeam().setTeamEdge(edges[1]);
        p_far.setGoal();
    }
}
