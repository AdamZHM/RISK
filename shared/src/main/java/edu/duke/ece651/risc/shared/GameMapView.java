package edu.duke.ece651.risc.shared;

/**
 * This is the GameMapView interface
 */
public interface GameMapView {

    /**
     * This function display one player's territories
     *
     * @param toDisplay is the GameMap instance to display
     * @param player_name is the player name of the play to display view of
     * @return the specified player's territories' information in String
     */
    public String displayMyTerritory(GameMap toDisplay, String player_name);

    /**
     * This function display all player's territories
     *
     * @param toDisplay is the GameMap instance to display
     * @return all players' territories' information in String
     */
    public String displayAllTerritory(GameMap toDisplay, int playerNum);
}
