package ui.frame;
import javax.swing.*;
import java.awt.*;

public class ImageFrame extends JFrame {
    private Image image;

    public ImageFrame(Image image) {
        this.image = image;
        setTitle("Terrain Image");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel(new ImageIcon(image));
        add(imageLabel, BorderLayout.CENTER);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
