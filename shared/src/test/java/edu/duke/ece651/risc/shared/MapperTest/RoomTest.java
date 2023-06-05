package edu.duke.ece651.risc.shared.MapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Entity.Room;
import edu.duke.ece651.risc.shared.Service.RoomService;

public class RoomTest {
  @Test
  public void test() {
    RoomService roomService = new RoomService();
    Room room1 = new Room("testroom", 2, 0);
    Room r = new Room("testroom", 2, 0);
    room1.getId();
    boolean flag = roomService.saveRoom(room1);
    assertEquals(false, roomService.saveRoom(r));
    assertEquals(true, flag);
    // Room room1 = roomService.selectRoom("ab");
    // assertEquals(2, room1.getPlayerNum());
    Room room = roomService.selectRoom(room1.getname());  
    int before = room.getRound();
    roomService.updateRoomRound(room1.getname());
    roomService.selectAllRooms();
    Room room2 = roomService.selectRoom(room1.getname());  
    roomService.clearUpRooms();
    assertEquals(before + 1, room2.getRound());
    roomService.deleteRoom(room2.getname());
  }
}
