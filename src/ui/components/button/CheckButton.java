package ui.components.button;

import java.awt.event.ActionEvent;
import java.util.List;

import org.opencv.core.Mat;

import ui.frame.FootAnalyserFrame;
import ui.frame.ImageFrame;
import foot.analyser.TerrainAnalyser;
import foot.entity.Ball;
import foot.entity.Player;
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
            terrainAnalyser.paintCircles();

           List<Player> players = terrainAnalyser.loadPlayers();
           Ball ball = terrainAnalyser.loadBall();
            System.out.println();


            Mat src = terrainAnalyser.getImageSrc();
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
