package ui.components.panel;

import javax.swing.*;
import java.awt.*;

public class CircleDetectionConfigPanel extends JPanel {
    private JSpinner minRadiusSpinner;
    private JSpinner maxRadiusSpinner;

    public CircleDetectionConfigPanel() {
        setLayout(new FlowLayout());

        add(new JLabel("Min Radius:"));
        minRadiusSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        add(minRadiusSpinner);

        add(new JLabel("Max Radius:"));
        maxRadiusSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 100, 1));
        add(maxRadiusSpinner);
    }

    public int getMinRadius() {
        return (int) minRadiusSpinner.getValue();
    }

    public int getMaxRadius() {
        return (int) maxRadiusSpinner.getValue();
    }
}
