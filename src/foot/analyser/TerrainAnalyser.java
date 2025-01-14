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

    public void paintCircles(){
        Paint paint = new foot.cv.paint.Paint();
        paint.paintCircles(imageSrc, circles);
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
                    return new Ball(centerX, centerY, radius, colorScalar);
                }
            }
        }
        return null; // No black circle found
    }
}
