package foot.cv.paint;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import foot.cv.Edge;
import foot.cv.RectCv;

public class Paint {
    public void paintCircle(Mat src , Point center , int radius , Scalar color){
        Imgproc.circle(src, center, radius, color, 3, 8, 0 );
    }
    public void paintCircles(Mat src,Mat circles){
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) Math.round(c[2]);
            new Paint().paintCircle(src, center, radius,new Scalar(0,255,0));
        }
    }
    public void paintRectangles(Mat src , RectCv[] rectCv){
        for (RectCv rectCv2 : rectCv) {
            Imgproc.drawContours(src,List.of(new MatOfPoint(rectCv2.getPoints())), -1, new Scalar(0, 0, 255),2);
        }
    }
    public void paintRectangles(Mat src , RectCv rect){
        Imgproc.drawContours(src,List.of(new MatOfPoint(rect.getPoints())), -1, new Scalar(0, 0, 255),2);
    }


    public void paintRectangles(Mat src, Mat rectangles) {
        for (int i = 0; i < rectangles.rows(); i++) {
            double[] rect = rectangles.get(i, 0);
            try {
                System.out.println(rect+" : "+rect.length+"["+rect[0]+","+rect[1]+","+rect[2]+"]");
                Point pt1 = new Point(rect[0], rect[1]);
                Point pt2 = new Point(rect[2], rect[3]);
                Imgproc.rectangle(src, pt1, pt2, new Scalar(0, 255, 0), 3);
            } catch (Exception e) {
            }
        }
    }

    public void paintEdge(Mat src,Edge edge){
        Imgproc.line(src, edge.getStartPoint(), edge.getEndPoint(), edge.getColor(),3);
    }
}
