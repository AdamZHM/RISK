package edu.duke.ece651.risc.shared;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Territory;

/**
 * This is the class for detecting end of the game 
 */
public class EndGameDetection{
    /**
     * This is the constructor for EndGameDetection class
     *
     */
    public EndGameDetection() {}

    /**
     * This method returns the active player on the current GameMap
     *
     * @param gameMap is the GameMap return by the server
     */
    public Set<String> getActivePlayer(GameMap gameMap){
        Map<String, Territory> my_territories = gameMap.getMyTerritories();
        Set<String> active_player = new HashSet<>();
        for (String territory_name : gameMap.getTerritoryNames()) {
            active_player.add(my_territories.get(territory_name).getOwner());
        }

        return active_player;
    }

    /**
     * Detect whether the game has finished
     *
     * @return true if the game ends, otherwise false
     */
    public boolean isEndGame(Set<String> active_player) {
        return active_player.size() == 1;
    }

    /**
     * Get the name of the owner of the action
     *
     * @return true if the player has lost(not in active player); otherwise, false
     */
    public boolean isLost(Set<String> active_player, String Color) {
        return !active_player.contains(Color);
    }
}
