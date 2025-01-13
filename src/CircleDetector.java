import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CircleDetector {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static List<Mat> detectCircles(String imagePath) {
        Mat src = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);

        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 1, 30); // change the last two parameters
                                      // (min_radius & max_radius) to detect larger circles

        List<Mat> circleList = new ArrayList<>();
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(0,255,0), 3, 8, 0 );
            circleList.add(src);
        }
        return circleList;
    }
}
