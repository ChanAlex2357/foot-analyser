package pann;

import org.opencv.core.Core;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SwingUtilities.invokeLater(() -> {
            OpenCVSwingFrame frame = new OpenCVSwingFrame();
            frame.setVisible(true);
        });
    }
}