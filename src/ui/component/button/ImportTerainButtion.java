package ui.component.button;

import javax.swing.JFrame;
import javax.swing.JLabel;

import foot.entity.Terrain;
import ui.foot.TerrainUI;
import ui.frame.FootAnalyserFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class ImportTerainButtion extends ImportImageButton {
    TerrainUI terrainUI;
    public ImportTerainButtion(FootAnalyserFrame parent,JLabel labelView){
        super(parent,labelView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (this.getImportedImage() == null) {return;}
        FootAnalyserFrame footAnalyserFrame = (FootAnalyserFrame)getParentComponent();
        Dimension panelSize = getShowerLabel().getSize();
        footAnalyserFrame.setTerrain(new Terrain(getImportedImage(), panelSize));
        footAnalyserFrame.getTerrain().setImagePath(getFilePath());
        footAnalyserFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        footAnalyserFrame.getSecondButton().setVisible(true);
        footAnalyserFrame.updateTerrainDetails();
        footAnalyserFrame.paint(getGraphics());
    }
}
