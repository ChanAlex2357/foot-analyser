import org.opencv.core.Mat;
import org.opencv.core.Core;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SwingUtilities.invokeLater(() -> {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Circle Detection Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            JLabel imageLabel = new JLabel();
            frame.add(imageLabel, BorderLayout.CENTER);

            JButton browseButton = new JButton("Browse");
            frame.add(browseButton, BorderLayout.NORTH);

            browseButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    List<Mat> detectedCircles = CircleDetector.detectCircles(selectedFile.getAbsolutePath());
                    if (!detectedCircles.isEmpty()) {
                        ImageIcon detectedImageIcon = new ImageIcon(Mat2BufferedImage(detectedCircles.get(0)));
                        imageLabel.setIcon(detectedImageIcon);
                    }
                }
            });
                frame.setVisible(true);
            });
        });
    }

    private static Image Mat2BufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] b = new byte[bufferSize];
        mat.get(0, 0, b);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}
