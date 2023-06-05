package edu.duke.ece651.risc.controller;

import edu.duke.ece651.risc.client.Player;
import edu.duke.ece651.risc.shared.Messenger;
import edu.duke.ece651.risc.shared.ResourceHelper;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

@ExtendWith(ApplicationExtension.class)
public class MenuControllerTest {

    private MenuController menuController;

    @Start
    private void start(Stage stage) throws IOException, ClassNotFoundException {
        Player current_player = new Player("Player1", null, null, System.out, 2);
        Player mockPlayer = spy(current_player);
        doNothing().when(mockPlayer).displayGameMap();
        doNothing().when(mockPlayer).setUpInitialMap();

        Messenger mockMessenger = mock(Messenger.class);
        // MessageVO mockVO = mock(MessageVO.class);
        when(mockMessenger.recv()).thenReturn(new HashSet());

        Map<String, ResourceHelper> mockHelpers = mock(Map.class);
        doReturn(new ResourceHelper()).when(mockHelpers).get(any());
        mockPlayer.messenger = mockMessenger;

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
    void create_room_pressed() {
        Platform.runLater(()->{
            menuController.createRoomPressed(new ActionEvent(menuController.createBtn, null));
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(true, menuController.createRoomInfo.isVisible());

    }

    @Test
    void join_room_pressed() {
        Platform.runLater(()->{
            try {
                menuController.joinRoomPressed(new ActionEvent(menuController.createBtn, null));
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(true, menuController.menuPrompt.isVisible());
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
        assertEquals(true, menuController.menuPrompt.isVisible());
        FxAssert.verifyThat(menuController.menuPrompt, LabeledMatchers.hasText("No available room to resume"));
    }

    @Test
    void test_return_to_start() {
        MouseEvent event = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null);
        Platform.runLater(()-> {
            try {
                menuController.returnToStart(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(false, menuController.createRoomInfo.isVisible());
        FxAssert.verifyThat(menuController.enterRoomName, TextInputControlMatchers.hasText(""));
    }

    @Test
    void test_create_submit_info() throws IOException, ClassNotFoundException {
        ActionEvent event = new ActionEvent(menuController.createBtn, null);
        Node mockNode = mock(Node.class);
        Scene scene = mock(Scene.class);
        when(mockNode.getScene()).thenReturn(scene);
        Stage stage = mock(Stage.class);
        when(scene.getWindow()).thenReturn(stage);
        event = event.copyFor(mockNode, mockNode);

        menuController.submitCreateInfo(event);
    }
}
