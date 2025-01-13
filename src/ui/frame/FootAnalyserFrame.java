package ui.frame;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import foot.Terrain;
import ui.components.button.CheckButton;
import ui.components.button.ImportImageButton;
import ui.components.button.ImportTerainButtion;
import ui.components.panel.TerrainPanel;
import ui.foot.TerrainUI;

public class FootAnalyserFrame extends JFrame{
    private ImportTerainButtion importImageButtion;
    private JLabel imageLabel;
    private CheckButton secondButton;
    private Terrain terrain;
    private TerrainPanel terrainPanel;

    private void build(){
        setTitle("FootAnalyser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
    }

    public FootAnalyserFrame(){
        build();
        // ImageLabel
        setImageLabel(new JLabel());
        // ImportButton
        setImportImageButtion(new ImportTerainButtion(this, getImageLabel()));
        // Second Button
        setSecondButton(new CheckButton(this));
        // Terrain Panel
        setTerrainPanel(new TerrainPanel());
    }

    public ImportImageButton getImportImageButtion() {
        return importImageButtion;
    }

    public void setImportImageButtion(ImportTerainButtion importImageButtion) {
        this.importImageButtion = importImageButtion;
        add(getImportImageButtion(), BorderLayout.NORTH);
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(JLabel imageLabel) {
        this.imageLabel = imageLabel;
        add(getImageLabel(),BorderLayout.CENTER);
    }

    public JButton getSecondButton() {
        return secondButton;
    }

    public void setSecondButton(CheckButton secondButton) {
        this.secondButton = secondButton;
        add(getSecondButton(),BorderLayout.SOUTH);
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
        add(getTerrainPanel(),BorderLayout.EAST);
    }

    public void updateTerrainDetails(){
        getTerrainPanel().updateDetails(getTerrain());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (this.getTerrain()!=null) {
            new TerrainUI(terrain, 0, 0).draw(g);
        }
    }
}
