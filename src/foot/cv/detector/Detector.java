package foot.cv.detector;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public abstract class Detector {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public abstract Mat detect(String filePath);
}
