package foot.entity;

import java.awt.Font;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import foot.analyser.OffsideState;
import foot.cv.Edge;
import foot.cv.paint.Paint;
import foot.utils.MathUtils;
import ui.color.ColorPalette;
import ui.constant.ScalarConstants;

public class Player  extends CircleEntitty{
    Team team;
    OffsideState offsideState = OffsideState.OutGame();
    ColorPalette colorPalette;

    public Player(int x , int y , int radius , Scalar color,Team team) {
        super(x, y, radius,color);
        setBorderColor(new Scalar(new  double[]{0,0,0}));
        setTeam(team);
        this.colorPalette = new ColorPalette();
        this.colorPalette.addColor(color);
    }

    @Override
    public Scalar getBorderColor() {
        if (offsideState != null && offsideState.getOffsideColor() != null) {
            return offsideState.getOffsideColor();
        }
        return super.getBorderColor();
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }

    public ColorPalette getColorPalette() {
        return colorPalette;
    }

    public void setColorPalette(ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
    }

    public double calculerDistanceFromEdge(Edge edge,boolean isVertical) {
        Point edgePoint = edge.getStartPoint();
        double distance = 0 ;
        if (isVertical) {
            double y_ref = edgePoint.y;
            distance = MathUtils.calcDistance(y_ref ,this.getY());
        }
        else {
            double x_ref = edgePoint.x;
            distance = MathUtils.calcDistance(x_ref,this.getX());
        }
        return distance;
    }

    public void setGoal(){
        getTeam().setGoal(this);
    }

    public OffsideState getOffsideState() {
        return offsideState;
    }

    public void setOffsideState(OffsideState offsideState) {
        this.offsideState = offsideState;
    }
    
    @Override
    public void draw(Mat src, Paint painter) {
        super.draw(src, painter);
        String text = getOffsideState().getState();
        if (text == null || text.equals("")) {
            text = getTeam().getName();
        }
        Imgproc.putText(src,text, getCoord(), Font.PLAIN,0.5,ScalarConstants.WHITE(),1);
    }
}
