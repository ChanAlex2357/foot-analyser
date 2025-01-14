package foot.cv.util;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageUtils {
    public static Mat loadImage(String filePath){
        return Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
    }
}
