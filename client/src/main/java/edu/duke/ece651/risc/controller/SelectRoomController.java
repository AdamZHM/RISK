package edu.duke.ece651.risc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;

import edu.duke.ece651.risc.client.Player;

public class SelectRoomController {
    // Initialize player object, the list of available room and whether this is join
    // or resume action
    public Player current_player;
    public HashSet<String> availableRoomName;
    public String mode;

    @FXML
    ResourceBundle resources;

    @FXML
    URL location;

    @FXML
    Label ErrorLabel;

    @FXML
    ImageView backBtn;

    @FXML
    Label menuPrompt;

    @FXML
    ChoiceBox<String> chooseRoom;

    @FXML
    Button sendBtn;

    /**
     * This function will be executed only when Button 'backBtn' has been clicked.
     * The scene will be set to the original menu
     * 
     */
    @FXML
    void returnToMenu(MouseEvent event) throws IOException {
        // Send messege to server
        this.current_player.messenger.send("break");
        
        // setup the menu scene
        URL xmlResource = getClass().getResource("/ui/menu.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
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
    }

    /**
     * This function will be executed only when Button 'sendBtn' has been clicked.
     * The window will change scene to game map
     * 
     */
    @FXML
    void selectRoomClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        // Retrieve the room name from choiceBox
        String BtnText = chooseRoom.getValue();

        // Display the error messege if no room is selected
        if (BtnText == null) {
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Please select one room!");
            return;
        }

        // update the roomname to player object
        this.current_player.currentRoomName = BtnText;
        // send the selected room name to the server
        this.current_player.messenger.send(BtnText);
        if (this.mode.equals("join")) {
            this.current_player.unitNumber = 10; // reset unit number
        }

        // set the visibility of errorlabel to false
        ErrorLabel.setVisible(false);

        // setup the map scene
        URL xmlResource = getClass().getResource("/ui/map.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(MapController.class, new MapController(current_player, this.mode));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        AnchorPane gp = loader.load();

        Node node = (Node) event.getSource();
        Stage menuStage = (Stage) node.getScene().getWindow();

        Scene scene = new Scene(gp, 1280, 800);
        menuStage.setScene(scene);
        menuStage.show();
    }

    /**
     * Initialize the choiceBox for selecting the available room
     */
    @FXML
    void initialize() {
        for (String roomName : this.availableRoomName) {
            chooseRoom.getItems().addAll(roomName);
        }
    }

    /**
     * SelectRoomController constructor
     * 
     * @param current_player
     * @param HashSet<String> the set of all availble room for display
     * @param mode,           flag for distinguishing whether this is signup or
     *                        login action
     */
    public SelectRoomController(Player current_player, HashSet<String> availableRoomName, String mode) {
        this.current_player = current_player;
        this.availableRoomName = availableRoomName;
        this.mode = mode;
    }
}
