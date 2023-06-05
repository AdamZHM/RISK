package edu.duke.ece651.risc.shared;

import edu.duke.ece651.risc.shared.Utils.InitialUnitNumHelper;
import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;

/**
 * This is the Placement Checker class
 */
public class PlacementChecker {

  /**
   * This is the GameMap instance
   */
  private GameMap gameMap;

  /**
   * This is the constructor of the class
   * @param map
   */
  public PlacementChecker(GameMap map) {
    gameMap = map;
  }

  /**
   * This function is to update the current checked map
   * @param map
   */
  public void updateMap(GameMap map){
    this.gameMap = map;
  }

  /**
   * This checks if the placement is valid
   *
   * @param id is the id of the player
   * @return error message in String if there's any else null
   */
  public String check(int id) {
    for (String unit : InitialUnitNumHelper.initialUnitNum.keySet()) {
      int currentPlaced = 0;
      for (String t : gameMap.getTerritoryNamesInGroups().get(id)) {
        currentPlaced += gameMap.getTerritoryByName(t).getMyUnits().get(unit).getNum();
      }
      if (currentPlaced != InitialUnitNumHelper.initialUnitNum.get(unit)) {
        return "Error: Haven't placed exactly " + InitialUnitNumHelper.initialUnitNum.get(unit) + " " + unit
            + "s to place!\n";
      }
    }
    return null;

  }
}
