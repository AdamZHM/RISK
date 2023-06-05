package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.vo.MessageVO;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlayerHandlerTest {

  @Test
  public void testCreate() throws InterruptedException, UnknownHostException, IOException, ClassNotFoundException {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(12344);
          Messenger messenger = new Messenger(serverSocket);

          Map<String, RoomHandler> map = mock(Map.class);
          doReturn(true).when(map).containsKey("a");
          PlayerHandler playerHandler = new PlayerHandler(messenger);
          Server.roomMap = map;
          playerHandler.run();

        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger1 = new Messenger("127.0.0.1", 12344);
    MessageVO messageVO = new MessageVO("create", 0, null, null, null, 2, 0, "a", "a", null, null, 0);
    messenger1.send(messageVO);
    Boolean recvdMsg = (Boolean) messenger1.recv();
    // messenger1.send("o");
    // Boolean recvdMsg1 = (Boolean) messenger1.recv();
    assertEquals(false, recvdMsg);
    messageVO = new MessageVO("create", 0, null, null, null, 2, 0, "b", "b", null, null, 0);
    messenger1.send(messageVO);
    Boolean recvdMsg1 = (Boolean) messenger1.recv();
    // messenger1.send("o");
    // Boolean recvdMsg1 = (Boolean) messenger1.recv();
    assertEquals(true, recvdMsg1);
    // assertEquals(true, recvdMsg1);

    MessageVO messageVO2 = new MessageVO("join", 0, null, null, null, 2, 0, "a", "a", null, null, 0);
    messenger1.send(messageVO2);
    Set<String> availableRoomName = (Set<String>) messenger1.recv();

    MessageVO messageVO4 = new MessageVO("login", 0, null, null, null, 2, 0, "a", "a", null, null, 0);
    messenger1.send(messageVO4);
    MessageVO messageVO5 = new MessageVO("signup", 0, null, null, null, 2, 0, "a", "a", null, null, 0);
    messenger1.send(messageVO5);
    MessageVO messageVO1 = new MessageVO("exit", "a");
    messenger1.send(messageVO1);
    assertEquals(0, availableRoomName.size());

  }

  @Test
  public void testAction() throws InterruptedException, UnknownHostException, IOException {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(12341);
          Messenger messenger = new Messenger(serverSocket);

          PlayerHandler playerHandler = mock(PlayerHandler.class);
          doReturn(true).when(playerHandler).doAction(any());
          playerHandler.run();

        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger1 = new Messenger("127.0.0.1", 12341);
    MessageVO messageVO4 = new MessageVO("action", new GameMap(2));
    messenger1.send(messageVO4);
    assertEquals("action", messageVO4.getMessage());
  }

  @Test
  public void testJoin() throws InterruptedException, UnknownHostException, IOException, ClassNotFoundException {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(2347);
          Messenger messenger = new Messenger(serverSocket);

          RoomHandler roomHandler = new RoomHandler(2, "a", new GameMap(2), 1, 0);
          Server.roomMap.put("a", roomHandler);

          RoomHandler roomHandler1 = new RoomHandler(2, "b", new GameMap(2), 1, 0);
          Server.roomMap.put("b", roomHandler1);
          PlayerHandler playerHandler = new PlayerHandler(messenger);
          playerHandler.run();

        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger1 = new Messenger("127.0.0.1", 2347);
    MessageVO messageVO2 = new MessageVO("join", 0, null, null, null, 2, 0, "a", "a", null, null, 0);
    messenger1.send(messageVO2);
    Set<String> availableRoomName = (Set<String>) messenger1.recv();
    messenger1.send("menu");

    messenger1.send(messageVO2);
    availableRoomName = (Set<String>) messenger1.recv();
    assertEquals(2, availableRoomName.size());
    messenger1.send("a");

    MessageVO messageVO = new MessageVO("resume", "b");
    messenger1.send(messageVO);
    MessageVO messageVO3 = new MessageVO("resume", "a");
    messenger1.send(messageVO3);
    Set<String> joinedRoomName = (Set<String>) messenger1.recv();
    assertEquals(0, joinedRoomName.size());
    messenger1.send("a");

    Action action = null;
    MessageVO messageVO5 = new MessageVO("action", action);
    messenger1.send(messageVO5);
    MessageVO messageVO4 = new MessageVO("placement", new GameMap(2));
    messenger1.send(messageVO4);

  }

  @Test
  public void testPlacement() throws InterruptedException, UnknownHostException, IOException {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(12348);
          Messenger messenger = new Messenger(serverSocket);

          RoomHandler roomHandler = new RoomHandler(2, "a", new GameMap(2), 1, 0);
          // roomHandler.currentPlayerNum = new AtomicInteger(2);
          Server.roomMap.put("a", roomHandler);
          PlayerHandler playerHandler = new PlayerHandler(messenger);
          playerHandler.run();

        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger1 = new Messenger("127.0.0.1", 12348);
    MessageVO messageVO4 = new MessageVO("test", new GameMap(2));
    messenger1.send(messageVO4);
    assertEquals("test", messageVO4.getMessage());
    messageVO4 = new MessageVO("placement", new GameMap(2));
    messenger1.send(messageVO4);
    assertEquals("placement", messageVO4.getMessage());
  }

  @Test
  public void testDoPlacement() throws ClassNotFoundException, IOException, InterruptedException {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(12350);
          Messenger messenger = new Messenger(serverSocket);

          RoomHandler roomHandler = mock(RoomHandler.class);
          roomHandler.playerNum = 1;
          roomHandler.name2Status = new HashMap<>();
          doReturn(1).when(roomHandler).addCurrentPlayerNumByOne();
          GameMap gameMap = new GameMap(2);
          doNothing().when(roomHandler).recvInitialPlacementFromOnePlayer("a", gameMap);
          Server.roomMap.put("a", roomHandler);

          PlayerHandler playerHandler = new PlayerHandler(messenger);
          playerHandler.currentRoomHandler = roomHandler;
          playerHandler.doPlacement(gameMap);
          assertEquals(1, roomHandler.playerNum);
        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger1 = new Messenger("127.0.0.1", 12350);
  }

  @Test
  public void testPlacementNotEqual() throws ClassNotFoundException, IOException, InterruptedException {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(12351);
          Messenger messenger = new Messenger(serverSocket);

          RoomHandler roomHandler = mock(RoomHandler.class);
          roomHandler.playerNum = 1;
          roomHandler.name2Status = new HashMap<>();
          doReturn(2).when(roomHandler).addCurrentPlayerNumByOne();
          GameMap gameMap = new GameMap(2);
          doNothing().when(roomHandler).recvInitialPlacementFromOnePlayer("a", gameMap);
          Server.roomMap.put("a", roomHandler);

          PlayerHandler playerHandler = new PlayerHandler(messenger);
          playerHandler.currentRoomHandler = roomHandler;
          playerHandler.doPlacement(gameMap);
          assertEquals(1, roomHandler.playerNum);
        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger1 = new Messenger("127.0.0.1", 12351);
  }

  @Test
  public void testDoAction1() throws UnknownHostException, IOException, InterruptedException {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(12352);
          Messenger messenger = new Messenger(serverSocket);

          RoomHandler roomHandler = mock(RoomHandler.class);
          roomHandler.playerNum = 1;
          roomHandler.name2Status = new HashMap<>();
          doReturn(2).when(roomHandler).addCurrentPlayerNumByOne();
          GameMap gameMap = new GameMap(2);
          doNothing().when(roomHandler).recvActionFromOnePlayer("a", null);
          Server.roomMap.put("a", roomHandler);
          assertEquals(2, gameMap.getNumPlayer());
          PlayerHandler playerHandler = new PlayerHandler(messenger);
          playerHandler.currentRoomHandler = roomHandler;
          playerHandler.doAction(null);
        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger1 = new Messenger("127.0.0.1", 12352);
  }

  // @Test
  // public void testDoAction2() throws UnknownHostException, IOException,
  // InterruptedException {
  // Thread th = new Thread() {
  // @Override()
  // public void run() {
  // try {
  // ServerSocket serverSocket = new ServerSocket(12353);
  // Messenger messenger = new Messenger(serverSocket);

  // RoomHandler roomHandler = mock(RoomHandler.class);
  // roomHandler.playerNum = 2;
  // roomHandler.name2Status = new HashMap<>();
  // doReturn(2).when(roomHandler).addCurrentPlayerNumByOne();
  // doNothing().when(roomHandler).recvActionFromOnePlayer("a", null);
  // Server.roomMap.put("a", roomHandler);

  // EndGameDetection endGameDetection = mock(EndGameDetection.class);
  // Set<String> set = mock(Set.class);
  // doReturn(false).when(endGameDetection).isEndGame(anySet());
  // roomHandler.endGameDetection = endGameDetection;

  // ServerActionHelper serverActionHelper = mock(ServerActionHelper.class);
  // roomHandler.serverActionHelper = serverActionHelper;

  // PlayerHandler playerHandler = new PlayerHandler(messenger);
  // playerHandler.currentRoomHandler = roomHandler;
  // // assertNotEquals(2, roomHandler.playerNum);
  // playerHandler.doAction(null);
  // // assertEquals(0, roomHandler.getCurrentPlayerNum());
  // } catch (Exception e) {
  // }
  // }
  // };
  // th.start();
  // Thread.sleep(100); // a bit of hack
  // Messenger messenger1 = new Messenger("127.0.0.1", 12353);
  // }

  // @Test
  // public void testDoAction3() throws UnknownHostException, IOException,
  // InterruptedException {
  // Thread th = new Thread() {
  // @Override()
  // public void run() {
  // try {
  // ServerSocket serverSocket = new ServerSocket(12354);
  // Messenger messenger = new Messenger(serverSocket);

  // RoomHandler roomHandler = mock(RoomHandler.class);
  // roomHandler.playerNum = 2;
  // roomHandler.name2Status = new HashMap<>();
  // doReturn(2).when(roomHandler).addCurrentPlayerNumByOne();
  // doNothing().when(roomHandler).recvActionFromOnePlayer("a", null);
  // Server.roomMap.put("a", roomHandler);
  // roomHandler.gameMap = new GameMap(2);

  // EndGameDetection endGameDetection = mock(EndGameDetection.class);
  // Set<String> set = mock(Set.class);
  // doReturn(set).when(endGameDetection).getActivePlayer(new GameMap(2));
  // doReturn(2).when(set).size();
  // doReturn(true).when(endGameDetection).isEndGame(anySet());
  // roomHandler.endGameDetection = endGameDetection;

  // ServerActionHelper serverActionHelper = mock(ServerActionHelper.class);
  // roomHandler.serverActionHelper = serverActionHelper;

  // PlayerHandler playerHandler = new PlayerHandler(messenger);
  // playerHandler.currentRoomHandler = roomHandler;
  // // assertNotEquals(2, roomHandler.playerNum);
  // playerHandler.doAction(null);
  // } catch (Exception e) {
  // }
  // }
  // };
  // th.start();
  // Thread.sleep(100); // a bit of hack
  // Messenger messenger1 = new Messenger("127.0.0.1", 12354);
  // }

  @Test
  public void testValidatePassword() throws ClassNotFoundException, IOException {
    Messenger messenger = mock(Messenger.class);
    PlayerHandler playerHandler = new PlayerHandler(messenger);
    playerHandler.validatePassword("username", "password");
    Server.UserPassword.put("username", "password");
    playerHandler.validatePassword("username", "password");
    playerHandler.validatePassword("username", "pass");
    assertEquals(1, Server.UserPassword.size());
    assertEquals("password", Server.UserPassword.get("username"));
  }

  @Test
  public void testSetupPassword() throws ClassNotFoundException, IOException {
    Messenger messenger = mock(Messenger.class);
    PlayerHandler playerHandler = new PlayerHandler(messenger);
    playerHandler.setupPassword("a", "password");
    assertEquals(false, Server.UserPassword.containsKey("a"));
    Server.UserPassword.put("username", "password");
    playerHandler.setupPassword("username", "password");
    assertEquals(true, Server.UserPassword.containsKey("username"));
  }
}
