import org.opencv.core.Core;
import org.opencv.core.Mat;

import foot.cv.detector.ShapeDetector;
import foot.utils.MatUtils;
import ui.frame.ImageFrame;

public class ShapeMain {
     public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        ShapeDetector shapeDetector = new ShapeDetector();
        Mat shapes =  shapeDetector.detect("c:\\Users\\Chan Alex\\Documents\\Prog Foot\\balls.png");
        ImageFrame imageFrame = new ImageFrame(MatUtils.Mat2BufferedImage(shapes));
        imageFrame.setVisible(true);
    }    
}
