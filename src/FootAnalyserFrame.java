import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FootAnalyserFrame extends JFrame {
    private JButton browseButton;
    private JLabel imageLabel;
    private JButton secondButton;
    private Terrain terrain;
    private TerrainPanel terrainPanel;

    public FootAnalyserFrame() {
        setTitle("FootAnalyser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        browseButton = new JButton("Browse");
        add(browseButton, BorderLayout.NORTH);

        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);

        secondButton = new JButton("Second Button");
        secondButton.setVisible(false);
        add(secondButton, BorderLayout.SOUTH);

        terrainPanel = new TerrainPanel();
        add(terrainPanel, BorderLayout.EAST);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(FootAnalyserFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    imageLabel.setIcon(imageIcon);
                    Dimension panelSize = imageLabel.getSize();
                    terrain = new Terrain(imageIcon, panelSize);
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                    secondButton.setVisible(true);
                    terrainPanel.updateDetails(terrain);
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (terrain != null) {
            terrain.draw(g, 0, 0);
        }
    }
}
