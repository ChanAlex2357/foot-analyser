import org.opencv.core.Core;

import ui.frame.FootAnalyserFrame;

public class App {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        FootAnalyserFrame frame = new FootAnalyserFrame();
        frame.setVisible(true);
    }
}
