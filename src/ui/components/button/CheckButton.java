package ui.components.button;

import java.awt.event.ActionEvent;
import org.opencv.core.Mat;

import ui.frame.FootAnalyserFrame;
import ui.frame.ImageFrame;
import foot.Terrain;
import foot.cv.detector.CircleDetector;
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
            Mat src = new CircleDetector(configPanel).detect(terrain.getImagePath());
            ImageFrame imageFrame = new ImageFrame(MatUtils.Mat2BufferedImage(src));
            imageFrame.setVisible(true);
        }
    }

    public FootAnalyserFrame getFootAnalyserFrame() {
        return footAnalyserFrame;
    }

    public void setFootAnalyserFrame(FootAnalyserFrame footAnalyserFrame) {
        this.footAnalyserFrame = footAnalyserFrame;
    }
}
