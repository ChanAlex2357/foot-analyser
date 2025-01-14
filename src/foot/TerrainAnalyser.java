package foot;

import org.opencv.core.Mat;

import foot.cv.detector.CircleDetector;
import foot.cv.util.ImageUtils;

public class TerrainAnalyser {
    CircleDetector circleDetector;
    Mat imageSrc;
    Terrain terrain;
    Mat circles;
    public TerrainAnalyser(Terrain terrain){
        setTerrain(terrain);
        setImageSrc( ImageUtils.loadImage(getTerrain().getImagePath()));
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
}
