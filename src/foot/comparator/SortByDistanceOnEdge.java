package foot.comparator;

import java.util.Comparator;

import foot.cv.Edge;
import foot.entity.Player;
public class SortByDistanceOnEdge implements Comparator<Player> {

    Edge edge ;
    boolean isVertical = false;
    public SortByDistanceOnEdge(Edge edge){
        setEdge(edge);
    }

    @Override
    public int compare(Player p1, Player p2) {
        double d1 = p1.calculerDistanceFromEdge(edge, isVertical);
        double d2 = p2.calculerDistanceFromEdge(edge, isVertical);

        if (d1 < d2) {
            return -1;
        }
        else if (d1 > d2) {
            return 1;
        }
        return 0;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }
    
}
