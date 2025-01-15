package ui.foot;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import foot.entity.Terrain;
import interfaces.DrawableComponent;

public class TerrainUI implements DrawableComponent{
    Terrain terrain;
    Image image;
    Point coordonnee;
    Rectangle drawAspect;
    Rectangle containerAspect;
    public TerrainUI(Terrain terrain,int containerWidth, int containerHeight){
        setTerrain(terrain);
        setImage(getTerrain().getImage());
        setContainerAspect(new Rectangle(containerWidth, containerHeight));

        setDrawAspect(buildDrawAspect());
        setCoordonnee(buildCoordonnee());
    }
    @Override
    public void draw(Graphics g) {
        // g.drawImage(this.getImage(), (int)getCoordonnee().getX(), (int)getCoordonnee().getY(), null);
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
    public Terrain getTerrain() {
        return terrain;
    }

    public void setDrawAspect(Rectangle drawAspect) {
        this.drawAspect = drawAspect;
    }
    public Rectangle getDrawAspect() {
        if (drawAspect == null) {
            setDrawAspect(buildDrawAspect());
        }
        return drawAspect;
    }

    private Rectangle buildDrawAspect(){
        int imgWidth = getImage().getWidth(null);
        int imgHeight = getImage().getHeight(null);
        double imgAspect = (double) imgWidth / imgHeight;
        double containerAspect = (double) getContainerAspect().getWidth() / getContainerAspect().getHeight();

        int drawWidth, drawHeight;
        if (imgAspect > containerAspect) {
            drawWidth = (int)getContainerAspect().getWidth();
            drawHeight = (int) (getContainerAspect().getWidth() / imgAspect);
        } else {
            drawHeight = (int)getContainerAspect().getHeight();
            drawWidth = (int) (getContainerAspect().getHeight() * imgAspect);
        }
        return new Rectangle(drawWidth,drawHeight);
    }

    private Point buildCoordonnee(){
        int x = ((int)getContainerAspect().getWidth() - (int)getDrawAspect().getWidth())/2;

        int y = ((int)getContainerAspect().getHeight() - (int)getDrawAspect().getWidth())/2; 

        return new Point(x, y);
    }

    public Point getCoordonnee() {
        return coordonnee;
    }
    public void setCoordonnee(Point coordonnee) {
        this.coordonnee = coordonnee;
    }
    public Rectangle getContainerAspect() {
        return containerAspect;
    }
    public void setContainerAspect(Rectangle containerAspect) {
        this.containerAspect = containerAspect;
    }
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
}
