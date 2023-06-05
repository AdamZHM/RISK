package edu.duke.ece651.risc.shared.EntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Configure.SingletonSqlFactory;
import edu.duke.ece651.risc.shared.Entity.Room;
import edu.duke.ece651.risc.shared.Entity.RoomPlayer;

public class PlayerTest {
  @Test
  public void test() {

    SqlSession s = SingletonSqlFactory.getSingletonInstance().openSession(TransactionIsolationLevel.SERIALIZABLE);
    SqlSession s1 = SingletonSqlFactory.getSingletonInstance().openSession(TransactionIsolationLevel.SERIALIZABLE);
    RoomPlayer roomPlayer = new RoomPlayer(1);
    assertEquals(1, roomPlayer.getRoomId());
    RoomPlayer roomPlayer2 = new RoomPlayer(1, "playerName");
    assertEquals("playerName", roomPlayer2.getPlayerName());
    RoomPlayer roomPlayer3 = new RoomPlayer(1, 1, 1, "playerName");

    assertEquals(1, roomPlayer3.getId());
    assertEquals(1, roomPlayer3.getPlayerId());
    RoomPlayer roomPlayer4 = new RoomPlayer(1, 1, "playerName");
    RoomPlayer roomPlayer5 = new RoomPlayer(1, 1, 1, "a", 1, 1, 1);
    assertEquals(1, roomPlayer5.getFood());
    RoomPlayer roomPlayer6 = new RoomPlayer(1, 1, "a", 1, 1, 1, false);
    roomPlayer6.setId(1);
    roomPlayer6.setRoomId(1);
    roomPlayer4.setPlayerName("playerName");
    roomPlayer4.setFood(1);
    roomPlayer4.setPlayerId(1);
    roomPlayer6.setTechLevel(1);
    roomPlayer6.setTechResource(1);
    assertEquals(1, roomPlayer6.getTechLevel());
    assertEquals(1, roomPlayer6.getTechResource());

  }
}
