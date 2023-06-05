package edu.duke.ece651.risc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.HashMap;

import edu.duke.ece651.risc.client.Player;

import edu.duke.ece651.risc.shared.vo.MessageVO;
import edu.duke.ece651.risc.shared.Utils.Status;

public class LoginController {
    // Player object initialize
    public Player current_player;

    @FXML
    ResourceBundle resources;

    @FXML
    URL location;

    @FXML
    ImageView BG;

    @FXML
    ImageView backBtn;

    @FXML
    PasswordField Password;

    @FXML
    TextField Username;

    @FXML
    AnchorPane UsernamePassword;

    @FXML
    Button loginBtn;

    @FXML
    Button signupBtn;

    @FXML
    Label msg;

    /**
     * This function will be executed only when Button 'backBtn' has been clicked. Action panel will be hidden.
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML
    void enterUserInfo(KeyEvent event) throws IOException, ClassNotFoundException {
        // It will only function when pressing enter key
        if(event.getCode() == KeyCode.ENTER){
            // Retrieve username and update the player object
            current_player.setUsername(Username.getText());
            // Conduct login/signup process and receive the response from theserver
            MessageVO messageVO= current_player.doLoginSignup(Username.getText(), Password.getText());
            
            // Login successfully, enter the menu scene
            if(messageVO.getStatus() == Status.LOGINSUCCESS){
                // setup the menu scene
                URL xmlResource = getClass().getResource("/ui/menu.fxml");		
                FXMLLoader loader = new FXMLLoader(xmlResource);

                HashMap<Class<?>,Object> controllers = new HashMap<>();
                controllers.put(MenuController.class, new MenuController(current_player));
                loader.setControllerFactory((c) -> {
                    return controllers.get(c);
                });
            
                AnchorPane gp = loader.load();

                Node node = (Node) event.getSource();
                Stage menuStage = (Stage) node.getScene().getWindow();

                Scene scene = new Scene(gp, 1280, 800);
                menuStage.setScene(scene);
                menuStage.show();

                // clear up the input fields
                Username.clear();
                Password.clear();
            }
            // Sighup/login success, hide the input username and password field
            else if (messageVO.getStatus() != Status.NONE){
                loginBtn.setVisible(true);
                signupBtn.setVisible(true);
                UsernamePassword.setVisible(false);
                msg.setVisible(false);
                Username.clear();
                Password.clear();
            }
            else{ // When there is any type of error during sighup/login, display the error message
                msg.setVisible(true);
                msg.setText(messageVO.getMessage());
                Password.clear();
            }
        }
    }

    /**
     * This function will be executed only when Button 'loginBtn' has been clicked. Input fields will be displayed.
     * Also, set the mode as 'l' for the purpose of communicating with server. Distinguish whether this is login or sighup process
     */
    @FXML
    void loginPressed(ActionEvent event) {
        loginBtn.setVisible(false);
        signupBtn.setVisible(false);
        UsernamePassword.setVisible(true);
        current_player.mode = "l";
    }

    /**
     * This function will be executed only when Button 'signupBtn' has been clicked. Input fields will be displayed.
     * Also, set the mode as 's' for the purpose of communicating with server. Distinguish whether this is login or sighup process
     */
    @FXML
    void signupPressed(ActionEvent event) {
        loginBtn.setVisible(false);
        signupBtn.setVisible(false);
        UsernamePassword.setVisible(true);
        current_player.mode = "s";
    }
    
    /**
     * This function will be executed only when Button 'backBtn' has been clicked. The scene will return to its initial status
     */
    @FXML
    void returnToStart(MouseEvent event) {
        loginBtn.setVisible(true);
        signupBtn.setVisible(true);
        UsernamePassword.setVisible(false);
        msg.setVisible(false);
        Username.clear();
        Password.clear();
    }

    @FXML
    void initialize() throws ClassNotFoundException, IOException {
    }

    /**
     * LoginController constructor
     * @param current_player
     */
    public LoginController(Player current_player) {
        this.current_player = current_player;
    }

}
