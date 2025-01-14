package foot.cv.detector;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import ui.components.panel.CircleDetectionConfigPanel;

import java.util.ArrayList;
import java.util.List;

public class CircleDetector extends Detector{
    int min_radius;
    int max_radius;
    static {
        System.loadLibrary(
            Core.NATIVE_LIBRARY_NAME
        );
    }
    public CircleDetector(CircleDetectionConfigPanel configPanel) {
        setMax_radius(configPanel.getMaxRadius());
        setMin_radius(configPanel.getMinRadius());
    }

    public Mat detect(String imagePath) {
        Mat src = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, getMin_radius(), getMax_radius()); // change the last two parameters
        // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(0,255,0), 3, 8, 0 );
            getCircleList().add(src);
        }
        return src;
    }

    List<Mat> circleList = new ArrayList<>();

    public List<Mat> getCircleList() {
        return circleList;
    }

    public void setCircleList(List<Mat> circleList) {
        this.circleList = circleList;
    }

    public int getMin_radius() {
        return min_radius;
    }

    public void setMin_radius(int min_radius) {
        this.min_radius = min_radius;
    }

    public int getMax_radius() {
        return max_radius;
    }

    public void setMax_radius(int max_radius) {
        this.max_radius = max_radius;
    }
}
