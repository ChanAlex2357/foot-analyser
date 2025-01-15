package foot.entity;

import java.util.List;

import org.opencv.core.Scalar;

import foot.cv.Edge;

public class Team {
    Scalar color;
    List<Player> players;
    Player goal;
    private String name;
    Edge teamEdge;

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

    public Player getGoal() {
        return goal;
    }

    public void setGoal(Player goal) {
        this.goal = goal;
        this.goal.setBorderColor( new Scalar(50,50,50));
    }

    public Edge getTeamEdge() {
        return teamEdge;
    }

    public void setTeamEdge(Edge edge) {
        this.teamEdge = edge;
    }
}