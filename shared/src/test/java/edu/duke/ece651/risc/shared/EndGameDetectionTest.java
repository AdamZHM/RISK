package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class EndGameDetectionTest {
  @Test
  public void test_getActivatePlayer() {
    GameMap gameMap = new GameMap(3);
    EndGameDetection endGameDetection = new EndGameDetection();

    for(String T : gameMap.getTerritoryNames()){
      if(T.equals("Oz")){
        gameMap.getMyTerritories().get(T).setOwner("Red");
      }
      else{
        gameMap.getMyTerritories().get(T).setOwner("Blue");
      }
    }

    // Two players (Red, Blue) Left
    Set<String> active_map = new HashSet<>(Arrays.asList("Blue", "Red"));
    assertEquals(active_map, endGameDetection.getActivePlayer(gameMap));
    assertFalse(endGameDetection.isEndGame(active_map));
    assertFalse(endGameDetection.isLost(active_map, "Red"));

    gameMap.getTerritoryByName("Oz").setOwner("Blue");
    Set<String> active_map_win = new HashSet<>(Arrays.asList("Blue"));

    assertEquals(active_map_win, endGameDetection.getActivePlayer(gameMap));
    assertTrue(endGameDetection.isEndGame(active_map_win));
    assertTrue(endGameDetection.isLost(active_map_win, "Red"));
    assertFalse(endGameDetection.isLost(active_map_win, "Blue"));
  }

}
