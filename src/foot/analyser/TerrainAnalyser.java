package foot.analyser;

import foot.cv.paint.Paint;

import java.util.List;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import foot.cv.detector.CircleDetector;
import foot.cv.util.ImageUtils;
import foot.entity.Ball;
import foot.entity.Player;
import foot.entity.Team;
import foot.entity.Terrain;
import ui.components.panel.CircleDetectionConfigPanel;

public class TerrainAnalyser {
    CircleDetector circleDetector;
    Mat imageSrc;
    Terrain terrain;
    Mat circles;
    public TerrainAnalyser(Terrain terrain,CircleDetectionConfigPanel configPanel){
        setTerrain(terrain);
        setImageSrc( ImageUtils.loadImage(getTerrain().getImagePath()));
        setCircleDetector(new CircleDetector(configPanel));
        setCircles(circleDetector.detect(getImageSrc()));
        loadBall();
        loadTeams();
        
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
            ball.paint(imageSrc);   
        }
    }

    public void paintPlayers(){
        Player[] players = getTerrain().getPlayers();
        if (players.length <= 0) {
            return;
        }
        for (Player player : players) {
            player.paint(imageSrc);
        }
    }
    public void paintCircles(){
        paintPlayers();
        paintBall();
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
                // Get the color at the center of the circle
                double[] color = getImageSrc().get(centerY, centerX);
                Scalar colorScalar = new Scalar(color);
                if (color != null) {
                    System.out.println("Circle " + i + " color: B=" + color[0] + ", G=" + color[1] + ", R=" + color[2]);
                }
                Player newPlayer = new Player(centerX, centerY, radius, colorScalar);
                players.add(newPlayer);
            }
        }
        getTerrain().setPlayers(players.toArray(new Player[0]));
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
                    System.out.println("Ballon");
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

        Player[] players = getTerrain().getPlayers();
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
        for (Player player : players) {
            player.setBorderColor(new Scalar(128, 0, 128)); // Violet color
        }
    }

    public List<Player> getOffsidePlayers() {
        Player playerWithBall = getPlayerWithBall();
        if (playerWithBall == null) {
            return new ArrayList<>();
        }

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
        Scalar team1Color = players.get(0).getColor();
        Scalar team2Color = null;

        for (Player player : players) {
            if (player.getColor().equals(team1Color)) {
                team1Players.add(player);
            } else {
                if (team2Color == null) {
                    team2Color = player.getColor();
                }
                team2Players.add(player);
            }
        }

        Team team1 = new Team("Team 1", team1Color, team1Players.toArray(new Player[0]));
        Team team2 = new Team("Team 2", team2Color, team2Players.toArray(new Player[0]));
        getTerrain().setTeams(new Team[]{team1, team2});
    }
}
