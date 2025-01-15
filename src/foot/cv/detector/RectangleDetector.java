package foot.cv.detector;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import foot.cv.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class RectangleDetector extends Detector {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public Mat detect(String imagePath) {
        Mat src = ImageUtils.loadImage(imagePath);
        return detect(src);
    }

    public Mat detect(Mat src) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 75, 200);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat rectangles = src.clone();
        for (MatOfPoint contour : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(contour2f, approx, Imgproc.arcLength(contour2f, true) * 0.02, true);
            if (approx.total() == 4 && Math.abs(Imgproc.contourArea(approx)) > 1000 && Imgproc.isContourConvex(new MatOfPoint(approx.toArray()))) {
                Point[] points = approx.toArray();
                for (int i = 0; i < 4; i++) {
                    Imgproc.line(rectangles, points[i], points[(i + 1) % 4], new Scalar(0, 255, 0), 3);
                }
            }
        }
        return rectangles;
    }
}
