package edu.duke.ece651.risc.shared.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Action;
import edu.duke.ece651.risc.shared.GameMap;

public class MessageVOTest {
  @Test
  public void testMessageVO(){
    GameMap gameMap = new GameMap(3);
    
    MessageVO messageVO2 = new MessageVO(1, gameMap);
    Action action = null;
    MessageVO messageVO3 = new MessageVO("a", action);
    MessageVO messageVO4 = new MessageVO("a", gameMap);
    MessageVO messageVO5 = new MessageVO("a", "a");
    MessageVO messageVO6 = new MessageVO("a", 1);
    List<String> territory_group = gameMap.getTerritoryNamesInGroups().get(0);
    MessageVO messageVO = new MessageVO("", 0, "Blue", gameMap, territory_group, 3, 1, null, null, null, null, -1);
    assertEquals("", messageVO.getMessage());
    assertEquals(0, messageVO.getId());
    assertEquals("Blue", messageVO.getColor());
    assertEquals(gameMap, messageVO.getGameMap());
    assertEquals(territory_group, messageVO.getTerritory_group());
    assertEquals(3, messageVO.getPlayerNum());
    messageVO.setAction(null);
    messageVO.getAction();
    messageVO.setUserName("a");
    messageVO.getUserName();
    messageVO.setPassword("a");
    assertEquals("a", messageVO.getPassword());
    messageVO.setCurrentPlayerNum(1);
    assertEquals(1, messageVO.getCurrentPlayerNum());
    messageVO.setRoomName("abc");
    assertEquals("abc", messageVO.getRoomName());
    messageVO.setStatus(1);
    assertEquals(1, messageVO.getStatus());

  }
}
