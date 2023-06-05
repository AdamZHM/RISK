package edu.duke.ece651.risc.shared;

import java.io.Serializable;

import edu.duke.ece651.risc.shared.Utils.InitialUnitNumHelper;

/**
 * This is the Placement class
 */
public class Placement implements Serializable {

  /**
   * This is the GameMap instance
   */
  private GameMap gameMap;

  /**
   * This is the territory instance that this placement involves
   */
  private Territory territory;

  /**
   * This is the type of unit specified in this placement
   */
  private String unit;

  /**
   * This specifies the number of unit to be placed
   */
  private int unitNum;

  
  /**
   * this function get gameMap
   * @return gameMap
   */
  public GameMap getGameMap() {
    return gameMap;
  }

  /**
   * this function get territory
   * @return territory
   */
  public Territory getTerritory() {
    return territory;
  }

  /**
   * this function get unit
   * @return unit
   */
  public String getUnit() {
    return unit;
  }

  /**
   * this function get unitNum
   * @return unitNum
   */
  public int getUnitNum() {
    return unitNum;
  }

  /**
   * constructs a Placement with the string
   *
   * @param ownerColor is the owner's color
   * @param string is the territory name and unit name and unit num
   * @param gameMap is the current game map that user is playing
   */
  public Placement(String ownerColor, String string, GameMap gameMap){
    this.gameMap = gameMap;
    String[] s = string.split(",");
    int size = s.length;
    if (size != 3){
      throw new IllegalArgumentException("There should be 3 arguments!");
    }
    String territoryName = s[0];
    if (!this.gameMap.getMyTerritories().containsKey(territoryName) || !this.gameMap.getTerritoryByName(territoryName).getOwner().equals(ownerColor)){
      throw new IllegalArgumentException("That territory does not belong to you!");
    }
    this.territory = this.gameMap.getTerritoryByName(territoryName);
    if (!InitialUnitNumHelper.initialUnitNum.containsKey(s[1])){
      StringBuilder sBuilder = new StringBuilder();
      sBuilder.append("This is not a valid unit type, valid unit types are: ");
      for (String unitType : InitialUnitNumHelper.initialUnitNum.keySet()){
        sBuilder.append(unitType + " ");
      }
      throw new IllegalArgumentException(sBuilder.toString());
    }
    this.unit = s[1];
    this.territory.getMyUnits().get(unit).getNum();
    if (!s[2].matches("\\d+")){
      throw new IllegalArgumentException("The third argument should be a number!");
    }

    if (getCurrUnitPlaceNumber() + Integer.parseInt(s[2]) > InitialUnitNumHelper.initialUnitNum.get("soldier")){
      throw new IllegalArgumentException("You do not have that many units to place!");
    }

    this.unitNum = Integer.parseInt(s[2]);
  }

  /**
   * This is the helper function to get the current placed unit number
   * @return placed unit number
   */
  public int getCurrUnitPlaceNumber(){
    int currentPlaced = 0;
    for (String t : gameMap.getMyTerritories().keySet()){
      if (gameMap.getTerritoryByName(t).getOwner().equals(this.territory.getOwner())){
        currentPlaced += gameMap.getTerritoryByName(t).getMyUnits().get(this.unit).getNum();
      }
    }
    return currentPlaced;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    return "(" + this.territory.getName() + ", " + this.unit + ", " + this.unitNum + ")";
  }
}
