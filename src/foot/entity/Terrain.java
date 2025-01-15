package foot.entity;
import java.awt.Dimension;
import java.awt.Image;
import java.util.List;

import javax.swing.*;

import org.opencv.core.Mat;

import foot.comparator.SortByDistanceOnEdge;
import foot.cv.Edge;
import foot.cv.RectCv;
import foot.cv.paint.Paint;
import foot.utils.PointUtils;

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
        this.image = imageIcon.getImage();
        this.setRectangle(new RectCv(PointUtils.createRectPoints(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight())));
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
        for (Team team : teams) {
            if (isVertical) {
                team.axis = Team.Y_AXIS;
            }
            else{
                team.axis = Team.X_AXIS;
            }
        }
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
        if (rectangle ==  null) {
            return;
        }
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
        getPlayers().sort( new SortByDistanceOnEdge(edge,isVertical));
    }

    private void dispatchEdge(Edge edge ) 
    {
        sortPlayersByEdge(edge);
        for (Player player : getPlayers()) {
            Team team = player.getTeam();
            if (team != null && team.getTeamEdge() == null) {
                team.setTeamEdge(edge);
                player.setGoal();
                break;
            }
        }

    }
    public void dispatchEdges(){
        Edge[] edges = getGoalEdges();
        for (Edge edge : edges) {
            dispatchEdge(edge);
        }
    }
}
