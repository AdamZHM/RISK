package edu.duke.ece651.risc.controller;

import edu.duke.ece651.risc.client.Player;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class MenuControllerTestWithMoreThanZero {

    private MenuController menuController;

    @Start
    private void start(Stage stage) throws IOException, ClassNotFoundException {
        Player current_player = new Player("Player1", null, null, System.out, 2);
        Player mockPlayer = spy(current_player);
        doNothing().when(mockPlayer).displayGameMap();
        doNothing().when(mockPlayer).setUpInitialMap();


        Messenger mockMessenger = mock(Messenger.class);
        MessageVO mockVO = mock(MessageVO.class);
        HashSet<String> set = new HashSet<>();
        set.add("test");
        HashSet<String> mockSet = spy(set);
        doReturn(2).when(mockSet).size();
        when(mockMessenger.recv()).thenReturn(mockSet);

        Map<String, ResourceHelper> mockHelpers = mock(Map.class);
        doReturn(new ResourceHelper()).when(mockHelpers).get(any());

        Map<String, VisionHelper> mockVisions = mock(Map.class);
        Map<String, String> mockVisionInfo = mock(Map.class);
        doReturn("<No Info Available>\n").when(mockVisionInfo).get(any());
        VisionHelper mockVision = mock(VisionHelper.class);
        mockVision.visionInfo = mockVisionInfo;
        doReturn(mockVision).when(mockVisions).get(any());

        mockPlayer.messenger = mockMessenger;
        mockPlayer.myMap = getMapForAttackDisabled();

        mockPlayer.playerActionHelper = new PlayerActionHelper(mockPlayer.myMap);
        mockPlayer.playerActionHelper.visionHelpers = mockVisions;
        mockPlayer.playerActionHelper.resourceHelpers = mockHelpers;

        menuController = new MenuController(mockPlayer);
        menuController.createBtn = new Button();
        menuController.createRoomInfo = new AnchorPane();
        menuController.menuPrompt = new Label();
        menuController.createRoomInfo = new AnchorPane();
        menuController.enterRoomName = new TextField();
        menuController.enterCapacity = new ChoiceBox<>();
        menuController.createRoomPrompt = new Label();
    }

    @Test
    void join_room_pressed_greater_than_zero() throws IOException, ClassNotFoundException {
        ActionEvent event = new ActionEvent(menuController.createBtn, null);
        Node mockNode = mock(Node.class);
        Scene scene = mock(Scene.class);
        when(mockNode.getScene()).thenReturn(scene);
        Stage stage = mock(Stage.class);
        when(scene.getWindow()).thenReturn(stage);
        event = event.copyFor(mockNode, mockNode);

        menuController.joinRoomPressed(event);

        assertEquals(false, menuController.menuPrompt.isVisible());
    }

    @Test
    void test_create_submit_info_more_than_zero() throws IOException, ClassNotFoundException {
        ActionEvent event = new ActionEvent(menuController.createBtn, null);
        Node mockNode = mock(Node.class);
        Scene scene = mock(Scene.class);
        when(mockNode.getScene()).thenReturn(scene);
        Stage stage = mock(Stage.class);
        when(scene.getWindow()).thenReturn(stage);
        event = event.copyFor(mockNode, mockNode);

        menuController.enterRoomName.setText("room1");
        menuController.enterCapacity.setValue("test");

        menuController.submitCreateInfo(event);
    }

    @Test
    void test_resume_pressed() throws IOException, ClassNotFoundException {
        ActionEvent event = new ActionEvent(menuController.createBtn, null);
        Node mockNode = mock(Node.class);
        Scene scene = mock(Scene.class);
        when(mockNode.getScene()).thenReturn(scene);
        Stage stage = mock(Stage.class);
        when(scene.getWindow()).thenReturn(stage);
        event = event.copyFor(mockNode, mockNode);

        menuController.resumePressed(event);
        assertEquals(false, menuController.menuPrompt.isVisible());
    }

    private GameMap getMapForAttackDisabled() {
        GameMap map = new GameMap(4);
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