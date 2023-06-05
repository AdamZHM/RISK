package edu.duke.ece651.risc.shared.MapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.GameMapTextView;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.Unit;
import edu.duke.ece651.risc.shared.Entity.Room;
import edu.duke.ece651.risc.shared.Entity.Territorys;
import edu.duke.ece651.risc.shared.Service.RoomService;
import edu.duke.ece651.risc.shared.Service.TerritoryService;

public class TerritorysTest {
  @Test
  public void test(){
    TerritoryService territoryService = new TerritoryService();
    Territorys territorys = new Territorys("Oz", "soldier", 8, "red", 1);
    Territorys res = territoryService.selectTerritory(territorys);
    assertEquals("Green", res.getOwner());
    // territoryService.saveTerritory(territorys);
    // Territorys territorys3 = territoryService.selectTerritory(new Territorys("Oz", "soldier", 3));
    // assertEquals("soldier", territorys3.getUnitType());
    // territoryService.deleteTerritory(territorys);
  }

  @Test
  public void testInitMap(){
    TerritoryService territoryService = new TerritoryService();
    RoomService roomService = new RoomService();
    Room room = roomService.selectRoom("test");
    // int playerNum = room.getPlayerNum();
    // assertEquals(2, playerNum);
    GameMap gameMap = new GameMap(2);
    territoryService.updateMap(gameMap, room.getId());
    territoryService.recoverGampeMap(2, room.getId());
    assertEquals(territoryService.selectTerritorysByRoomId(room.getId()).get(0).getOwner(), "");
  }

  @Test
  public void test1(){
    TerritoryService territoryService = new TerritoryService();
    List<Territorys> res = territoryService.selectTerritorysByRoomId(21);
    assertEquals(0, res.size());
    // GameMap gameMap = territoryService.recoverGampeMap(2, 21);
    // assertEquals("Red", gameMap.getTerritoryByName("Scadrial").getOwner());
    // assertEquals(11, gameMap.getTerritoryByName("Scadrial").getUnitByName("soldier").getNum());
  }
}
