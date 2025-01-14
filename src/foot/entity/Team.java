package foot.entity;

import org.opencv.core.Scalar;

public class Team {
    Scalar color;
    Player[] players;

    public Team(Scalar color){
        this(color,null);
    }

    public Team(Scalar color , Player[] players){
        setColor(color);
        setPlayers(players);
    }

    public Scalar getColor() {
        return color;
    }

    public void setColor(Scalar color) {
        this.color = color;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
    
}