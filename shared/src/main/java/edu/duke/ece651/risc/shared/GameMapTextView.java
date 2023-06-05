package edu.duke.ece651.risc.shared;

import org.apache.commons.lang3.StringUtils;

import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;

import java.util.List;
import java.util.Map;

/**
 * This is the GameMapTextView class
 */
public class GameMapTextView implements GameMapView{

  /**
   * This function display one player's territories
   *
   * @param toDisplay is the GameMap instance to display
   * @param player_name is the player name of the play to display view of
   * @return the specified player's territories' information in String
   */
  public String displayMyTerritory(GameMap toDisplay, String player_name) {
    StringBuilder sb = new StringBuilder();

    Map<String, Territory> my_t = toDisplay.getMyTerritories();
    for (Map.Entry<String, List<String>> entry : toDisplay.getTerritoryInfo().entrySet()) {
      if (my_t.get(entry.getKey()).getOwner().equals(player_name)) {
        Territory territory = toDisplay.getTerritoryByName(entry.getKey());
        sb.append(" " + territory.getMyUnits().get("soldier").getNum() + " soldier(s) in " + entry.getKey() + ": ");
        sb.append("(next to: " + StringUtils.join(entry.getValue(), ", ") + ")\n");
      }
    }
    return sb.toString();
  }

  /**
   * This function display all player's territories
   *
   * @param toDisplay is the GameMap instance to display
   * @return all players' territories' information in String
   */
  public String displayAllTerritory(GameMap toDisplay, int playerNum) {
    StringBuilder sb = new StringBuilder();
    Map<String, Territory> my_t = toDisplay.getMyTerritories();
    Map<Integer, String> playerId2Color = PlayerId2ColorHelper.playerId2Color;
    for (int i = 0; i < playerNum; i++){
      sb.append(playerId2Color.get(i) + " player:\n----------\n");
      for (Map.Entry<String, List<String>> entry : toDisplay.getTerritoryInfo().entrySet()) {
        if (my_t.get(entry.getKey()).getOwner().equals(playerId2Color.get(i))) {
          Territory territory = toDisplay.getTerritoryByName(entry.getKey());
          sb.append(" " + territory.getMyUnits().get("soldier").getNum() + " soldier(s) in " + entry.getKey() + ": ");
          sb.append("(next to: " + StringUtils.join(entry.getValue(), ", ") + ")\n");
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
