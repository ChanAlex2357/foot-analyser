package foot.entity;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

import foot.cv.Edge;
import foot.utils.MathUtils;
public class Player  extends CircleEntitty{
    Team team;
    public Player(int x , int y , int radius , Scalar color,Team team) {
        super(x, y, radius,color);
        setBorderColor(new Scalar(new  double[]{0,0,0}));
        setTeam(team);
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
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
    
}
