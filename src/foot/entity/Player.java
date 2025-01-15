package foot.entity;

import org.opencv.core.Scalar;
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
}
