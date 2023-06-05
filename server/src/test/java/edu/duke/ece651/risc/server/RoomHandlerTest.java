package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.vo.MessageVO;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoomHandlerTest {

  @Test
  public void testSendInitMessageToAll() throws ClassNotFoundException, IOException {
    Messenger messenger1 = mock(Messenger.class);
    Messenger messenger2 = mock(Messenger.class);
    RoomHandler roomHandler = new RoomHandler(2, "a", new GameMap(2), 1, 0);
    roomHandler.acceptOnePlayer("a", messenger1);
    roomHandler.acceptOnePlayer("b", messenger2);
    Server.allName2Messenger.put("a", messenger1);
    Server.allName2Messenger.put("b", messenger2);
    roomHandler.sendInitMessageToAll();
    assertEquals("Red", roomHandler.playerId2Color.get(0));
    assertEquals(0, roomHandler.playerName2Id.get("a"));
    assertEquals("Green", roomHandler.playerId2Color.get(1));
    assertEquals(1, roomHandler.playerName2Id.get("b"));
  }

  @Test
  public void testRoomAvailable() throws IOException, ClassNotFoundException {
    GameMap gameMap = new GameMap(2);
    RoomHandler roomHandler = new RoomHandler(2, "a", gameMap, 1, 0);
    assertEquals(true, roomHandler.roomAvailable());
    Messenger messenger1 = mock(Messenger.class);
    Messenger messenger2 = mock(Messenger.class);
    roomHandler.acceptOnePlayer("a", messenger1);
    roomHandler.acceptOnePlayer("b", messenger2);
    Server.allName2Messenger.put("a", messenger1);
    Server.allName2Messenger.put("b", messenger2);
    roomHandler.updateTerritoryUnit();
    roomHandler.sendGameMapAfterPlacementDoneToAll();
    roomHandler.sendGameMapAfterOneRoundToAll();
    roomHandler.recvActionFromOnePlayer("a", null);
    assertEquals(false, roomHandler.roomAvailable());
    roomHandler.setCurrentPlayerNumToZero();
    assertEquals(0, roomHandler.getCurrentPlayerNum().get());
    roomHandler.addCurrentPlayerNumByOne();
    assertEquals(1, roomHandler.getCurrentPlayerNum().get());
  }

  @Test
  public void test_send_game_map_after_one_round_to_all() throws IOException, ClassNotFoundException {
    GameMap gameMap = new GameMap(2);
    RoomHandler room = new RoomHandler(2, "room", gameMap, 1, 0);
    Messenger messenger1 = mock(Messenger.class);
    Messenger messenger2 = mock(Messenger.class);
    room.acceptOnePlayer("a", messenger1);
    room.acceptOnePlayer("b", messenger2);
    Server.allName2Messenger.put("a", messenger1);
    Server.allName2Messenger.put("b", messenger2);
    doThrow(IOException.class).when(messenger1).messengerAlive();
    Map<Integer, String> map = mock(Map.class);
    doReturn(messenger1).when(map).get(any());
    Set<String> set = mock(Set.class);
    doReturn(false).when(set).contains(any());
    EndGameDetection detector = mock(EndGameDetection.class);
    doReturn(set).when(detector).getActivePlayer(any());
    when(detector.isEndGame(set)).thenReturn(true);
    ServerActionHelper serverActionHelper = mock(ServerActionHelper.class);
    doReturn("abc").when(serverActionHelper).doCheckRehearser(any());
    room.playerId2Name = map;
    room.endGameDetection = detector;
    // room.sendGameMapAfterOneRoundToAll();
    room.recvActionFromOnePlayer("a", null);
    PlacementChecker placementChecker = mock(PlacementChecker.class);
    doReturn(null).when(placementChecker).check(anyInt());
    room.placementChecker = placementChecker;
    room.recvInitialPlacementFromOnePlayer("a", gameMap);
    assertEquals(null, messenger1.recv());
    assertEquals(null, messenger2.recv());
    assertEquals(2, room.getCurrentPlayerNum().get());
  }

  @Test
  public void test() throws ClassNotFoundException, IOException {
    GameMap gameMap = new GameMap(2);
    RoomHandler room = new RoomHandler(2, "room", gameMap, 1, 0);
    Messenger messenger1 = mock(Messenger.class);
    Messenger messenger2 = mock(Messenger.class);
    room.acceptOnePlayer("a", messenger1);
    room.acceptOnePlayer("b", messenger2);
    Server.allName2Messenger.put("a", messenger1);
    Server.allName2Messenger.put("b", messenger2);
    MessageVO messageVO = new MessageVO(1, gameMap);
    ServerActionHelper serverActionHelper = mock(ServerActionHelper.class);
    room.serverActionHelper = serverActionHelper;
    // doReturn("not null").when(serverActionHelper).doCheckRehearser(null);
    when(serverActionHelper.doCheckRehearser(null)).thenReturn("not null", null);
    PlacementChecker placementChecker = mock(PlacementChecker.class);
    when(placementChecker.check(anyInt())).thenReturn("not null", null);
    room.placementChecker = placementChecker;
    doReturn(messageVO).when(messenger1).recv();
    room.recvInitialPlacementFromOnePlayer("a", gameMap);
    room.recvActionFromOnePlayer("a", null);
    assertEquals(false, room.roomAvailable());
    assertEquals(messenger1, Server.allName2Messenger.get("a"));
  }
}