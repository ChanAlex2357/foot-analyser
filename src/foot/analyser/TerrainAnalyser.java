package foot.analyser;

import java.util.List;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import foot.cv.RectCv;
import foot.cv.detector.CircleDetector;
import foot.cv.detector.RectangleDetector;
import foot.cv.util.ImageUtils;
import foot.entity.Ball;
import foot.entity.Player;
import foot.entity.Team;
import foot.entity.Terrain;
import ui.color.BluePalette;
import ui.color.RedPalette;
import ui.component.panel.CircleDetectionConfigPanel;
import ui.constant.ScalarConstants;

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
    }
    
    public void build() throws Exception{
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
    public void loadEntities() throws Exception{
        loadTerrain();
        loadBall();
        loadTeams();
        loadEdges();
    }
    public void loadEdges(){
        getTerrain().dispatchEdges();
    }
    public void loadTerrain() {
        if (rectangles.length <= 2) {
            return;   
        }
        // int max_size = 0;
        // RectCv max = null;
        // for (RectCv rectCv : rectangles) {
            // if (rectCv.area() > max_size) {
                // max = rectCv;
            // }
        // }
        // this.getTerrain().setRectangle(max);
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
        Team[ ] teams= getTerrain().getTeams();
        if (teams == null) {return;}
        for (Team team : teams) {
            paintTeam(team);
            try {
                team.getTeamEdge().draw(imageSrc);
            } catch (Exception e) {}
        }
    }
    public void paintTeam(Team team){
        List<Player> players = team.getPlayers();
        if (players == null || players.size() <= 0) {
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
    
    public void paintOutlines(){
        // Draw circles on the original image
        // if (circles.cols() > 0) {
        //     for (int x = 0; x < circles.cols(); x++) {
        //     double[] circle = circles.get(0, x);
        //     if (circle == null) break;
        //     Point center = new Point(Math.round(circle[0]), Math.round(circle[1]));
        //     int radius = (int) Math.round(circle[2]);
        //     // Draw the circle center
        //     Imgproc.circle(getImageSrc(), center, 3, new Scalar(255, 165, 0), -1);
        //     // Draw the circle outline
        //     Imgproc.circle(getImageSrc(), center, radius, new Scalar(255, 165, 0), 3);
        //     }
        // }
    }
    public void paintTerrain(){
        this.getTerrain().draw(imageSrc);
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

    public Ball loadBall() throws Exception {
        Mat circles = getCircles();
        Ball detectedBall = null;
        double minRadius = Double.MAX_VALUE;

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
                if (Ball.isBallColor(colorScalar)) {
                    if (radius < minRadius) {
                        minRadius = radius;
                        detectedBall = new Ball(centerX, centerY, radius, colorScalar);
                    }
                }
            }
        }

        if (detectedBall != null) {
            getTerrain().setBall(detectedBall);
            return getTerrain().getBall();
        }

        throw new Exception("Aucun ballon noir n'a ete trouver sur le terrain");
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

    public Team getAttackingTeam() {
        for (Team team : getTerrain().getTeams()) {
            if (team.getPhase().equals(Team.getPHASE_ATTACK())) {
                return team;
            }
        }
        return null;
    }
    public Team getDefendingTeam() {
        Team[] teams = getTerrain().getTeams();
        if (teams == null) {
            return null;
        }
        for (Team team : teams) {
            if (team.getPhase().equals(Team.getPHASE_DEFENSE())) {
                return team;
            }
        }
        return null;
    }


    public void analyseOffside() throws Exception {
        Player playerWithBall = getPlayerWithBall();
        if (playerWithBall == null) {return;}

        playerWithBall.setBorderColor(new Scalar(10, 0, 128));
        Team attackingTeam = getAttackingTeam();
        Team defendingTeam = getDefendingTeam();

        if (attackingTeam == null || defendingTeam == null) {
            throw new Exception("Phase de jeu indeterminer , pas d'attaquant ou defense");
        }

        OffsideAnalyser offsideAnalyser = new OffsideAnalyser();
        offsideAnalyser.analyseOffside(attackingTeam, defendingTeam, playerWithBall);
    }

    // public void loadTeams() throws Exception {
    //     List<Player> players = loadPlayers();
    //     if (players.isEmpty()) {
    //         throw new Exception("Aucun joueur detecter dans l'image");
    //     }

    //     // Assuming players are divided into two teams based on their colors
    //     List<Player> team1Players = new ArrayList<>();
    //     List<Player> team2Players = new ArrayList<>();
    //     Team team1 = new Team("Team 1", players.get(0).getColor(), team1Players);
    //     Team team2 = new Team("Team 2", null, team2Players);

    //     for (Player player : players) {
    //         if (player.getColor().equals(team1.getColor())) {
    //             team1.addPlayer(player);
    //         } else {
    //             if (  team2.getColor() == null) {
    //                 team2.setColor(player.getColor());
    //                 team2.addPlayer(player);
    //             }
    //             else if (player.getColor().equals(team2.getColor())) {
    //                 team2.addPlayer(player);
    //             }
    //             else {
    //                 System.out.println("NO TEAM ASIGNED : "+player);
    //                 player.setBorderColor(ScalarConstants.VIOLET());
    //             }
    //         }
    //     }
    //     getTerrain().setTeams(new Team[]{team1, team2});
    //     if (team1.getColor() == null || team2.getColor() == null) {
    //         throw new Exception("Impossible de distinguer deux quipes , veuillez ajuster les colleurs de votre Mamaan");
    //     }
    // }
    public void loadTeams() throws Exception {
        List<Player> players = loadPlayers();
        if (players.isEmpty()) {
            throw new Exception("Aucun joueur detecter dans l'image");
        }

        // Define color palettes for teams
        RedPalette redPalette = new RedPalette();
        BluePalette bluePalette = new BluePalette();

        // Create teams with predefined color palettes
        Team team1 = new Team("Team 1", ScalarConstants.RED(), new ArrayList<>());
        team1.setColorPalette(redPalette);
        Team team2 = new Team("Team 2", ScalarConstants.BLUE(), new ArrayList<>());
        team2.setColorPalette(bluePalette);

        for (Player player : players) {
            if (team1.getColorPalette().contains(player.getColor())) {
                team1.addPlayer(player);
            } else if (team2.getColorPalette().contains(player.getColor())) {
                team2.addPlayer(player);
            } else {
                System.out.println("NO TEAM ASSIGNED: " + player);
                player.setBorderColor(ScalarConstants.VIOLET());
            }
        }

        getTerrain().setTeams(new Team[]{team1, team2});
        if (team1.getPlayers().isEmpty() || team2.getPlayers().isEmpty()) {
            throw new Exception("Impossible de distinguer deux équipes, veuillez ajuster les couleurs de votre image.");
        }
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
        paintOutlines();
        paintCircles();
        paintRectangles();
        paintOffSideLine();
    }

    private void paintOffSideLine() {
        Team defenderTeam = getDefendingTeam();
        if (defenderTeam != null) {
            Player defender  = defenderTeam.getDefender();
            foot.cv.paint.Paint paint = new foot.cv.paint.Paint();
            paint.paintRepere(imageSrc, defender, terrain);
        }
    }

    /**
     * Sets the game phase based on the player who currently has the ball.
     * If no player has the ball, an exception is thrown.
     * The team color of the player with the ball determines the attacking team.
     * The team with the matching color is set to attack, while the other team is set to defense.
     *
     * @throws Exception if no player has the ball, indicating no game phase.
     */
    public void setGamePhase() throws Exception{
        Player playerWithBall = getPlayerWithBall();
        if (playerWithBall == null) {
            throw new Exception("PAS DE JOUEUR QUI POSSEDE LE BALLON DE JEU");
        }
        // Assuming the team color of the player with the ball determines the attacking team
        Scalar attackingTeamColor = playerWithBall.getColor();
        Team[] teams = getTerrain().getTeams();
        for (Team team : teams) {
            if (team.getColor() != null && team.getColor().equals(attackingTeamColor)) {
                team.setToAttack();
            }
            else {
                team.setToDefense();
            }
        }
    }

    public void analyse() throws Exception{
        setGamePhase();
        analyseOffside();
    }
}
