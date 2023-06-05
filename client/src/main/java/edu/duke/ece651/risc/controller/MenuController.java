package edu.duke.ece651.risc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import edu.duke.ece651.risc.client.Player;

public class MenuController {
    // Player object initialize
    public Player current_player;

    @FXML
    public ResourceBundle resources;

    @FXML
    public URL location;

    @FXML
    public ImageView BG;

    @FXML
    public Label CreateRoomLabel;

    @FXML
    public ImageView CreateRoomPanel;

    @FXML
    public ImageView JoinRoomImg;

    @FXML
    public Label JoinRoomLabel;

    @FXML
    public ImageView ResumeImg;

    @FXML
    public Label ResumeLabel;

    @FXML
    public ImageView backBtn;

    @FXML
    public Button createBtn;

    @FXML
    public ImageView createRoomImg;

    @FXML
    public AnchorPane createRoomInfo;

    @FXML
    public Label createRoomPrompt;

    @FXML
    public ChoiceBox<String> enterCapacity;

    @FXML
    public TextField enterRoomName;

    @FXML
    public Button joinBtn;

    @FXML
    public Label menuPrompt;

    @FXML
    public Button resumeBtn;

    @FXML
    public Label score;

    @FXML
    public Button submitRoomInfo;

    /**
     * This function will be executed only when Button 'createBtn' has been clicked.
     * The input fields for creating a new room will be displayed 
     * 
     */
    @FXML
    void createRoomPressed(ActionEvent event) {
        createRoomInfo.setVisible(true);
    }

    /**
     * This function will be executed only when Button 'joinBtn' has been clicked.
     * The function will first detect whether there is available room to join, if yes, then change to the selectRoom scene
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML
    void joinRoomPressed(ActionEvent event) throws IOException, ClassNotFoundException {
        // Receive the list of available room from the server
        HashSet<String> availableRoomName = this.current_player.tryJoinRoom();

        // If there is no available room, display the error messege
        if(availableRoomName.size() == 0){
            menuPrompt.setVisible(true);
            menuPrompt.setText("No available room to join");
        }
        else{
            // setup the select room scene
            URL xmlResource = getClass().getResource("/ui/selectRoom.fxml");		
            FXMLLoader loader = new FXMLLoader(xmlResource);

            HashMap<Class<?>,Object> controllers = new HashMap<>();
            controllers.put(SelectRoomController.class, new SelectRoomController(current_player, availableRoomName, "join"));
            loader.setControllerFactory((c) -> {
                return controllers.get(c);
            });
        
            AnchorPane gp = loader.load();

            Node node = (Node) event.getSource();
            Stage menuStage = (Stage) node.getScene().getWindow();

            Scene scene = new Scene(gp, 1280, 800);
            menuStage.setScene(scene);
            menuStage.show();

            // Set the visibility of prompt to false
            menuPrompt.setVisible(false);
        }
    }

    /**
     * This function will be executed only when Button 'resumeBtn' has been clicked.
     * The function will first detect whether there is available room to resume, if yes, then change to the selectRoom scene
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML
    void resumePressed(ActionEvent event) throws IOException, ClassNotFoundException  {
        // Receive the list of available room from the server
        HashSet<String> availableRoomName = this.current_player.tryResumeRoom();

        // If there is no available room, display the error messege
        if(availableRoomName.size() == 0){
            menuPrompt.setVisible(true);
            menuPrompt.setText("No available room to resume");
        }
        else{
            // setup the select room scene
            URL xmlResource = getClass().getResource("/ui/selectRoom.fxml");		
            FXMLLoader loader = new FXMLLoader(xmlResource);

            HashMap<Class<?>,Object> controllers = new HashMap<>();
            controllers.put(SelectRoomController.class, new SelectRoomController(current_player, availableRoomName, "resume"));
            loader.setControllerFactory((c) -> {
                return controllers.get(c);
            });
        
            AnchorPane gp = loader.load();

            Node node = (Node) event.getSource();
            Stage menuStage = (Stage) node.getScene().getWindow();

            Scene scene = new Scene(gp, 1280, 800);
            menuStage.setScene(scene);
            menuStage.show();

            // Set the visibility of prompt to false
            menuPrompt.setVisible(false);
        }
    }

    /**
     * This function will be executed only when Button 'backBtn' has been clicked.
     * Return the scene back to the initial menu scene
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML
    void returnToStart(MouseEvent event) throws IOException {
        if(createRoomInfo.isVisible()){
            createRoomInfo.setVisible(false);
            enterRoomName.clear();
        }
        else{
            // setup the map scene
            URL xmlResource = getClass().getResource("/ui/login.fxml");		
            FXMLLoader loader = new FXMLLoader(xmlResource);

            HashMap<Class<?>,Object> controllers = new HashMap<>();
            controllers.put(LoginController.class, new LoginController(current_player));
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
    }

    /**
     * This function will be executed only when Button 'submitRoomInfo' has been clicked.
     * The function will first detect whether the required input information is missing
     * Then change to scene to game room if the server's response contains no error messgeg
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @FXML
    void submitCreateInfo(ActionEvent event)throws IOException, ClassNotFoundException {
        // Receive room capacity and room name from choiceBox and textfield
        String roomCapacity = (String) enterCapacity.getValue();
        String roomName = enterRoomName.getText();

        // Display error messege when the required information is missing
        if(roomCapacity == null || roomName.equals("") || roomName == null){
            createRoomPrompt.setText("Room name and capacity CAN NOT be null!");
            createRoomPrompt.setVisible(true);
            return;
        }

        // Receive the response of creating room from server
        String create_result = this.current_player.doCreateRoom(roomName, roomCapacity);
        
        // If there is error messege, display the error messege
        if(!create_result.equals("")){
            createRoomPrompt.setText(create_result);
            createRoomPrompt.setVisible(true);
        }
        else{
            // Reset the createRoomInfo panel and clear the text field
            createRoomInfo.setVisible(false);
            enterRoomName.clear();

            // setup the map scene
            URL xmlResource = getClass().getResource("/ui/map.fxml");		
            FXMLLoader loader = new FXMLLoader(xmlResource);

            HashMap<Class<?>,Object> controllers = new HashMap<>();
            controllers.put(MapController.class, new MapController(current_player, "create"));
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
    }

    /**
     * Initialize the choiceBox for selecting the capacity of the room
     */
    @FXML
    void initialize() { 
        ObservableList<String> roomNum = FXCollections.observableArrayList("2", "3", "4", "5");
        enterCapacity.getItems().addAll(roomNum);

        Integer elo_score = this.current_player.getScore();
        score.setText("Player: " + this.current_player.username + ", your current score is " + elo_score.toString());
    }

    /**
     * MenuController constructor
     * @param current_player
     */
    public MenuController(Player current_player) {
        this.current_player = current_player;
    }
}
