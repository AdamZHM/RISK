package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PlacementTest {
  @Test
  public void test_Placement(){
    GameMap gameMap = new GameMap(3);

    Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
    Map<String, Territory> my_territories = gameMap.getMyTerritories();
    for (String territory_name : territory_group.get(2)) {
      my_territories.get(territory_name).setOwner("Blue");
    }

    String string = "Mordor,soldier,3";
    Placement placement = new Placement("Blue", string, gameMap);
    assertEquals(gameMap, placement.getGameMap());
    assertEquals("soldier", placement.getUnit());
    assertEquals(3, placement.getUnitNum());
    assertEquals(gameMap.getTerritoryByName("Mordor"), placement.getTerritory());
    String string1 = "Mordor,soldier";
    assertThrows(IllegalArgumentException.class, ()->new Placement("Blue", string1, gameMap));
    String string2 = "Oz,soldier,3";
    assertThrows(IllegalArgumentException.class, ()->new Placement("Blue", string2, gameMap));
    String string3 = "Mordor,soldier,3a";
    assertThrows(IllegalArgumentException.class, ()->new Placement("Blue", string3, gameMap));
    String string4 = "Ozz,soldier,3";
    assertThrows(IllegalArgumentException.class, ()->new Placement("Blue", string4, gameMap));
    String string5 = "Mordor,solder,3";
    assertThrows(IllegalArgumentException.class, ()->new Placement("Blue", string5, gameMap));
    String string6 = "Mordor,soldier,11";
    assertThrows(IllegalArgumentException.class, ()->new Placement("Blue", string6, gameMap));
    Placement p1 = new Placement("Blue", string, gameMap);
    assertEquals(placement.toString(), "(Mordor, soldier, 3)");
    assertEquals(placement.hashCode(), p1.hashCode());
  }

}
