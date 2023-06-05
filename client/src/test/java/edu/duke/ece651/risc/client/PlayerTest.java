package edu.duke.ece651.risc.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.framework.qual.DefaultQualifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Action;
import edu.duke.ece651.risc.shared.EndGameDetection;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Messenger;
import edu.duke.ece651.risc.shared.MoveAction;
import edu.duke.ece651.risc.shared.MoveAttackAction;
import edu.duke.ece651.risc.shared.Placement;
import edu.duke.ece651.risc.shared.PlayerActionHelper;
import edu.duke.ece651.risc.shared.ResourceHelper;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.VisionHelper;
import edu.duke.ece651.risc.shared.Entity.Vision;
import edu.duke.ece651.risc.shared.Service.PlayerService;
import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;

public class PlayerTest {
  @Test
  public void test() throws ClassNotFoundException, IOException, InterruptedException{
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(12345);
          Messenger messenger = new Messenger(serverSocket);
          //System.out.println("send map");
          MessageVO messageVO = new MessageVO("2", new GameMap(2));
          messenger.send(messageVO);
          //System.out.println("send resource");
          ResourceHelper mockResource = mock(ResourceHelper.class);
          //System.out.println("send vision");
          VisionHelper mockVisionHelper =mock(VisionHelper.class);
          messenger.send(mockResource);
          messenger.send(mockVisionHelper);
          messenger.send(messageVO);
          messenger.closeMessenger();

          //doReturn(null).when(mockResource).getResourcesAttr();

          
        } catch (Exception e) {
        }
      }
    };
    th.start();
    Thread.sleep(100); // a bit of hack
    Messenger messenger = new Messenger("127.0.0.1", 12345);
    BufferedReader bufferedReader = mock(BufferedReader.class);
    PrintStream out = mock(PrintStream.class);
    //out.println("init player");
    Player player = new Player("a", messenger, bufferedReader, out, 3);
    //out.println("init");
    player.setUpInitialMap();
    player.setUsername("a");
    assertEquals("a", player.username);
    assertEquals(null, player.getColor());
    GameMap gameMap = new GameMap(2);
    //player.setupResourceHelper();

    //test doOnePlacement
    Placement placement =mock(Placement.class);
    Territory territory = new Territory("Oz", "Green");
    territory.getMyUnits().get("soldier").setNum(1);
    doReturn("soldier").when(placement).getUnit();
    doReturn(3).when(placement).getUnitNum();
    doReturn(territory).when(placement).getTerritory();
    player.doOnePlacement(placement);
    assertEquals(player.unitNumber, 0);

    //test readMoveAction
    VisionHelper mockVisionHelper=mock(VisionHelper.class);
    player.playerActionHelper.visionHelpers.put(player.color,mockVisionHelper);
    assertThrows(IllegalArgumentException.class,()->player.readActionContent("mv", "1", "2" ,"soldier", 1));
    PlayerActionHelper mockActionHelper=mock(PlayerActionHelper.class);
    player.playerActionHelper=mockActionHelper;
    doReturn(null).when(mockActionHelper).doCheckRehearser(any());
    assertNull(player.playerActionHelper.getHeadAction());

    //test readMoveSpyAction
    GameMap gameMapAction = new GameMap(2);
    Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
    Map<String, Territory> my_territories = gameMap.getMyTerritories();
    for (String territory_name : territory_group.get(0)) {
      my_territories.get(territory_name).setOwner("Red");
    }
    player.myMap=gameMapAction;
    player.color="Red";
    player.playerActionHelper.visionHelpers=new HashMap<>();
    player.playerActionHelper.visionHelpers.put("Red",mockVisionHelper);
    assertDoesNotThrow(()->player.readActionContent("ms", "1", "2" ,"soldier", 1));
    assertDoesNotThrow(()->player.readActionContent("us", "1", "2" ,"soldier", 1));
    assertDoesNotThrow(()->player.readActionContent("uc", "1", "2" ,"soldier", 1));
    assertDoesNotThrow(()->player.readActionContent("oc", "1", "2" ,"soldier", 1));



    //test readUpgradeSpyAction

    //test readUpgradeCloakAction

    //test readOrderCloakAction

    //test sendMessageVoToServer
    Messenger mockmessenger=mock(Messenger.class);
    player.messenger=mockmessenger;
    doNothing().when(mockmessenger).send(any());
    player.sendMessageVoToServer("error", -1, null, null, null);

    //test updateGameMapAfterPlacement
    Player spyPlayer3=spy(player);
    MessageVO messageVOTest = new MessageVO("2", new GameMap(2));
    doReturn(messageVOTest).when(mockmessenger).recv();
    GameMap mockMap=mock(GameMap.class);
    // doNothing().when(mock)
    doNothing().when(mockMap).updateMyTerritories(any(),anyString());
    player.myMap=mockMap;
    spyPlayer3.updateGameMapAfterPlacementPhase();
    
    //test tryJoinRoom
    Player spyPlayer2=spy(player);
    doNothing().when(spyPlayer2).sendMessageVoToServer("join", -1, null, player.username, "");
    HashSet<String> response=new HashSet<String>();
    doReturn(response).when(mockmessenger).recv();
    assertEquals(response, player.tryJoinRoom());

    //test doCreateRoom
    assertEquals("",player.doCreateRoom("", "2"));
    doReturn(false).when(mockmessenger).recv();
    assertEquals("Room name existed!", player.doCreateRoom("a", "2"));
    doReturn(true).when(mockmessenger).recv();
    player.doCreateRoom("a", "2");
    assertEquals("a", player.currentRoomName);

    //test doLoginSignup
    player.mode="";
    
    assertThrows( EOFException.class,()->player.doLoginSignup("a", "b"));

    MessageVO mockVo=mock(MessageVO.class);
    doReturn(mockVo).when(mockmessenger).recv();
    player.mode="l";
    assertEquals(mockVo, player.doLoginSignup("a", "b"));
    //MessageVO testMessageVO=new MessageVO("", Status.NONE);
    //assertEquals(player.doLoginSignup("a", "b").getStatus(), testMessageVO.getStatus());
    player.mode="s";
    assertEquals(mockVo, player.doLoginSignup("a", "b"));


    //test EndGameDetection
    EndGameDetection endGameDetection = mock(EndGameDetection.class);
    Set<String> set = mock(Set.class);
    doReturn(set).when(endGameDetection).getActivePlayer(new GameMap(2));
    assertEquals(false, player.checkGameEnd());
    assertEquals(true, player.checkGameLost());
    messenger.closeMessenger();

    //test getScore
    PlayerService mockService=mock(PlayerService.class);
    doReturn(1).when(mockService).playerScore(anyString());
    player.playerservice=mockService;
    assertEquals(1, player.getScore());
  }

}
