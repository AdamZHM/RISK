package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class PlacementCheckerTest {
  @Test
  public void test() {
    GameMap gameMap = new GameMap(3);
    // setupTerritory(2, "Blue", gameMap);
    Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
    Map<String, Territory> my_territories = gameMap.getMyTerritories();
    for (String territory_name : territory_group.get(2)) {
      my_territories.get(territory_name).setOwner("Blue");
    }
    PlacementChecker placementChecker = new PlacementChecker(gameMap);
    Territory territory = my_territories.get("Mordor");
    territory.getMyUnits().get("soldier").setNum(11);
    assertEquals("Error: Haven't placed exactly 10 soldiers to place!\n", placementChecker.check(2));
    territory.getMyUnits().get("soldier").setNum(10);
    assertEquals(null, placementChecker.check(2));

    GameMap gameMap2 = new GameMap(2);
    placementChecker.updateMap(gameMap2);
    Map<String, Territory> my_territories1 = gameMap2.getMyTerritories();
    Territory territory1 = my_territories1.get("Mordor");
    assertEquals(0, territory1.getMyUnits().get("soldier").getNum());
  }

  // public void setupTerritory(int id, String playerColor, GameMap gameMap) {
  //   Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
  //   Map<String, Territory> my_territories = gameMap.getMyTerritories();
  //   for (String territory_name : territory_group.get(id)) {
  //     my_territories.get(territory_name).setOwner(playerColor);
  //   }
  // }
}
