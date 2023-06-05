package edu.duke.ece651.risc.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.GameMapTextView;
import edu.duke.ece651.risc.shared.PlayerActionHelper;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;
import edu.duke.ece651.risc.shared.Messenger;
import edu.duke.ece651.risc.shared.Placement;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.Service.PlayerService;

public class Player {
  public String username;
  public String color;
  public Messenger messenger;
  private BufferedReader inputReader;
  private PrintStream out;
  public PlayerActionHelper playerActionHelper;
  private GameMapTextView view;
  private HashMap<String, Function<String, Action>> actionFactory;
  public EndGameDetection endGameDetection;
  public String currentRoomName;
  public int roomPlayerNum;
  public int unitNumber;
  public GameMap myMap;
  public String mode;
  public static PlayerService playerservice = new PlayerService();

  public Player(String username, Messenger messenger, BufferedReader inputReader, PrintStream out,
      int unitNumber) throws ClassNotFoundException, IOException {
    this.username = username;
    this.messenger = messenger;
    this.inputReader = inputReader;
    this.out = out;
    this.unitNumber = unitNumber;
    this.endGameDetection = new EndGameDetection();
    this.currentRoomName = "";
    this.mode = "";
    // setUpInitialMap();
    setUpActionChoice();
  }

  public void setUpInitialMap() throws ClassNotFoundException, IOException {
    MessageVO initRecvdMessage = (MessageVO) messenger.recv();
    this.color = initRecvdMessage.getColor();
    this.myMap = initRecvdMessage.getGameMap();
    this.roomPlayerNum = initRecvdMessage.getPlayerNum();
    this.view = new GameMapTextView();
    this.playerActionHelper = new PlayerActionHelper(myMap);
    this.displayGameMap();
    setupResourceHelper();
  }

  private void setUpActionChoice() {
    actionFactory = new HashMap<String, Function<String, Action>>();
    actionFactory.put("mv", (s) -> new MoveAction(s));// move
    actionFactory.put("ms", (s) -> new MoveSpyAction(s));// move spy
    actionFactory.put("ak", (s) -> new AttackAction(s));// attack
    actionFactory.put("uu", (s) -> new UpgradeUnitAction(s));// upgrade unit
    actionFactory.put("ut", (s) -> new UpgradeTechAction(s));// upgrade technology level
    actionFactory.put("us", (s) -> new UpgradeSpyAction(s));//upgrade to Spy
    actionFactory.put("uc", (s) -> new UpgradeCloakAction(s));//research cloak
    actionFactory.put("oc", (s) -> new CloakingAction(s));//order cloak
    actionFactory.put("commit", null);
  }

  public void setUsername(String name) {
    this.username = name;
  }

  /**
   * do placement once and output the current player map info to the
   * printstream out
   * 
   */
  public void doOnePlacement(Placement placeInfo) {
    Territory territory = placeInfo.getTerritory();
    String unit = placeInfo.getUnit();
    Integer unitNum = placeInfo.getUnitNum();
    Integer originUnitNum = territory.getMyUnits().get(unit).getNum();
    territory.getMyUnits().get(unit).setNum(unitNum + originUnitNum);
    this.unitNumber -= unitNum;
    this.playerActionHelper.visionHelpers.get(this.color).updateVisionPerAction(this.myMap);
    out.println(
        this.color + " player, your current initial unit Placement is:\n"
            + this.view.displayMyTerritory(this.myMap, this.color));
  }

  /**
   * This is the helper function to display game map based on current map and
   * player number
   * 
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public void displayGameMap() throws ClassNotFoundException, IOException {
    this.out.println(this.view.displayAllTerritory(this.myMap, this.roomPlayerNum));
  }

  /**
   * This function is for update game map after the placement phase
   * 
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public void updateGameMapAfterPlacementPhase() throws ClassNotFoundException, IOException {
    MessageVO messageVO = (MessageVO) this.messenger.recv();
    this.myMap.updateMyTerritories(messageVO.getGameMap(), "");
    this.out.println(messageVO.getMessage());
  }

  public void setupResourceHelper() throws ClassNotFoundException, IOException{
    ResourceHelper playeResourceHelper = (ResourceHelper) this.messenger.recv();
    VisionHelper playeVisionHelper = (VisionHelper) this.messenger.recv();
    playerActionHelper.setupHelper(this.color, playeResourceHelper,playeVisionHelper);
  }

  /**
   * In one Action round, read the details information of the Action and check
   * whether it is legal
   * with illegal input, print prompt to player and request re-enter
   * 
   * @param prompt      to show player the gamemap and action information
   * @param choice_type is whether move or attack
   * @throws IOException
   */
  public void readActionContent(String choice_type, String src, String dst, String unitlevel, int unitQuantity)
      throws IOException, IllegalArgumentException {

    // try {
    Action toCheckAction = actionFactory.get(choice_type).apply(this.color);
    toCheckAction.updateParam(src, dst, unitlevel, unitQuantity);
    String check_result = playerActionHelper.doCheckRehearser(toCheckAction);
    this.playerActionHelper.visionHelpers.get(this.color).updateVisionPerAction(this.myMap);
    if (check_result != null) {
      throw new IllegalArgumentException(check_result);
    }
    // } catch (IllegalArgumentException e) {
    // out.println(e.getMessage());
    // }

    return;
  }

  // /**
  // * read one Action Content for GUI move/attack
  // * @param prompt
  // * @param actionType is move/attack/upgradeUnit/upgradeTech
  // * @param param1 is the src Territory for move/attack, territory for
  // upgradeUnit
  // * @param param2 is the dst Territory for move/attack, src unitType for
  // upgradeUnit
  // * @param param3 is the unitName for move/attack, tar unitType for upgradeUnit
  // * @throws IOException
  // */
  // public void readActionContent(String prompt,String actionType, String param1,
  // String param2, String param3, int unitNum) throws IOException {
  // //String s;
  // out.println(prompt);
  // while (true) {
  // //s = inputReader.readLine();
  // try {
  // Action toCheckAction = actionFactory.get(actionType).apply(this.color);
  // toCheckAction.updateParam(param1, param2, param3, unitNum);
  // String check_result = playerActionHelper.doCheckRehearser(toCheckAction);
  // if (check_result != null) {
  // // System.out.println(check_result);
  // throw new IllegalArgumentException(check_result);
  // }
  // break;
  // } catch (IllegalArgumentException e) {
  // out.println(e.getMessage());
  // }
  // }
  // return;
  // }

  /**
   * test help function to return the player's color
   * 
   * @return the player's color
   */
  public String getColor() {
    return this.color;
  }

  /**
   * check whether the player has lost the game
   * 
   * @return if the player lost the game return true, else false
   */
  public Boolean checkGameLost() {
    Set<String> active_player = this.endGameDetection.getActivePlayer(this.myMap);
    return this.endGameDetection.isLost(active_player, this.color);
  }

  /**
   * This is the function to send necessary message to server, including
   * create/join/resume,
   * 
   * @param message   create / join /resume, the action that user would like to do
   * @param playerNum if create room, this parameter is needed
   * @param roomName  room name
   * @param userName  user name
   * @throws IOException
   */
  public void sendMessageVoToServer(String message, int playerNum, String roomName, String userName, String password)
      throws IOException {
    MessageVO messageVO = new MessageVO(message, -1, null, null, null, playerNum, -1, roomName, userName, password,
        null,
        Status.TO_PLACEMENT);
    this.messenger.send(messageVO);
  }

  /**
   * This function is to let player join room, if there is no available room,
   * player will be
   * reprompted to menu, if there is available room, player should input the
   * existed available room
   * name to enter into. Also they can input menu.
   * 
   * @return HashSet of available room name
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public HashSet<String> tryJoinRoom() throws IOException, ClassNotFoundException {
    this.sendMessageVoToServer("join", -1, null, this.username, "");
    return (HashSet<String>) this.messenger.recv();
  }

  /**
   * This function is to create room, player could return to menu by inputing
   * "menu",
   * otherwise they should input room name and room capacity
   * 
   * @return null if player create room
   * @throws IOException
   */
  public String doCreateRoom(String roomName, String Capacity) throws IOException {
    String message = "Please input following info to create room: room name, room capacity. E.g.: room1,3\n" +
        "Input menu to return to menu\n";
    out.println(message);

    try {
      if (roomName == "" || roomName == null) {
        throw new EOFException();
      }

      this.sendMessageVoToServer("create", Integer.parseInt(Capacity), roomName, this.username, "");
      if (!(Boolean) this.messenger.recv()) {
        out.println("Room name existed, please input another room name! Only room name!");
        return "Room name existed!";
      }
      this.currentRoomName = roomName;

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }

    return "";
  }

  /**
   * This function is to resume room, player can choose a joined room to resume,
   * After resume, client will indicate player what step to do next
   * 
   * @return HashSet of available room name
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public HashSet<String> tryResumeRoom() throws IOException, ClassNotFoundException {
    MessageVO messageVO = new MessageVO("resume", this.username);
    this.messenger.send(messageVO);
    out.println("Below are your joined room names, input room name to resume");
    return (HashSet<String>) this.messenger.recv();
  }

  /**
   * check whether the game is end
   * 
   * @return if game end return true, else false
   */
  public Boolean checkGameEnd() {
    Set<String> active_player = this.endGameDetection.getActivePlayer(this.myMap);

    if (this.endGameDetection.isEndGame(active_player)) {
      // DisplayResult(active_player.iterator().next());
      System.out.println(active_player.iterator().next());
      return true;
    }
    return false;
  }

  public MessageVO doLoginSignup(String user, String passwd) throws IOException, ClassNotFoundException {
    try {
      if (this.mode == "" || this.mode == null) {
        throw new EOFException(); 
      }
      if (this.mode.equals("l")) {
        // Prompt user to enter password for login
        String message = "Please enter your password";
        out.println(message);

        this.sendMessageVoToServer("login", 0, "", user, passwd);

        // Receive the response from the server
        MessageVO messageVO = (MessageVO) this.messenger.recv();
        out.println(messageVO.getMessage());

        return messageVO;
      } else if (this.mode.equals("s")) {
        // Prompt user to setup new password
        String message = "Please setup your password";
        out.println(message);

        this.sendMessageVoToServer("signup", 0, "", user, passwd);
        MessageVO messageVO = (MessageVO) this.messenger.recv();
        out.println(messageVO.getMessage());

        return messageVO;
      } else if (this.mode.equals("e")) {
        System.exit(0);
      }

    } catch (IllegalArgumentException e) {
      out.println(e.getMessage());
    }

    return new MessageVO("", Status.NONE);
  }

  public int getScore(){
    return playerservice.playerScore(this.username);
  }
}