package ui.frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import foot.entity.Terrain;
import ui.components.button.CheckButton;
import ui.components.button.ImportImageButton;
import ui.components.button.ImportTerainButtion;
import ui.components.panel.CircleDetectionConfigPanel;
import ui.components.panel.TerrainPanel;
import ui.foot.TerrainUI;

public class FootAnalyserFrame extends JFrame {
    private JLabel imageLabel;
    private CircleDetectionConfigPanel configPanel;
    private JPanel buttonPanel;
    private JPanel sidePanel;
    private ImportTerainButtion importImageButtion;
    private CheckButton secondButton;
    private Terrain terrain;
    private TerrainPanel terrainPanel;
    private JSlider zoomSlider;
    private double zoomFactor = 1.0;

    public FootAnalyserFrame() {
        setTitle("Foot Analyser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // Image panel
        imageLabel = new JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                // if (getIcon() != null) {
                //     Image image = ((ImageIcon) getIcon()).getImage();
                //     int width = (int) (image.getWidth(null) * zoomFactor);
                //     int height = (int) (image.getHeight(null) * zoomFactor);
                //     g.drawImage(image, 0, 0, width, height, null);
                // }
            }
        };
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
        add(imageScrollPane, BorderLayout.CENTER);

        sidePanel = new JPanel();
        sidePanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        // Configuration panel
        configPanel = new CircleDetectionConfigPanel();
        sidePanel.add(configPanel);

        // Button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        setImportImageButtion(new ImportTerainButtion(this, imageLabel));
        buttonPanel.add(importImageButtion);
        CheckButton checkButton = new CheckButton(this);
        setSecondButton(checkButton);
        buttonPanel.add(checkButton);

        // Zoom slider
        zoomSlider = new JSlider(50, 200, 100);
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                zoomFactor = zoomSlider.getValue() / 100.0;
                imageLabel.repaint();
            }
        });
        buttonPanel.add(zoomSlider);

        setTerrainPanel(new TerrainPanel());
        add(buttonPanel, BorderLayout.SOUTH);
        add(sidePanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setImage(ImageIcon imageIcon) {
        imageLabel.setIcon(imageIcon);
        pack();
    }

    public ImportImageButton getImportImageButtion() {
        return importImageButtion;
    }

    public void setImportImageButtion(ImportTerainButtion importImageButtion) {
        this.importImageButtion = importImageButtion;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(JLabel imageLabel) {
        this.imageLabel = imageLabel;
        add(getImageLabel(), BorderLayout.CENTER);
    }

    public JButton getSecondButton() {
        return secondButton;
    }

    public void setSecondButton(CheckButton secondButton) {
        this.secondButton = secondButton;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public TerrainPanel getTerrainPanel() {
        return terrainPanel;
    }

    public void setTerrainPanel(TerrainPanel terrainPanel) {
        this.terrainPanel = terrainPanel;
        sidePanel.add(terrainPanel);
    }

    public void updateTerrainDetails() {
        getTerrainPanel().updateDetails(getTerrain());
    }

    public CircleDetectionConfigPanel getConfigPanel() {
        return configPanel;
    }

    public void setConfigPanel(CircleDetectionConfigPanel configPanel) {
        this.configPanel = configPanel;
        this.add(configPanel, BorderLayout.EAST);
    }

    public void updateImage(Image image) {
        getImageLabel().setIcon(new ImageIcon(image));
        pack();
    }
}
