package ui.components.panel;
import javax.swing.*;

import foot.entity.Terrain;

public class TerrainPanel extends JPanel {
    private JLabel sizeLabel;
    private JLabel orientationLabel;

    public TerrainPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        sizeLabel = new JLabel("Size: ");
        orientationLabel = new JLabel("Orientation: ");
        add(sizeLabel);
        add(orientationLabel);
    }

    public void updateDetails(Terrain terrain) {
        if (terrain != null) {
            sizeLabel.setText("Size: " + terrain.getSize().width + " x " + terrain.getSize().height);
            orientationLabel.setText("Orientation: " + (terrain.isVertical() ? "Vertical" : "Horizontal"));
        }
    }
}
