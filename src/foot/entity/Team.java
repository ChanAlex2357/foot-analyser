package foot.entity;

import java.util.List;

import org.opencv.core.Scalar;

public class Team {
    Scalar color;
    List<Player> players;
    Player goal;
    private String name;

    public Team(Scalar color){
        this(color,null);
    }

    public Team(Scalar color , List<Player> players){
        setColor(color);
        setPlayers(players);
    }

    public Team(String name, Scalar color) {
        this(name, color, null);
    }

    public Team(String name, Scalar color, List<Player> players) {
        this(color, players);
        this.name = name;
    }

    public Scalar getColor() {
        return color;
    }

    public void setColor(Scalar color) {
        this.color = color;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player){
        getPlayers().add(player);
        player.setTeam(this);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}