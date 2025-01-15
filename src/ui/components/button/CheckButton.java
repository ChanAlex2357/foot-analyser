package ui.components.button;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.opencv.core.Mat;

import ui.frame.FootAnalyserFrame;
import foot.analyser.TerrainAnalyser;
import foot.entity.Terrain;
import foot.utils.MatUtils;
import ui.components.panel.CircleDetectionConfigPanel;

public class CheckButton extends ActionButton {

    private FootAnalyserFrame footAnalyserFrame;

    public CheckButton(FootAnalyserFrame footAnalyserFrame) {
        super("Check . . .");
        setVisible(false);
        setFootAnalyserFrame(footAnalyserFrame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Terrain terrain = footAnalyserFrame.getTerrain();
        if (terrain != null) {
            
            CircleDetectionConfigPanel configPanel = footAnalyserFrame.getConfigPanel();
            TerrainAnalyser terrainAnalyser = new TerrainAnalyser(terrain, configPanel);    
            try {
                terrainAnalyser.build();
                terrainAnalyser.analyse();
                
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            terrainAnalyser.paint();
            Mat src = terrainAnalyser.getImageSrc();
            // ImageFrame imageFrame = new ImageFrame(MatUtils.Mat2BufferedImage(src));
            // imageFrame.setVisible(true);
            footAnalyserFrame.updateImage( MatUtils.Mat2BufferedImage(src));
            footAnalyserFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    public FootAnalyserFrame getFootAnalyserFrame() {
        return footAnalyserFrame;
    }

    public void setFootAnalyserFrame(FootAnalyserFrame footAnalyserFrame) {
        this.footAnalyserFrame = footAnalyserFrame;
    }
}
