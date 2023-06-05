package edu.duke.ece651.risc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.vo.MessageVO;
import edu.duke.ece651.risc.shared.Placement;
import edu.duke.ece651.risc.client.Player;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.Unit;
import edu.duke.ece651.risc.shared.VisionHelper;
import edu.duke.ece651.risc.shared.ResourceHelper;
import edu.duke.ece651.risc.shared.Utils.Status;

public class MapController {
    public Player current_player;
    public String Src;
    public int roomPlayerNum;
    public String mode;
    public HashMap<String, Rectangle> matchBtn;
    public HashMap<String, String> matchaction;

    @FXML
    public Button DukeBtn;

    @FXML
    public Rectangle DukeFlag;

    @FXML
    public Button ElantrisBtn;

    @FXML
    public Rectangle ElantrisFlag;

    @FXML
    public Label ErrorPromptLabel;

    @FXML
    public Button GondorBtn;

    @FXML
    public Rectangle GondorFlag;

    @FXML
    public Button HogwartsBtn;

    @FXML
    public Rectangle HogwartsFlag;

    @FXML
    public Button MedkemiaBtn;

    @FXML
    public Rectangle MidkemiaFlag;

    @FXML
    public Button MordorBtn;

    @FXML
    public Rectangle MordorFlag;

    @FXML
    public Button NarniaBtn;

    @FXML
    public Rectangle NarniaFlag;

    @FXML
    public Button OzBtn;

    @FXML
    public Rectangle OzFlag;

    @FXML
    public Label PromptLabel;

    @FXML
    public Label ResourceLabel;

    @FXML
    public Button RosharBtn;

    @FXML
    public Rectangle RosharFlag;

    @FXML
    public Button ScadriaBtn;

    @FXML
    public Rectangle ScadrialFlag;

    @FXML
    public Label TerritoryNameLabel;

    @FXML
    public TextField UnitNumText;

    @FXML
    public ChoiceBox<String> UnitTypeText;

    @FXML
    public AnchorPane actionPanel;

    @FXML
    public ChoiceBox<String> actionType;

    @FXML
    public Button backBtn;

    @FXML
    public Button commitBtn;

    @FXML
    public ChoiceBox<String> dst;

    @FXML
    public Label foodLabel;

    @FXML
    public AnchorPane gameMap;

    @FXML
    public ImageView mapBG;

    @FXML
    public HBox mapTitle;

    @FXML
    public ImageView returnBtn;

    @FXML
    public Label techLevelLabel;

    @FXML
    public Label techResourceLabel;

    @FXML
    public Label SpyPromptLabel;

    @FXML
    public Label cloakLabel;

    @FXML
    public VBox territoryInfo;

    /**
     * This function will be executed only when Button 'backBtn' has been clicked. Action panel will be hidden.
     * 
     */
    @FXML
    void backToMap(ActionEvent event) {
        actionPanel.setVisible(false);
    }

    /**
     * This function will be executed only when choiceBox 'dst' has been clicked. The choices will be depending on the action type
     * 
     */
    @FXML
    void selectField(MouseEvent event) {
        // Receive the action type from 'actionType' choiceBox
        String choice_type = matchaction.get(actionType.getValue());

        // Initialize the choice box as the destination upgrade level
        if(choice_type.equals("uu")){
            ArrayList<String> unitLevelList = new ArrayList<>(
                        Arrays.asList("soldier", "warrior", "knight", 
                        "captain", "commander", "general", "king"));
                    
            this.initChoicebox(dst, unitLevelList);
        }
        // Initialize the choice box as the name of destination territory
        else{
            ArrayList<String> dstName = new ArrayList<>(
                this.current_player.myMap.getTerritoryNames());
            this.initChoicebox(dst, dstName);
        }
    }

     /**
     * This function will be executed only when button 'commitBtn' has been clicked.
     * This function will determine whether to send the corresponding action to the server
     * 
     */
    @FXML
    void commitAction(ActionEvent event) throws IOException, ClassNotFoundException {   
        // Processed the input data with the logic in placement stage     
        if(this.current_player.unitNumber > 0){
            String input = Src + "," + UnitTypeText.getValue() + "," + UnitNumText.getText();

            try{
                // Try to send the placement data to server and display the error messege when exceptions are thrown
                Placement p = new Placement(this.current_player.color, input, this.current_player.myMap);
                this.current_player.doOnePlacement(p);
                actionPanel.setVisible(false);
            }
            catch (IllegalArgumentException e){
                ErrorPromptLabel.setVisible(true);
                ErrorPromptLabel.setText(e.getMessage());
            }

            // Send the corresponding placement action to the server after the placement has been finished
            if(this.current_player.unitNumber == 0){
                Boolean flag = false;
                while (!flag) {
                    MessageVO messageVO = new MessageVO("placement", this.current_player.myMap);
                    this.current_player.messenger.send(messageVO); // send updated map to server
                    flag = (Boolean) this.current_player.messenger.recv();
                }
                // Update the player object with the information received from the server
                this.current_player.updateGameMapAfterPlacementPhase();
                // Initialize the choiceBox for action stage
                this.updateChoiceBox();
            }
        }
        else{ // When entering the action stage
            if(this.current_player.checkGameEnd()){
                // do nothing when the game has already ended.
            }
            else if(!this.current_player.checkGameLost()){
                // return the function if actionType choiceBox is empty
                if(actionType.getValue() == null){
                    this.updateTitleLabel();
                    return;
                }

                // Receive action type from the choiceBox
                String choice_type = matchaction.get(actionType.getValue());

                // if(choice_type == null){
                //     // Do nothing if the choiceBox is null
                // }
                // else if(choice_type.equals("commit")){
                if(choice_type.equals("commit")){
                    // Send the action data to the server and receive the ACK
                    Boolean flag = false;
                    while (!flag) {
                        MessageVO messageVO = new MessageVO("action", this.current_player.playerActionHelper.getHeadAction());
                        this.current_player.messenger.send(messageVO);
                        flag = (Boolean)  this.current_player.messenger.recv();
                    }
                    // Reset and update the ActionHelper and Resourcehelper function 
                    this.current_player.playerActionHelper.resetAction();
                    GameMap newGameMap = (GameMap) this.current_player.messenger.recv();
                    ResourceHelper newResourceHelper = (ResourceHelper) this.current_player.messenger.recv();
                    VisionHelper newVisionHelper =(VisionHelper) this.current_player.messenger.recv();
                    this.current_player.playerActionHelper.setupHelper(this.current_player.color, newResourceHelper,newVisionHelper);
                    this.current_player.myMap.updateMyTerritories(newGameMap, "");
                    
                    // Update and initialize the map and the input fields
                    this.updateDisplayMap();
                    this.current_player.displayGameMap();
                    this.updateChoiceBox();
                    this.initActionPanel();
                }
                else{
                    // When the input action isn't commit. E.g. move, attack...

                    // Receive the number of unit from text field
                    String numUnitVal = UnitNumText.getText();
                    if(numUnitVal.equals("")){
                        numUnitVal = "0";
                    }

                    try{
                        // Processed the input action using the input collected from textfields and choiceBoxes
                        if(choice_type.equals("uu")){
                            this.current_player.readActionContent(choice_type, Src, UnitTypeText.getValue(), dst.getValue(), Integer.parseInt(numUnitVal));
                        }
                        else{
                            this.current_player.readActionContent(choice_type, Src, dst.getValue(), UnitTypeText.getValue(), Integer.parseInt(numUnitVal));
                        }
                        actionPanel.setVisible(false);
                        this.initActionPanel();
                    }
                    catch(IOException|IllegalArgumentException e){
                        // Display the error messege if this action is invalid
                        this.initActionPanel();
                        actionPanel.setVisible(true);

                        ErrorPromptLabel.setVisible(true);
                        ErrorPromptLabel.setText(e.getMessage());
                    }
                    
                    this.current_player.myMap = this.current_player.playerActionHelper.getMap();
                    this.updateChoiceBox();
                }
            }
            else{ // Only receive the latest gamemap to futher watch the game process
                System.out.println("Watch game");
                this.current_player.playerActionHelper.resetAction();

                // Receiving the latest the gamemap and logic for disconnection
                boolean flag = false;
                while (!flag){
                    try {
                        GameMap newGameMap = (GameMap) this.current_player.messenger.recv();
                        flag = true;
                        this.current_player.myMap.updateMyTerritories(newGameMap, "");
                        this.updateDisplayMap();
                        this.current_player.displayGameMap();
                        this.updateChoiceBox();
                    } catch (Exception e) {
                        continue;
                    }
                }                
            }
        }
        // Update the title label using the latest gamemap
        this.updateTitleLabel();
    }

    /**
     * This function initialize the MapController class
     * Initialize the mapping between button and territory name, action type and action factory
     * 
     * @return a string that will be displayed
     */
    @FXML
    void returnToMenu(MouseEvent event) throws IOException, ClassNotFoundException {
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
    }

    /**
     * This function will activate the panel for input action commend 
     * Initialize input field and all the corresponding prompt. Including territory name, occupied units, 
     * resources and cost to neighbor territories. It will also display the game result if one player has lost or the winner is determined.
     * 
     * @return a string that will be displayed
     */
    @FXML
    void showActionPanel(ActionEvent event) {
        // Initialize the action panel
        this.initActionPanel();

        ErrorPromptLabel.setVisible(false);
        actionPanel.setVisible(true);

        // Receive the territory name from the textfield of a button
        Src = ((Button)event.getSource()).getText();

        // Initialize the Territoryname Label
        TerritoryNameLabel.setText(Src);
        // Initialize the Resource Label
        String msg = this.setResourcePrompt(Src);
        ResourceLabel.setText(msg);

        VisionHelper accessibleTerritory = this.current_player.playerActionHelper.visionHelpers.get(this.current_player.color);
        msg = accessibleTerritory.getSpyInfoStr();
        SpyPromptLabel.setText(msg);

        // Initialize the prompt messege accroding to different condition
        if(this.current_player.unitNumber > 0){
            // Messege for placement stage
            msg = this.current_player.color + " player: you still have " 
                        + this.current_player.unitNumber + "units to place.";
        }
        else if(this.current_player.checkGameEnd()){
            // Messege when the game has ended
            Set<String> active_player = this.current_player.endGameDetection.getActivePlayer(this.current_player.myMap);
            msg = "This game has ended. " + active_player.iterator().next() + " wins!";
        }
        else if(this.current_player.checkGameLost()){
            // Messege when the player has lost
            msg = this.current_player.color + " player: you have lost!";
        }
        else{
            // Messege for normal action stage
            msg = this.current_player.color +  " player: Please input your action";
        }
        // Initialize the Prompt Label
        PromptLabel.setText(msg);
    }

    /**
     * This function initialize the MapController class
     * Initialize the mapping between button and territory name, action type and action factory
     * 
     * @return a string that will be displayed
     */
    @FXML
    void initialize()throws IOException, ClassNotFoundException {
        // initialize the hashmap between territroy name and button
        matchBtn = new HashMap<>(){{
            put("Narnia", NarniaFlag);
            put("Midkemia", MidkemiaFlag);
            put("Oz", OzFlag);
            put("Gondor", GondorFlag);
            put("Elantris", ElantrisFlag);
            put("Scadrial", ScadrialFlag);
            put("Mordor", MordorFlag);
            put("Roshar", RosharFlag);
            put("Hogwarts", HogwartsFlag);
            put("Duke", DukeFlag);
        }};

        // Initialize the hashmap between choices and its action factory input
        matchaction = new HashMap<>(){{
            put("Move", "mv");
            put("Attack", "ak");
            put("Upgrade Unit", "uu");
            put("Upgrade Technology Level", "ut");
            put("Move Spy", "ms");
            put("Upgrade to Spy", "us");
            put("Research Cloak", "uc");
            put("Order Cloak", "oc");
            put("Commit", "commit");
            put(null, "default");
        }};
        // Setup the map when its resuming game room
        if (this.mode.equals("resume")){
            // Receive the game status from the server
            int resumedRoomStatus = (int) this.current_player.messenger.recv();


            // Receive the gamemap and other information from serve the update the display map
            this.current_player.setUpInitialMap();
            //this.current_player.setupResourceHelper();
            this.updateDisplayMap();
            if(resumedRoomStatus == Status.TO_PLACEMENT){
                // Initialize the choiceBox according to the function of placement stage
                this.actionType.getItems().addAll("Placement");
                this.initChoicebox(this.dst, this.getYourTerritory());
            }
            else{
                this.current_player.unitNumber = 0;
                // Initialize the titlelabel and choiceBox to the function of action stage
                this.updateTitleLabel();
                this.updateChoiceBox();
            }
        }
        // Initialize the scene when creating new room
        else if(this.mode.equals("create")){
            this.current_player.setUpInitialMap();
            this.updateDisplayMap();
            this.actionType.getItems().addAll("Placement");
            this.initChoicebox(this.dst, this.getYourTerritory());
        }
        else{ // Initialize the scene when the player is joining the room
            this.current_player.setUpInitialMap();
            this.updateDisplayMap();
            this.actionType.getItems().addAll("Placement");
            this.initChoicebox(this.dst, this.getYourTerritory());
        }

        ArrayList<String> unitLevelList = new ArrayList<>(
                        Arrays.asList("soldier", "warrior", "knight", 
                        "captain", "commander", "general", "king"));
                    
        this.initChoicebox(UnitTypeText, unitLevelList);

        // Adjust the displayed map according to number of player inside one room
        this.adjustMap();
    }
    
    /**
     * This function initialize the prompt that will be display in resource label
     * Receive the resouces infomation, cost to neighbor and occupied unit inside this territory
     * 
     * @return a string that will be displayed
     */
    public String setResourcePrompt(String source){
        // Territory my_t = this.current_player.myMap.getTerritoryByName(Src);
        VisionHelper accessibleTerritory = this.current_player.playerActionHelper.visionHelpers.get(this.current_player.color);
        String msg = accessibleTerritory.visionInfo.get(source);

        // if(msg == null){
        //     return "";
        // }
        
        // Map<String, Integer> myResource = my_t.getMyResources();
        // Map<String, Integer> myCostToNeightbor = my_t.getMyCostToNeighInfo();
        // Map<String, Unit> myUnit = my_t.getMyUnits();
        
        // String msg = "Currently has " ;
        // for(Unit type: myUnit.values()){
        //     msg += type.getNum() + " " + type.getName() + ", ";
        // }
        // msg += "\nResource: ";

        // for(String type: myResource.keySet()){
        //     msg += type + " " + myResource.get(type) + ", ";
        // }

        // msg += "\nDistance: ";
        // for(String type: myCostToNeightbor.keySet()){
        //     msg += type + " " + myCostToNeightbor.get(type) + ", ";
        // }

        return msg;
     }
    
    /**
     * This function initialize the choiceBox to its original form
     * 
     */
    public void updateChoiceBox(){
        ArrayList<String> actionSelection = new ArrayList<>(
            Arrays.asList("Move",
                        "Move Spy",
                        "Attack",
                        "Upgrade Unit",
                        "Upgrade Technology Level",
                        "Upgrade to Spy",
                        "Research Cloak",
                        "Order Cloak",
                        "Commit"));
        actionType.getItems().clear();
        for(String name: actionSelection){
            actionType.getItems().addAll(name);
        }

        ArrayList<String> dstName = new ArrayList<>(
            this.current_player.myMap.getTerritoryNames());
        
        dst.getItems().clear();
        for(String name: dstName){
            dst.getItems().addAll(name);
        }
    }

    /**
     * This function adjust the map according to number of player inside one room
     * 
     */
    public void adjustMap(){
        // receive the number of player of one room
        this.roomPlayerNum = this.current_player.myMap.getNumPlayer();
        if(this.roomPlayerNum == 2 || this.roomPlayerNum == 4){
            HogwartsBtn.setVisible(false);
            DukeBtn.setVisible(false);
            HogwartsFlag.setVisible(false);
            DukeFlag.setVisible(false);
            // Hogwarts_1.setVisible(false);
            // Hogwarts_2.setVisible(false);
            // Hogwarts_3.setVisible(false);
            // Hogwarts_4.setVisible(false);
            // Duke_1.setVisible(false);
            // Duke_2.setVisible(false);
        }
        else if(this.roomPlayerNum == 3){
            DukeBtn.setVisible(false);
            DukeFlag.setVisible(false);
            // Hogwarts_4.setVisible(false);
            // Duke_1.setVisible(false);
            // Duke_2.setVisible(false);
        }
    }
    
    /**
     * This function update the title label such that the latest Food resource, Tech resource and Tech level is displayed
     * 
     */
    public void updateTitleLabel(){
        ResourceHelper myResourceDetail = this.current_player.playerActionHelper.resourceHelpers.get(this.current_player.color);
        int techLvl = myResourceDetail.getTechLevel();

        foodLabel.setText("Food: " + myResourceDetail.getResourcesAttr().get("food"));
        techResourceLabel.setText("Tech Resource: " + myResourceDetail.getResourcesAttr().get("technology"));
        techLevelLabel.setText("Tech Level: " + techLvl);

        Boolean cloakGlag = this.current_player.playerActionHelper.resourceHelpers.get(this.current_player.color).cloakFlag;
        cloakLabel.setText("Cloak: " + cloakGlag.toString());
    }

    /**
     * This function reset the choices that will appear in the choice box
     * @param choiceBox, the choiceBox we are resetting the choices
     * @param choices, an arrayList of String that will serve as the choices in the choiceBox
     */
    public void initChoicebox(ChoiceBox<String> choiceBox, ArrayList<String> choices){
        choiceBox.getItems().clear();
        for(String name: choices){
            choiceBox.getItems().addAll(name);
        }
    }

    /**
     * This function returns an arraylist of the territory own by the user
     * 
     */
    public ArrayList<String> getYourTerritory(){
        ArrayList<String> yourTerritory = new ArrayList<>();
        Map<String, Territory> my_t = this.current_player.myMap.getMyTerritories();
        for(String name: my_t.keySet()){
            if(my_t.get(name).getOwner().equals(this.current_player.color)){
                yourTerritory.add(name);
            }
        }

        return yourTerritory;
    }

    /**
     * This function initialize the action panel, clear up the textfield and choiceBox and set the visibility to false
     * 
     */
    public void initActionPanel(){
        UnitNumText.clear();
        UnitTypeText.setValue(null);
        actionType.setValue(null);
        dst.setValue(null);
        actionPanel.setVisible(false);
    }

    /**
     * This function update the displayed map based on current owner of each territory
     * 
     */
    public void updateDisplayMap(){
        Map<String, Territory> my_t = this.current_player.myMap.getMyTerritories();
        for(String name: my_t.keySet()){
            try{
                String msg = setResourcePrompt(name);
                if(msg.equals("<No Info Available>\n") || msg.contains("<Old Info>")){
                    matchBtn.get(name).setStyle("-fx-fill: Gray");
                }
                else{
                    matchBtn.get(name).setStyle("-fx-fill: " + my_t.get(name).getOwner());
                }
            } catch(Exception e){
                matchBtn.get(name).setStyle("-fx-fill: " + my_t.get(name).getOwner());
            }
        }
    }

    /**
     * MapController constructor
     * @param current_player
     * @param mode, flag for distinguishing whether this is placement stage or action stage
     */
    public MapController(Player current_player, String mode) {
        this.current_player = current_player;
        this.mode = mode;
    }

}
