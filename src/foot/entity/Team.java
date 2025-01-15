package foot.entity;

import java.util.List;

import org.opencv.core.Scalar;

import foot.comparator.SortByDistanceOnEdge;
import foot.cv.Edge;
import ui.color.ColorPalette;

public class Team {
    public static final String PHASE_ATTACK = "Attack";
    public static final String PHASE_DEFENSE = "Defense";
    public static final String PHASE_UNDEFINED = "Undefined";
    public static final char Y_AXIS = 'Y';
    public static final char X_AXIS = 'X';

    char axis = Y_AXIS;
    Scalar color;
    ColorPalette colorPalette;
    List<Player> players;
    Player goal;
    private String name;
    Edge teamEdge;
    private String phase = PHASE_UNDEFINED;

    public Team(Scalar color) {
        this(color, null);
    }

    public Team(Scalar color, List<Player> players) {
        this.colorPalette = new ColorPalette();
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
        this.colorPalette.addColor(color);
    }

    public ColorPalette getColorPalette() {
        return colorPalette;
    }

    public void setColorPalette(ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        getPlayers().add(player);
        player.setTeam(this);
        player.setColorPalette(colorPalette);
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
        this.goal.setBorderColor(new Scalar(100, 100, 100));
    }

    public Edge getTeamEdge() {
        return teamEdge;
    }

    public void setTeamEdge(Edge edge) {
        this.teamEdge = edge;
    }

    public static String getPHASE_ATTACK() {
        return PHASE_ATTACK;
    }

    public static String getPHASE_DEFENSE() {
        return PHASE_DEFENSE;
    }

    public static String getPHASE_UNDEFINED() {
        return PHASE_UNDEFINED;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void setToAttack() {
        setPhase(getPHASE_ATTACK());
    }

    public void setToDefense() {
        setPhase(getPHASE_DEFENSE());
    }

    public void sortPlayersOnTeamEdge() {
        getPlayers().sort(new SortByDistanceOnEdge(getTeamEdge()));
    }

    public Player getDefender() {
        sortPlayersOnTeamEdge();
        for (Player player : players) {
            if (player.equals(getGoal())) {
                continue;
            }
            return player;
        }
        return null;
    }

    public static String getPhaseAttack() {
        return PHASE_ATTACK;
    }

    public static String getPhaseDefense() {
        return PHASE_DEFENSE;
    }

    public static String getPhaseUndefined() {
        return PHASE_UNDEFINED;
    }

    public static char getyAxis() {
        return Y_AXIS;
    }

    public static char getxAxis() {
        return X_AXIS;
    }

    public char getAxis() {
        return axis;
    }

    public void setAxis(char axis) {
        this.axis = axis;
    }
}