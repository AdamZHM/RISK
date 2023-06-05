package edu.duke.ece651.risc.controller;

import edu.duke.ece651.risc.client.Player;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.Entity.Vision;
import edu.duke.ece651.risc.shared.Service.PlayerService;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class MapControllerTest {

    private MapController mapController;
    public Messenger mockMessenger;

    @Start
    private void start(Stage stage) throws IOException, ClassNotFoundException {
        Player current_player = new Player("Player1", null, null, null, 2);
        Player mockPlayer = spy(current_player);
        doNothing().when(mockPlayer).displayGameMap();
        doNothing().when(mockPlayer).setUpInitialMap();

        mockMessenger = mock(Messenger.class);
        MessageVO mockVO = mock(MessageVO.class);
        when(mockMessenger.recv()).thenReturn(Status.TO_PLACEMENT);

        Map<String, ResourceHelper> mockHelpers = mock(Map.class);
        doReturn(new ResourceHelper()).when(mockHelpers).get(any());
        Map<String, VisionHelper> mockVisions = mock(Map.class);
        Map<String, String> mockVisionInfo = mock(Map.class);
        doReturn("<No Info Available>\n").when(mockVisionInfo).get(any());
        VisionHelper mockVision = mock(VisionHelper.class);
        mockVision.visionInfo = mockVisionInfo;
        doReturn(mockVision).when(mockVisions).get(any());
        mockPlayer.messenger = mockMessenger;
        GameMap map = getMapForAttackTest();
        GameMap mockMap = spy(map);
        doReturn(map.getTerritoryByName("Scadrial")).when(mockMap).getTerritoryByName(any());
        mockPlayer.myMap = mockMap;
        mockPlayer.playerActionHelper = new PlayerActionHelper(mockPlayer.myMap);
        mockPlayer.playerActionHelper.visionHelpers = mockVisions;
        mockPlayer.playerActionHelper.resourceHelpers = mockHelpers;
        PlayerService mockService = mock(PlayerService.class);
        doReturn(1).when(mockService).playerScore(any());
        mockPlayer.playerservice = mockService;

//        Rectangle mockRec = mock(Rectangle.class);
//        doNothing().when(mockRec).setStyle(any());
//        HashMap<String, Rectangle> mockButton = mock(HashMap.class);
//        doReturn(mockRec).when(mockButton).get(any());

        // System.out.println(mockPlayer.myMap);

        mapController = new MapController(mockPlayer, "resume");
        mapController.actionType = new ChoiceBox<>();
        mapController.dst = new ChoiceBox<>();
        mapController.NarniaBtn = new Button();
        mapController.MedkemiaBtn = new Button();
        mapController.OzBtn = new Button();
        mapController.GondorBtn = new Button();
        mapController.ElantrisBtn = new Button();
        mapController.ScadriaBtn = new Button();
        mapController.MordorBtn = new Button();
        mapController.RosharBtn = new Button();
        mapController.HogwartsBtn = new Button();
        mapController.DukeBtn = new Button();
        // mapController.matchBtn = mockButton;
        mapController.NarniaFlag = new Rectangle();
        mapController.MidkemiaFlag = new Rectangle();
        mapController.OzFlag = new Rectangle();
        mapController.GondorFlag = new Rectangle();
        mapController.ElantrisFlag = new Rectangle();
        mapController.ScadrialFlag = new Rectangle();
        mapController.MordorFlag = new Rectangle();
        mapController.RosharFlag = new Rectangle();
        mapController.HogwartsFlag = new Rectangle();
        mapController.DukeFlag = new Rectangle();
        mapController.cloakLabel = new Label();
        mapController.SpyPromptLabel = new Label();
        // mapController.Duke_1 = new Line();
        // mapController.Duke_2 = new Line();
        // mapController.Hogwarts_4 = new Line();

        mapController.foodLabel = new Label();
        mapController.techLevelLabel = new Label();
        mapController.techResourceLabel = new Label();

        mapController.UnitNumText = new TextField();
        mapController.UnitTypeText = new ChoiceBox<>();
        mapController.actionPanel = new AnchorPane();

        mapController.ErrorPromptLabel = new Label();
        mapController.TerritoryNameLabel = new Label();
        mapController.ResourceLabel = new Label();
        mapController.PromptLabel = new Label();
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

    @Test
    void test_return_to_menu(FxRobot robot) throws IOException, ClassNotFoundException {
        Node mockNode = mock(Node.class);
        Scene scene = mock(Scene.class);
        when(mockNode.getScene()).thenReturn(scene);
        Stage stage = mock(Stage.class);
        when(scene.getWindow()).thenReturn(stage);
        MouseEvent event = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null);
        event = event.copyFor(mockNode, mockNode);

        mapController.returnToMenu(event);
        assertEquals(null, mapController.actionType.getValue());
        assertEquals(null, mapController.dst.getValue());
        assertEquals(true, mapController.actionPanel.isVisible());
    }

    @Test
    void test_show_action_panel(FxRobot robot) {
        Platform.runLater(()->{
            mapController.showActionPanel(new ActionEvent(mapController.DukeBtn, null));
        });
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(mapController.UnitNumText, TextInputControlMatchers.hasText(""));
        // FxAssert.verifyThat(mapController.UnitTypeText, TextInputControlMatchers.hasText(""));
        assertEquals(null, mapController.actionType.getValue());
        assertEquals(null, mapController.dst.getValue());
        assertEquals(true, mapController.actionPanel.isVisible());
    }

    @Test
    void test_initialize_3_players(FxRobot robot) throws IOException, ClassNotFoundException {
        mapController.initialize();

        when(mockMessenger.recv()).thenReturn(Status.NONE);
        mapController.initialize();

        mapController.mode = "create";
        mapController.initialize();

        mapController.mode = "else";
        mapController.initialize();

        assertEquals(false, mapController.DukeBtn.isVisible());
        // assertEquals(false, mapController.Hogwarts_4.isVisible());
        // assertEquals(false, mapController.Duke_1.isVisible());
        // assertEquals(false, mapController.Duke_2.isVisible());
    }

    @Test
    void test_commit_action(FxRobot robot) {
        Platform.runLater(()->{
            try {
                mapController.commitAction(new ActionEvent(mapController.DukeBtn, null));
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

}