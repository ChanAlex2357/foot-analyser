package foot.cv.detector;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import foot.cv.util.ImageUtils;
import ui.components.panel.CircleDetectionConfigPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * The CircleDetector class is responsible for detecting circles in an image using the Hough Circle Transform.
 * It extends the Detector class and utilizes OpenCV for image processing.
 * 
 * <p>This class provides methods to detect circles in an image and print the color of each detected circle.
 * The detection parameters such as minimum and maximum radius can be configured through a CircleDetectionConfigPanel.</p>
 * 
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * CircleDetectionConfigPanel configPanel = new CircleDetectionConfigPanel();
 * CircleDetector detector = new CircleDetector(configPanel);
 * Mat result = detector.detect("path/to/image.jpg");
 * }
 * </pre>
 * 
 * <p>Note: Ensure that the OpenCV native library is loaded before using this class.</p>
 * 
 * @see Detector
 * @see CircleDetectionConfigPanel
 * @see ImageUtils
 * @see org.opencv.core.Mat
 * @see org.opencv.core.Point
 * @see org.opencv.core.Scalar
 * @see org.opencv.imgproc.Imgproc
 */
public class CircleDetector extends Detector {
    int min_radius;
    int max_radius;
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public CircleDetector(CircleDetectionConfigPanel configPanel) {
        setMax_radius(configPanel.getMaxRadius());
        setMin_radius(configPanel.getMinRadius());
    }

    public Mat detect(String imagePath) {
        Mat src = ImageUtils.loadImage(imagePath);
        Mat circles = detect(src);
        System.out.println("------------------------- // --------------------------------");
        for (int i = 0; i < circles.cols(); i++) {
            double[] circle = circles.get(0, i);
            Point center = new Point(Math.round(circle[0]), Math.round(circle[1]));
            int radius = (int) Math.round(circle[2]);
            Scalar color = new Scalar(src.get((int) center.y, (int) center.x));
            System.out.println("Circle at (" + center.x + ", " + center.y + ") with radius " + radius + " has color " + color);
        }
        return circles;
    }
    
    public Mat detect(Mat src) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1,
        (double) gray.rows() / 100, // reduce this value to detect circles that are closer to each other
        50.0, 20.0, getMin_radius(), getMax_radius()); // adjust the thresholds for smaller circles
        System.out.println("------------------------- // --------------------------------");
        for (int i = 0; i < circles.cols(); i++) {
            double[] circle = circles.get(0, i);
                Point center = new Point(Math.round(circle[0]), Math.round(circle[1]));
                int radius = (int) Math.round(circle[2]);
                Scalar color = new Scalar(src.get((int) center.y, (int) center.x));
                System.out.println("Circle at (" + center.x + ", " + center.y + ") with radius " + radius + " has color " + color);
            }
        return circles;
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
