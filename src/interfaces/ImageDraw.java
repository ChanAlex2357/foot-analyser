package interfaces;

import org.opencv.core.Mat;

import foot.cv.paint.Paint;

public interface ImageDraw {
    public void draw(Mat src , Paint painter);
    public default void draw(Mat src){
        Paint painter = new Paint();
        draw(src, painter);
    }
}
