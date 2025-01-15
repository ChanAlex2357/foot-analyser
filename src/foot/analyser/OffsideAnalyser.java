package foot.analyser;

import foot.entity.Player;
import foot.entity.Team;

public class OffsideAnalyser {

    public void analyseOffside(Team assaillantTeam,Team defenderTeam,Player mainer){
        Player defender = defenderTeam.getDefender();
        for (Player player : assaillantTeam.getPlayers()) {
            if (defenderTeam.getAxis() == Team.X_AXIS) {
                analyseOffsideOnXAxis(player, defender, mainer);
            }
            else if (defenderTeam.getAxis() == Team.Y_AXIS){
                analyseOffsideOnYAxis(player, defender, mainer);
            }
        }
    }
    public void analyseOffsideOnXAxis(Player assaillant , Player defender , Player mainer){
        int x_defender = defender.getX();
        int x_possession = mainer.getX();
        int x_attack = assaillant.getX();
        checkOffside(assaillant, x_attack, x_defender, x_possession);
        // if (x_attack < x_possession && x_possession < x_defender) {
        //     return;
        // }
        // else if (x_possession > x_defender && x_attack > x_possession) {
        //     return;
        // }

        // if ((x_possession < x_defender) && ( x_attack > x_defender)) {
        //     assaillant.setOffsideState(OffsideState.Offside());
        // }
        // else if ((x_possession < x_defender) && (x_attack < x_defender)) {
        //     assaillant.setOffsideState(OffsideState.InGame());
        // }
        // else if ((x_possession > x_defender) && ( x_attack < x_defender)) {
        //     assaillant.setOffsideState(OffsideState.Offside());
        // }
        // else if ((x_possession > x_defender) && (x_attack > x_defender)) {
        //     assaillant.setOffsideState(OffsideState.InGame());
        // }
    }

    public void analyseOffsideOnYAxis(Player assaillant , Player defender , Player mainer){
        int y_defender = defender.getY();
        int y_possession = mainer.getY();
        int y_attack = assaillant.getY();
        checkOffside(assaillant, y_attack, y_defender, y_possession);
    }

    public void checkOffside(Player assaillant ,int coord_attack , int coord_defender , int coord_possession){
        if (coord_attack < coord_possession && coord_possession < coord_defender) {
            return;
        }
        else if (coord_possession > coord_defender && coord_attack > coord_possession) {
            return;
        }

        if ((coord_possession < coord_defender) && ( coord_attack > coord_defender)) {
            assaillant.setOffsideState(OffsideState.Offside());
        }
        else if ((coord_possession < coord_defender) && (coord_attack < coord_defender)) {
            assaillant.setOffsideState(OffsideState.InGame());
        }
        else if ((coord_possession > coord_defender) && ( coord_attack < coord_defender)) {
            assaillant.setOffsideState(OffsideState.Offside());
        }
        else if ((coord_possession > coord_defender) && (coord_attack > coord_defender)) {
            assaillant.setOffsideState(OffsideState.InGame());
        }
    }
    
}
