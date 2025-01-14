package foot.cv.detector;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ShapeDetector extends Detector{
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public Mat detect(String imagePath) {
        Mat src = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);

        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 50, 150);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            double perimeter = Imgproc.arcLength(contour2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(contour2f, approx, 0.04 * perimeter, true);

            int vertices = (int) approx.total();
            if (vertices == 3) {
                // Triangle
                Imgproc.drawContours(src, List.of(new MatOfPoint(approx.toArray())), -1, new Scalar(0, 255, 0), 2);
            } else if (vertices == 4) {
                // Rectangle or Square
                Rect rect = Imgproc.boundingRect(contour);
                double aspectRatio = Math.abs(1 - (double) rect.width / rect.height);
                if (aspectRatio <= 0.02) {
                    // Square
                    Imgproc.drawContours(src, List.of(new MatOfPoint(approx.toArray())), -1, new Scalar(255, 0, 0), 2);
                } else {
                    // Rectangle
                    Imgproc.drawContours(src, List.of(new MatOfPoint(approx.toArray())), -1, new Scalar(0, 0, 255), 2);
                }
            } else {
                // Circle
                double area = Imgproc.contourArea(contour);
                Rect rect = Imgproc.boundingRect(contour);
                int radius = rect.width / 2;
                if (Math.abs(1 - ((double) rect.width / rect.height)) <= 0.2 && Math.abs(1 - (area / (Math.PI * Math.pow(radius, 2)))) <= 0.2) {
                    Imgproc.drawContours(src, List.of(new MatOfPoint(approx.toArray())), -1, new Scalar(0, 255, 255), 2);
                }
            }
        }
        return src;
    }
}
