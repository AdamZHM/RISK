package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;

public class GameMapTextViewTest {

  @Test
  public void test_displayMyTerritory() {
    GameMap gameMap = new GameMap(3);
    Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
    Map<String, Territory> my_territories = gameMap.getMyTerritories();
    for (String territory_name : territory_group.get(0)) {
      my_territories.get(territory_name).setOwner("Red");
    }
    GameMapTextView view = new GameMapTextView();
    assertEquals(view.displayMyTerritory(gameMap,"Red"), " 0 soldier(s) in Narnia: (next to: Elantris, Midkemia)\n" +
        " 0 soldier(s) in Midkemia: (next to: Elantris, Narnia, Oz, Scadrial)\n" +
        " 0 soldier(s) in Oz: (next to: Gondor, Midkemia, Mordor, Scadrial)\n");
  }

  @Test
  public void test_displayTerritory() {
		GameMap gameMap = getGameMap();
		GameMap gameMap2 = getGameMap();
    GameMapTextView view = new GameMapTextView();
    assertEquals(view.displayAllTerritory(gameMap, 3),
        "Red player:\n" +
            "----------\n" +
            " 0 soldier(s) in Narnia: (next to: Elantris, Midkemia)\n" +
            " 0 soldier(s) in Midkemia: (next to: Elantris, Narnia, Oz, Scadrial)\n" +
            " 0 soldier(s) in Oz: (next to: Gondor, Midkemia, Mordor, Scadrial)\n\n" +
            "Green player:\n" +
            "----------\n" +
            " 0 soldier(s) in Elantris: (next to: Midkemia, Narnia, Roshar, Scadrial)\n" +
            " 0 soldier(s) in Scadrial: (next to: Elantris, Hogwarts, Midkemia, Mordor, Oz, Roshar)\n" +
            " 0 soldier(s) in Roshar: (next to: Elantris, Hogwarts, Scadrial)\n\n" +
            "Blue player:\n" +
            "----------\n" +
            " 0 soldier(s) in Mordor: (next to: Gondor, Hogwarts, Oz, Scadrial)\n" +
            " 0 soldier(s) in Hogwarts: (next to: Mordor, Roshar, Scadrial)\n" +
            " 0 soldier(s) in Gondor: (next to: Mordor, Oz)\n\n");
    Territory territory = gameMap2.getTerritoryByName("Oz");
    String unit = "soldier";
    Integer unitNum = 3;
    Integer originUnitNum = territory.getMyUnits().get(unit).getNum();
    territory.getMyUnits().get(unit).setNum(unitNum + originUnitNum);
    gameMap.updateMyTerritories(gameMap2, ""); 
    // System.out.println(view.displayAllTerritory());
    assertEquals(view.displayAllTerritory(gameMap, 3),
        "Red player:\n" +
            "----------\n" +
            " 0 soldier(s) in Narnia: (next to: Elantris, Midkemia)\n" +
            " 0 soldier(s) in Midkemia: (next to: Elantris, Narnia, Oz, Scadrial)\n" +
            " 3 soldier(s) in Oz: (next to: Gondor, Midkemia, Mordor, Scadrial)\n\n" +
            "Green player:\n" +
            "----------\n" +
            " 0 soldier(s) in Elantris: (next to: Midkemia, Narnia, Roshar, Scadrial)\n" +
            " 0 soldier(s) in Scadrial: (next to: Elantris, Hogwarts, Midkemia, Mordor, Oz, Roshar)\n" +
            " 0 soldier(s) in Roshar: (next to: Elantris, Hogwarts, Scadrial)\n\n" +
            "Blue player:\n" +
            "----------\n" +
            " 0 soldier(s) in Mordor: (next to: Gondor, Hogwarts, Oz, Scadrial)\n" +
            " 0 soldier(s) in Hogwarts: (next to: Mordor, Roshar, Scadrial)\n" +
            " 0 soldier(s) in Gondor: (next to: Mordor, Oz)\n\n");
  }

  private GameMap getGameMap(){
		GameMap gameMap = new GameMap(3);
		Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
		Map<String, Territory> my_territories = gameMap.getMyTerritories();
		Map<Integer, String> playerId2Color = PlayerId2ColorHelper.playerId2Color;
    for (int i = 0 ; i < 3; i++){
			for (String territory_name : territory_group.get(i)) {
				my_territories.get(territory_name).setOwner(playerId2Color.get(i));
			}
		}
		return gameMap;
	}
}
