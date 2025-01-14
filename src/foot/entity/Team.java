package foot.entity;

import org.opencv.core.Scalar;

public class Team {
    Scalar color;
    Player[] players;
    private String name;

    public Team(Scalar color){
        this(color,null);
    }

    public Team(Scalar color , Player[] players){
        setColor(color);
        setPlayers(players);
    }

    public Team(String name, Scalar color) {
        this(name, color, null);
    }

    public Team(String name, Scalar color, Player[] players) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}