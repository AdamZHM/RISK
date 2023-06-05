package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;

public class GameMapTest {

	@Test
	public void test_game_map() {
		GameMap gameMap = new GameMap(3);
		Set<String> set = new HashSet<>();
		set.add("Narnia");
		set.add("Midkemia");
		set.add("Oz");
		set.add("Gondor");
		set.add("Elantris");
		set.add("Scadrial");
		set.add("Mordor");
		set.add("Roshar");
		set.add("Hogwarts");
		assertEquals(set, gameMap.getTerritoryNames());
	}

	@Test
	public void test_updateMyTerritories() {
		GameMap gameMap = getGameMap();
		GameMap gameMap2 = getGameMap();
		
		Territory territory = gameMap2.getTerritoryByName("Oz");
		String unit = "soldier";
		Integer unitNum = 3;
		Integer originUnitNum = territory.getMyUnits().get(unit).getNum();
		territory.getMyUnits().get(unit).setNum(unitNum + originUnitNum);
		// System.out.println(gameMap.getTerritoryByName("Oz").getOwner());
		gameMap.getNumPlayer();
		gameMap.updateMyTerritories(gameMap2, "");
		assertEquals(3, gameMap.getTerritoryByName("Oz").getMyUnits().get("soldier").getNum());
		// System.out.println(gameMap.getTerritoryByName("Oz").getOwner());
		gameMap.updateMyTerritories(gameMap2, "Red");
	}

	@Test
	public void test_game_map_v2() {
		GameMap gameMap = getGameMap();
		gameMap.getCompleteTerritoryInfo();
		Territory narnia = gameMap.getMyTerritories().get("Narnia");
		assertEquals(narnia.getCostToNeighbor("Elantris"), 2);
	}

	private GameMap getGameMap(){
		GameMap gameMap = new GameMap(5);
		Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
		Map<String, Territory> my_territories = gameMap.getMyTerritories();
		Map<Integer, String> playerId2Color = PlayerId2ColorHelper.playerId2Color;
		for (Map.Entry<Integer, String> entry1 : playerId2Color.entrySet()) {
			for (String territory_name : territory_group.get(entry1.getKey())) {
				my_territories.get(territory_name).setOwner(entry1.getValue());
			}
		}
		return gameMap;
	}
}
