package edu.duke.ece651.risc.shared.MapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Entity.Player;
import edu.duke.ece651.risc.shared.Entity.Room;
import edu.duke.ece651.risc.shared.Entity.RoomPlayer;
import edu.duke.ece651.risc.shared.Service.RoomPlayerService;

public class RoomPlayerTest {
  @Test
  public void test() {
    RoomPlayerService roomPlayerService = new RoomPlayerService();
    List<RoomPlayer> res = roomPlayerService.selectJoinedRooms("a");
    // assertEquals(0, res.size());
    // // assertEquals("ab", res.get(0).getPlayerName());

    // RoomPlayer roomPlayer = new RoomPlayer(2, "a");
    // roomPlayerService.saveOnePlayerToRoom(2, 0, "a");


    // List<RoomPlayer> res1 = roomPlayerService.selectJoinedRooms("a");
    // assertEquals("a", res1.get(0).getPlayerName());
    // roomPlayerService.deleteOnePlayerInRoom(roomPlayer);
    
    List<RoomPlayer> res2 = roomPlayerService.selectAllByRoomId(35);
    assertEquals(0, res2.size());

    RoomPlayer roomPlayer = new RoomPlayer(67, 1, "b", 100, 100, 1, false);
    roomPlayerService.updateResource(roomPlayer);
    RoomPlayer roomPlayer2 = new RoomPlayer(67, 2, "c", 100, 100, 1, false);
    roomPlayerService.saveOnePlayerToRoom(67, 2, "c", 1, 1, 1);
    roomPlayerService.deleteOnePlayerInRoom(roomPlayer2);
    List<RoomPlayer> res1 = roomPlayerService.selectAllByRoomId(67);
    // assertEquals(100, res.get(0).getFood());
    RoomPlayer roomPlayer3 = new RoomPlayer(193, "e", true);
    // roomPlayerService.updateCloak(roomPlayer3);
  }
}
