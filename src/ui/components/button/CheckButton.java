package ui.components.button;

import java.awt.event.ActionEvent;

import ui.frame.FootAnalyserFrame;
import ui.frame.ImageFrame;

public class CheckButton extends ActionButton{

    FootAnalyserFrame footAnalyserFrames;
    public CheckButton(FootAnalyserFrame footAnalyserFrame){
        super("Check . . .");
        setVisible(false);
        setFootAnalyserFrames(footAnalyserFrame);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(true);
        ImageFrame imageFrame = new ImageFrame(getFootAnalyserFrames().getTerrain().getImage());
        imageFrame.setVisible(true);
    }
    public FootAnalyserFrame getFootAnalyserFrames() {
        return footAnalyserFrames;
    }
    public void setFootAnalyserFrames(FootAnalyserFrame footAnalyserFrames) {
        this.footAnalyserFrames = footAnalyserFrames;
    }
    
}
