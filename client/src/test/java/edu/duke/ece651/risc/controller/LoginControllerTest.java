package edu.duke.ece651.risc.controller;

import edu.duke.ece651.risc.client.Player;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {

    public Player current_player;
    private Button loginBtn;
    private LoginController loginController;

    @Start
    private void start(Stage stage) throws IOException, ClassNotFoundException {
        loginBtn = new Button();
        loginBtn.setId("loginBtn");
        // String username, Messenger messenger, BufferedReader inputReader, PrintStream out, int unitNumber
        current_player = new Player("test", null, null, null, 2);
        loginController = new LoginController(current_player);
        loginController.loginBtn = loginBtn;
        loginController.signupBtn = new Button();
        loginController.UsernamePassword = new AnchorPane();
        loginController.Username = new TextField();
        loginController.Password = new PasswordField();
        loginController.msg = new Label();
    }

    @Test
    void test_login_pressed(FxRobot robot) {
        Platform.runLater(()->{
            loginController.loginPressed(new ActionEvent(loginBtn, null));
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(false, loginController.loginBtn.isVisible());
        assertEquals(false, loginController.signupBtn.isVisible());
        assertEquals(true, loginController.UsernamePassword.isVisible());
        assertEquals(true, loginController.Username.isVisible());
        assertEquals(true, loginController.Password.isVisible());
        assertEquals(true, current_player.mode.equals("l"));
    }

    @Test
    void test_sign_up_pressed(FxRobot robot) {
        Platform.runLater(()->{
            loginController.signupPressed(new ActionEvent(loginBtn, null));
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(false, loginController.loginBtn.isVisible());
        assertEquals(false, loginController.signupBtn.isVisible());
        assertEquals(true, loginController.UsernamePassword.isVisible());
        assertEquals(true, loginController.Username.isVisible());
        assertEquals(true, loginController.Password.isVisible());
        assertEquals(true, current_player.mode.equals("s"));
    }

    @Test
    void test_return_to_start(FxRobot robot) {
        Platform.runLater(()->{
            loginController.returnToStart(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(true, loginController.loginBtn.isVisible());
        assertEquals(true, loginController.signupBtn.isVisible());
        assertEquals(false, loginController.UsernamePassword.isVisible());
        assertEquals(false, loginController.msg.isVisible());
        FxAssert.verifyThat(loginController.Username, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(loginController.Password, TextInputControlMatchers.hasText(""));
    }

    @Test
    void test_enter_user_info(FxRobot robot) throws IOException, ClassNotFoundException {
        MessageVO messageVO = mock(MessageVO.class);
        doReturn(Status.LOGINSUCCESS).when(messageVO).getStatus();
        Player current_player = mock(Player.class);
        doNothing().when(current_player).setUsername(any());
        doReturn(messageVO).when(current_player).doLoginSignup(any(), any());
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, new String(new int[]{}, 0, 0), "", KeyCode.ENTER,
                false, false, false, false);

        Node mockNode = mock(Node.class);
        Scene scene = mock(Scene.class);
        when(mockNode.getScene()).thenReturn(scene);
        Stage stage = mock(Stage.class);
        when(scene.getWindow()).thenReturn(stage);

        event = event.copyFor(mockNode, mockNode);

        loginController.current_player = current_player;
        loginController.enterUserInfo(event);

        FxAssert.verifyThat(loginController.Username, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(loginController.Password, TextInputControlMatchers.hasText(""));

        doReturn(Status.SIGNUPSUCCESS).when(messageVO).getStatus();
        // current_player.mode = "test";

        loginController.enterUserInfo(event);

        assertEquals(true, loginController.loginBtn.isVisible());
        assertEquals(true, loginController.signupBtn.isVisible());
        assertEquals(false, loginController.UsernamePassword.isVisible());
        assertEquals(false, loginController.msg.isVisible());
        FxAssert.verifyThat(loginController.Username, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(loginController.Password, TextInputControlMatchers.hasText(""));

        doReturn(Status.NONE).when(messageVO).getStatus();
        // current_player.mode = "l";

        loginController.enterUserInfo(event);

        assertEquals(true, loginController.msg.isVisible());

        event = new KeyEvent(KeyEvent.KEY_TYPED, new String(new int[]{}, 0, 0), "", KeyCode.ENTER,
                false, false, false, false);

        loginController.enterUserInfo(event);
    }
}