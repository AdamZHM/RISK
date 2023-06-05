package edu.duke.ece651.risc.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import edu.duke.ece651.risc.shared.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import edu.duke.ece651.risc.client.Player;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class SelectRoomControllerTest {
  public Player current_player;
  public HashSet<String> availableRoomName;
  public String mode;
  private SelectRoomController selectRoomController;

  @Start
  private void start(Stage stage) throws IOException, ClassNotFoundException {
    availableRoomName = new HashSet<>();
    current_player = new Player("test", null, null, System.out, 2);

    Map<String, VisionHelper> mockVisions = mock(Map.class);
    Map<String, String> mockVisionInfo = mock(Map.class);
    doReturn("<No Info Available>\n").when(mockVisionInfo).get(any());
    VisionHelper mockVision = mock(VisionHelper.class);
    mockVision.visionInfo = mockVisionInfo;
    doReturn(mockVision).when(mockVisions).get(any());

    Map<String, ResourceHelper> mockHelpers = mock(Map.class);
    doReturn(new ResourceHelper()).when(mockHelpers).get(any());
    // current_player.messenger = mockMessenger;

    current_player.myMap = getMapForAttackTest();
    current_player.playerActionHelper = new PlayerActionHelper(current_player.myMap);
    current_player.playerActionHelper.visionHelpers = mockVisions;
    current_player.playerActionHelper.resourceHelpers = mockHelpers;

    selectRoomController = new SelectRoomController(current_player, availableRoomName, "join");
    selectRoomController.chooseRoom = new ChoiceBox<>();
    selectRoomController.ErrorLabel = new Label();
  }

  @Test
  void testInitialize(FxRobot robot) {
    availableRoomName.add("abc");
    selectRoomController.availableRoomName = availableRoomName;
    selectRoomController.initialize();
    assertEquals(1, availableRoomName.size());
    assertEquals("join", selectRoomController.mode);
  }

  @Test
  void testReturnToMenu(FxRobot robot) throws IOException, ClassNotFoundException {

    Messenger messenger = mock(Messenger.class);
    MessageVO messageVO = new MessageVO("message", -1, "Red", new GameMap(2), null, 2, -1, "roomName", "userName",
            "password",
            null,
            Status.TO_PLACEMENT);

    // VisionHelper mockedVisionHelper = mock(VisionHelper.class);
    // Map<String, String> mockVisionInfo = mock(Map.class);
    // doReturn("<No Info Available>\n").when(mockVisionInfo).get(any());
    // mockedVisionHelper.visionInfo = mockVisionInfo;
    // ResourceHelper mockedResourceHelper = mock(ResourceHelper.class);
    doReturn(messageVO).when(messenger).recv();
    // doReturn(messageVO).doReturn(mockedResourceHelper).doReturn(mockedVisionHelper).doReturn(messageVO).doReturn(mockedResourceHelper).doReturn(mockedVisionHelper).when(messenger).recv();
    selectRoomController.current_player.messenger = messenger;

    Node mockNode = mock(Node.class);
    Scene scene = mock(Scene.class);
    when(mockNode.getScene()).thenReturn(scene);
    Stage stage = mock(Stage.class);
    when(scene.getWindow()).thenReturn(stage);
    MouseEvent event = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
        true, true, true, true, true, true, null);

    event = event.copyFor(mockNode, mockNode);
    selectRoomController.returnToMenu(event);
    WaitForAsyncUtils.waitForFxEvents();
  }

  @Test
  void testSelectRoomClicked() throws ClassNotFoundException, IOException {
    selectRoomController.chooseRoom = mock(ChoiceBox.class);
    doReturn("room").when(selectRoomController.chooseRoom).getValue();

    Messenger messenger = mock(Messenger.class);
    MessageVO messageVO = new MessageVO("message", -1, "Red", new GameMap(2), null, 2, -1, "roomName", "userName",
        "password",
        null,
        Status.TO_PLACEMENT);

    VisionHelper mockedVisionHelper = mock(VisionHelper.class);
    Map<String, String> mockVisionInfo = mock(Map.class);
    doReturn("<No Info Available>\n").when(mockVisionInfo).get(any());
    mockedVisionHelper.visionInfo = mockVisionInfo;
    ResourceHelper mockedResourceHelper = mock(ResourceHelper.class);
    doReturn(messageVO).doReturn(mockedResourceHelper).doReturn(mockedVisionHelper).doReturn(messageVO).doReturn(mockedResourceHelper).doReturn(mockedVisionHelper).when(messenger).recv();
    selectRoomController.current_player.messenger = messenger;
    Node mockNode = mock(Node.class);
    Scene scene = mock(Scene.class);
    when(mockNode.getScene()).thenReturn(scene);
    Stage stage = mock(Stage.class);
    when(scene.getWindow()).thenReturn(stage);
    ActionEvent event = new ActionEvent();
    event = event.copyFor(mockNode, mockNode);

    selectRoomController.selectRoomClicked(event);
    WaitForAsyncUtils.waitForFxEvents();


    doReturn(null).when(selectRoomController.chooseRoom).getValue();
    selectRoomController.selectRoomClicked(event);
    doReturn("null").when(selectRoomController.chooseRoom).getValue();
    selectRoomController.mode = "else";
    selectRoomController.selectRoomClicked(event);
    assertEquals(false, selectRoomController.ErrorLabel.isVisible());
  }

  private GameMap getMapForAttackTest() {
    GameMap map = new GameMap(3);
    Map<String, Territory> myTerritories = map.getMyTerritories();

    myTerritories.get("Scadrial").setOwner("Player1");
    Territory scadrial = myTerritories.get("Scadrial");
    scadrial.getUnitByName("warrior").setNum(3);
    scadrial.getUnitByName("knight").setNum(5);

    myTerritories.get("Mordor").setOwner("Player2");
    myTerritories.get("Mordor").getUnitByName("warrior").setNum(4);
    myTerritories.get("Mordor").getUnitByName("knight").setNum(4);

    return map;
  }
}