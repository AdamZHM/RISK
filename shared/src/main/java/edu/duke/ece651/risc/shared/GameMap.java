package edu.duke.ece651.risc.shared;

import java.io.*;
import java.util.*;

/**
 * This is the map class
 */
public class GameMap implements Serializable {

  /**
   * This is a static map containing all territories' resource creation information
   */
  static public final Map<String, Map<String, Integer>> resourceInfoMap = new HashMap<>(){{
    put("Narnia", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Midkemia", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Oz", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Gondor", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Elantris", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Scadrial", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Mordor", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Roshar", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Hogwarts", new HashMap<>(){{put("food", 5); put("technology", 50);}});
    put("Duke", new HashMap<>(){{put("food", 5); put("technology", 50);}});
  }};

  /**
   * This has the game map's static territory information: territories' names, and
   * their neighbors' names
   * This map is for 2 players' game, and is the same as the 4 player's map
   */
  static final Map<String, Map<String, Integer>> territoryInfo2 = new HashMap<>() {{
    put("Narnia", new HashMap<>(){{put("Elantris", 2); put("Midkemia", 2);}});
    put("Midkemia", new HashMap<>(){{put("Narnia", 2); put("Elantris", 2); put("Scadrial", 2); put("Oz", 2);}});
    put("Oz", new HashMap<>(){{put("Midkemia", 2); put("Scadrial", 2); put("Mordor", 2); put("Gondor", 2);}});
    put("Gondor", new HashMap<>(){{put("Oz", 2); put("Mordor", 2);}});
    put("Elantris", new HashMap<>(){{put("Narnia", 2); put("Midkemia", 2); put("Scadrial", 2); put("Roshar", 2);}});
    put("Scadrial", new HashMap<>(){{put("Midkemia", 2); put("Elantris", 2); put("Roshar", 2); put("Mordor", 2); put("Oz", 2);}});
    put("Mordor", new HashMap<>(){{put("Oz", 2); put("Scadrial", 2); put("Gondor", 2);}});
    put("Roshar", new HashMap<>(){{put("Scadrial", 2); put("Elantris", 2);}});
  }};

  /**
   * This has the game map's static territory information: territories' names, and
   * their neighbors' names
   * This map is for 3 players' game
   */
  static final Map<String, Map<String, Integer>> territoryInfo3 = new HashMap<>() {{
    put("Narnia", new HashMap<>(){{put("Elantris", 2); put("Midkemia", 2);}});
    put("Midkemia", new HashMap<>(){{put("Narnia", 2); put("Elantris", 2); put("Scadrial", 2); put("Oz", 2);}});
    put("Oz", new HashMap<>(){{put("Midkemia", 2); put("Scadrial", 2); put("Mordor", 2); put("Gondor", 2);}});
    put("Gondor", new HashMap<>(){{put("Oz", 2); put("Mordor", 2);}});
    put("Elantris", new HashMap<>(){{put("Narnia", 2); put("Midkemia", 2); put("Scadrial", 2); put("Roshar", 2);}});
    put("Scadrial", new HashMap<>(){{put("Midkemia", 2); put("Elantris", 2); put("Roshar", 2); put("Hogwarts", 2); put("Mordor", 2); put("Oz", 2);}});
    put("Mordor", new HashMap<>(){{put("Oz", 2); put("Scadrial", 2); put("Hogwarts", 2); put("Gondor", 2);}});
    put("Roshar", new HashMap<>(){{put("Scadrial", 2); put("Elantris", 2); put("Hogwarts", 2);}});
    put("Hogwarts", new HashMap<>(){{put("Mordor", 2); put("Scadrial", 2); put("Roshar", 2);}});
  }};

  /**
   * This has the game map's static territory information: territories' names, and
   * their neighbors' names
   * This map is for 4 players' game, and is the same as the 2 players' map
   */
  static final Map<String, Map<String, Integer>> territoryInfo4 = new HashMap<>() {{
    put("Narnia", new HashMap<>(){{put("Elantris", 2); put("Midkemia", 2);}});
    put("Midkemia", new HashMap<>(){{put("Narnia", 2); put("Elantris", 2); put("Scadrial", 2); put("Oz", 2);}});
    put("Oz", new HashMap<>(){{put("Midkemia", 2); put("Scadrial", 2); put("Mordor", 2); put("Gondor", 2);}});
    put("Gondor", new HashMap<>(){{put("Oz", 2); put("Mordor", 2);}});
    put("Elantris", new HashMap<>(){{put("Narnia", 2); put("Midkemia", 2); put("Scadrial", 2); put("Roshar", 2);}});
    put("Scadrial", new HashMap<>(){{put("Midkemia", 2); put("Elantris", 2); put("Roshar", 2); put("Mordor", 2); put("Oz", 2);}});
    put("Mordor", new HashMap<>(){{put("Oz", 2); put("Scadrial", 2); put("Gondor", 2);}});
    put("Roshar", new HashMap<>(){{put("Scadrial", 2); put("Elantris", 2);}});
  }};

  /**
   * This has the game map's static territory information: territories' names, and
   * their neighbors' names
   * This map is for 5 players' game
   */
  static final Map<String, Map<String, Integer>> territoryInfo5 = new HashMap<>() {{
    put("Narnia", new HashMap<>(){{put("Elantris", 2); put("Midkemia", 2);}});
    put("Midkemia", new HashMap<>(){{put("Narnia", 2); put("Elantris", 2); put("Scadrial", 2); put("Oz", 2);}});
    put("Oz", new HashMap<>(){{put("Midkemia", 2); put("Scadrial", 2); put("Mordor", 2); put("Gondor", 2);}});
    put("Gondor", new HashMap<>(){{put("Oz", 2); put("Mordor", 2); put("Duke", 2);}});
    put("Elantris", new HashMap<>(){{put("Narnia", 2); put("Midkemia", 2); put("Scadrial", 2); put("Roshar", 2);}});
    put("Scadrial", new HashMap<>(){{put("Midkemia", 2); put("Elantris", 2); put("Roshar", 2); put("Hogwarts", 2); put("Mordor", 2); put("Oz", 2);}});
    put("Mordor", new HashMap<>(){{put("Oz", 2); put("Scadrial", 2); put("Hogwarts", 2); put("Gondor", 2); put("Duke", 2);}});
    put("Roshar", new HashMap<>(){{put("Scadrial", 2); put("Elantris", 2); put("Hogwarts", 2);}});
    put("Hogwarts", new HashMap<>(){{put("Mordor", 2); put("Scadrial", 2); put("Roshar", 2); put("Duke", 2);}});
    put("Duke", new HashMap<>(){{put("Gondor", 2); put("Mordor", 2); put("Hogwarts", 2);}});
  }};

  /**
   * Key: number of players
   * Value: the game map to use correspondent to the number of players
   */
  final Map<Integer, Map<String, Map<String, Integer>>> territoryInfos = new HashMap<>(){{
    put(2, territoryInfo2);
    put(3, territoryInfo3);
    put(4, territoryInfo4);
    put(5, territoryInfo5);
  }};

  /**
   * This is the territory groups for 2 players
   */
  static final Map<Integer, List<String>> territoryNamesInGroups2 = new HashMap<>() {{
    put(0, new ArrayList<>(Arrays.asList("Narnia", "Elantris", "Roshar", "Scadrial")));
    put(1, new ArrayList<>(Arrays.asList("Midkemia", "Oz", "Gondor", "Mordor")));
  }};

  /**
   * This is the territory groups for 3 players
   */
  static final Map<Integer, List<String>> territoryNamesInGroups3 = new HashMap<>() {{
    put(0, new ArrayList<>(Arrays.asList("Narnia", "Midkemia", "Oz")));
    put(1, new ArrayList<>(Arrays.asList("Elantris", "Scadrial", "Roshar")));
    put(2, new ArrayList<>(Arrays.asList("Gondor", "Mordor", "Hogwarts")));
  }};

  /**
   * This is the territory groups for 4 players
   */
  static final Map<Integer, List<String>> territoryNamesInGroups4 = new HashMap<>() {{
    put(0, new ArrayList<>(Arrays.asList("Narnia", "Midkemia")));
    put(1, new ArrayList<>(Arrays.asList("Oz", "Gondor")));
    put(2, new ArrayList<>(Arrays.asList("Elantris", "Roshar")));
    put(3, new ArrayList<>(Arrays.asList("Scadrial", "Mordor")));
  }};

  /**
   * This is the territory groups for 5 players
   */
  static final Map<Integer, List<String>> territoryNamesInGroups5 = new HashMap<>() {{
    put(0, new ArrayList<>(Arrays.asList("Narnia", "Midkemia")));
    put(1, new ArrayList<>(Arrays.asList("Oz", "Gondor")));
    put(2, new ArrayList<>(Arrays.asList("Elantris", "Roshar")));
    put(3, new ArrayList<>(Arrays.asList("Scadrial", "Mordor")));
    put(4, new ArrayList<>(Arrays.asList("Hogwarts", "Duke")));
  }};

  /**
   * Key: number of players
   * Value: the territory groups to use correspondent to the number of players
   */
  final Map<Integer, Map<Integer, List<String>>> territoryGroups = new HashMap<>(){{
    put(2, territoryNamesInGroups2);
    put(3, territoryNamesInGroups3);
    put(4, territoryNamesInGroups4);
    put(5, territoryNamesInGroups5);
  }};

  /**
   * This is the number of players
   */
  private int numPlayers;

  /**
   * This holds the territories in the game map
   * key is the territory's name
   * value is the territory instance corresponding to the name
   */
  private Map<String, Territory> myTerritories;

  /**
   *
   * @param numPlayers
   */
  public GameMap(int numPlayers) {
    this.myTerritories = new HashMap<>();
    this.numPlayers = numPlayers;
    setUpMyTerritories();
  }

  /**
   * Get myTerritories in the game map
   *
   * @return the myTerritories in the game map
   */
  public Map<String, Territory> getMyTerritories() {
    return this.myTerritories;
  }

  /**
   * Get the territory instance based on the specified territory name
   *
   * @param territoryName is the name of the territory
   * @return the territory instance correspondent to the specified territory name
   */
  public Territory getTerritoryByName(String territoryName) {
    return this.myTerritories.get(territoryName);
  }

  /**
   * This get function returns all the territory names
   *
   * @return all the territory names
   */
  public Set<String> getTerritoryNames() {
    return territoryInfos.get(numPlayers).keySet();
  }

  /**
   * This returns territory info - adjacency relationship, territory names, but exclude edge information
   *
   * @return the territory info of the game
   */
  public Map<String, List<String>> getTerritoryInfo() {
    Map<String, List<String>> toReturn = new HashMap<>();
    Map<String, Map<String, Integer>> territoryInfo = territoryInfos.get(numPlayers);
    for (String name: territoryInfo.keySet()) {
      toReturn.put(name, new ArrayList<>());
      toReturn.get(name).addAll(territoryInfo.get(name).keySet());
      Collections.sort(toReturn.get(name));
    }
    return toReturn;
  }

  /**
   * This returns territory info - adjacency relationship, territory names, including edge information
   *
   * @return the complete territory info of the game
   */
  public Map<String, Map<String, Integer>> getCompleteTerritoryInfo() {
    return territoryInfos.get(numPlayers);
  }

  /**
   * This function should be called only once when asking players to choose a
   * group of territories to start the game with
   * It returns the territory names in groups
   * key is the group id, used to keep check which group it is
   * value is the corresponding territory name set of that group
   *
   * @return the territory names in groups
   */
  public Map<Integer, List<String>> getTerritoryNamesInGroups() {
    return territoryGroups.get(numPlayers);
  }

  /**
   * Get the number of players
   *
   * @return the number of players
   */
  public int getNumPlayer() {
    return this.numPlayers;
  }

  /**
   * This is the initial set up to set up myTerritories in the game map
   */
  private void setUpMyTerritories() {
    for (String territoryName : territoryInfos.get(numPlayers).keySet()) {
      Territory territory = new Territory(territoryName, "");
      myTerritories.put(territoryName, territory);
    }
    for (String territoryName : territoryInfos.get(numPlayers).keySet()) {
      Territory toSetUp = myTerritories.get(territoryName);
      setUpNeighborHelper(toSetUp);
      setUpCostToNeighHelper(toSetUp);
      setUpTerritoryResourceHelper(toSetUp);
    }
  }

  /**
   * Helper function to set up neighbors for designated territory
   *
   * @param territory is the territory instance to set up neighbors for
   */
  private void setUpNeighborHelper(Territory territory) {
    for (String neighborName : territoryInfos.get(numPlayers).get(territory.getName()).keySet()) {
      territory.getMyNeighbors().put(neighborName, myTerritories.get(neighborName));
    }
  }

  /**
   * Helper function to set up territory's costs to its neighbors
   *
   * @param territory is the territory instance to set up costs to its neighbors for
   */
  private void setUpCostToNeighHelper(Territory territory) {
    Map<String, Integer> territoryInfo = territoryInfos.get(numPlayers).get(territory.getName());
    for (String neighborName : territoryInfo.keySet()) {
      territory.getMyCostToNeighInfo().put(neighborName, territoryInfo.get(neighborName));
    }
  }

  /**
   * Helper function to set up territory's resource generation information
   *
   * @param territory is the territory instance to set up resource generation information for
   */
  private void setUpTerritoryResourceHelper(Territory territory) {
    Map<String, Integer> resourceMap = resourceInfoMap.get(territory.getName());
    for (String resourceName: resourceMap.keySet()) {
      territory.getMyResources().put(resourceName, resourceMap.get(resourceName));
    }
  }

  /**
   * This function will update the GameMap at client after recv the GameMap from
   * server.
   * 
   * @param newGameMap
   */
  public void updateMyTerritories(GameMap newGameMap, String playerColor) {
    for (String territoryName : territoryInfos.get(numPlayers).keySet()) {
      Territory territory = this.myTerritories.get(territoryName);
      if (playerColor.equals("") || territory.getOwner().equals(playerColor)) {
        myTerritories.put(territoryName, newGameMap.getTerritoryByName(territoryName));
      }
    }
  }

}
