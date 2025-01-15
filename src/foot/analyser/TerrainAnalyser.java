package foot.analyser;

import java.util.List;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import foot.cv.Edge;
import foot.cv.RectCv;
import foot.cv.detector.CircleDetector;
import foot.cv.detector.RectangleDetector;
import foot.cv.util.ImageUtils;
import foot.entity.Ball;
import foot.entity.Player;
import foot.entity.Team;
import foot.entity.Terrain;
import ui.components.panel.CircleDetectionConfigPanel;

public class TerrainAnalyser {
    CircleDetector circleDetector;
    RectangleDetector rectangleDetector;
    Mat imageSrc;
    Terrain terrain;
    // Shapes
    Mat circles;
    RectCv[] rectangles;
    public TerrainAnalyser(Terrain terrain,CircleDetectionConfigPanel configPanel){
        setTerrain(terrain);
        setImageSrc( ImageUtils.loadImage(getTerrain().getImagePath()));
        setDetectors(configPanel);
        loadShapes();
        loadEntities();
    }
    private void setDetectors(CircleDetectionConfigPanel configPanel){
        setCircleDetector(new CircleDetector(configPanel));
        setRectangleDetector(new RectangleDetector());
    }
    private void loadShapes(){
        setCircles(getCircleDetector().detect(getImageSrc()));
        setRectangles(getRectangleDetector());
    }
    public void loadEntities(){
        loadBall();
        loadTeams();
        loadTerrain();
        loadEdges();
    }
    public void loadEdges(){
        getTerrain().dispatchEdges();
    }
    public void loadTerrain() {
        int max_size = 0;
        RectCv max = null;
        for (RectCv rectCv : rectangles) {
            if (rectCv.area() > max_size) {
                max = rectCv;
            }
        }
        this.getTerrain().setRectangle(max);
    }
    public CircleDetector getCircleDetector() {
        return circleDetector;
    }
    public void setCircleDetector(CircleDetector circleDetector) {
        this.circleDetector = circleDetector;
    }
    public Mat getImageSrc() {
        return imageSrc;
    }
    public void setImageSrc(Mat imageSrc) {
        this.imageSrc = imageSrc;
    }
    public Terrain getTerrain() {
        return terrain;
    }
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
    public Mat getCircles() {
        return circles;
    }
    public void setCircles(Mat circles) {
        this.circles = circles;
    }

    public void paintBall(){
        Ball ball = getTerrain().getBall();
        if (ball != null) {
            ball.draw(imageSrc);   
        }
    }

    public void paintPlayers(){
        List<Player> players = getTerrain().getPlayers();
        if (players.size() <= 0) {
            return;
        }
        for (Player player : players) {
            player.draw(imageSrc);
        }
    }
    public void paintCircles(){
        paintPlayers();
        paintBall();
    }
    public void paintTerrain(){
        // this.getTerrain().draw(imageSrc);
    }
    public void paintRectangles(){
        paintTerrain();
    }

    public List<Player> loadPlayers() {
        Mat circles = getCircles();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < circles.cols(); i++) {
            double[] circle = circles.get(0, i);
            if (circle == null) {
                continue;
            }
            int centerX = (int) Math.round(circle[0]);
            int centerY = (int) Math.round(circle[1]);
            int radius = (int) Math.round(circle[2]);

            // Vérifier que les coordonnées sont valides
            if (centerX >= 0 && centerX < getImageSrc().cols() && centerY >= 0 && centerY < getImageSrc().rows()) {
                double[] color = getImageSrc().get(centerY, centerX);
                if (Ball.isBallColor(color)) {continue;}
                // Get the color at the center of the circle
                Scalar colorScalar = new Scalar(color);
                Player newPlayer = new Player(centerX, centerY, radius, colorScalar , null);
                players.add(newPlayer);
            }
        }
        getTerrain().setPlayers(players);
        return players;
    }

    public Ball loadBall() {
        Mat circles = getCircles();
        for (int i = 0; i < circles.cols(); i++) {
            double[] circle = circles.get(0, i);
            if (circle == null) {
                continue;
            }
            int centerX = (int) Math.round(circle[0]);
            int centerY = (int) Math.round(circle[1]);
            int radius = (int) Math.round(circle[2]);

            // Vérifier que les coordonnées sont valides
            if (centerX >= 0 && centerX < getImageSrc().cols() && centerY >= 0 && centerY < getImageSrc().rows()) {
                // Get the color at the center of the circle
                double[] color = getImageSrc().get(centerY, centerX);
                if (color != null && color[0] == 0 && color[1] == 0 && color[2] == 0) {
                    Scalar colorScalar = new Scalar(color);
                    getTerrain().setBall(
                        new Ball(centerX, centerY, radius, colorScalar)
                    );
                    return getTerrain().getBall();
                }
            }
        }
        return null; // No black circle found
    }

    public Player getPlayerWithBall() {
        Ball ball = getTerrain().getBall();
        if (ball == null) {
            return null;
        }

        List<Player> players = getTerrain().getPlayers();
        Player playerWithBall = null;
        double minDistance = Double.MAX_VALUE;

        for (Player player : players) {
            double distance = Math.sqrt(Math.pow(player.getX() - ball.getX(), 2) + Math.pow(player.getY() - ball.getY(), 2));
            if (distance < minDistance) {
                minDistance = distance;
                playerWithBall = player;
            }
        }
        return playerWithBall;
    }

    public String getAttackingTeam() {
        Player playerWithBall = getPlayerWithBall();
        if (playerWithBall == null) {
            return "Unknown";
        }

        // Assuming the team color of the player with the ball determines the attacking team
        Scalar attackingTeamColor = playerWithBall.getColor();
        Team[] teams = getTerrain().getTeams();
        for (Team team : teams) {
            if (team.getColor().equals(attackingTeamColor)) {
                return "Attacking Team: " + team.getName();
            }
        }

        return "Unknown";
    }


    public void loadOffside(){
        List<Player> players = getOffsidePlayers();
        System.out.println("OFFSIDE : "+players.size());
        for (Player player : players) {
            player.setBorderColor(new Scalar(128, 0, 128)); // Violet color
        }
    }

    public List<Player> getOffsidePlayers() {
        Player playerWithBall = getPlayerWithBall();
        if (playerWithBall == null) {
            return new ArrayList<>();
        }
        System.out.println("Player with ball : "+playerWithBall);
        playerWithBall.setBorderColor(new Scalar(128, 0, 128));
        Scalar attackingTeamColor = playerWithBall.getColor();
        Team[] teams = getTerrain().getTeams();
        Team attackingTeam = null;
        Team defendingTeam = null;

        for (Team team : teams) {
            if (team.getColor().equals(attackingTeamColor)) {
                attackingTeam = team;
            } else {
                defendingTeam = team;
            }
        }

        if (attackingTeam == null || defendingTeam == null) {
            return new ArrayList<>();
        }

        // Déterminer la position du dernier défenseur
        int lastDefenderY = Integer.MAX_VALUE;
        for (Player defender : defendingTeam.getPlayers()) {
            if (defender.getY() < lastDefenderY) {
                lastDefenderY = defender.getY();
            }
        }

        // Trouver les joueurs attaquants qui sont hors-jeu
        List<Player> offsidePlayers = new ArrayList<>();
        for (Player attacker : attackingTeam.getPlayers()) {
            if (attacker.getY() < lastDefenderY) {
                offsidePlayers.add(attacker);
            }
        }

        return offsidePlayers;
    }

    public void loadTeams() {
        List<Player> players = loadPlayers();
        if (players.isEmpty()) {
            return;
        }

        // Assuming players are divided into two teams based on their colors
        List<Player> team1Players = new ArrayList<>();
        List<Player> team2Players = new ArrayList<>();
        Team team1 = new Team("Team 1", players.get(0).getColor(), team1Players);
        Team team2 = new Team("Team 2", null, team2Players);

        for (Player player : players) {
            if (player.getColor().equals(team1.getColor())) {
                team1.addPlayer(player);
            } else {
                if (team2.getColor() == null) {
                    team2.setColor(player.getColor());
                }
                team2.addPlayer(player);
            }
        }
        getTerrain().setTeams(new Team[]{team1, team2});
    }
    public RectangleDetector getRectangleDetector() {
        return rectangleDetector;
    }
    public void setRectangleDetector(RectangleDetector rectangleDetector) {
        this.rectangleDetector = rectangleDetector;
    }
    public RectCv[] getRectangles() {
        return rectangles;
    }
    public void setRectangles(RectCv[] rects) {
        this.rectangles = rects;
    }
    private void setRectangles(RectangleDetector rectangleDetector){
        rectangleDetector.detect(getImageSrc());
        setRectangles(rectangleDetector.getRectangles());
    }

    public void paint(){
        paintCircles();
        paintRectangles();
    }
}
