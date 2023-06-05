package edu.duke.ece651.risc.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.Entity.Player;
import edu.duke.ece651.risc.shared.Entity.Room;
import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;

public class PlayerHandler extends Server implements Runnable {
  protected final Messenger messenger;
  protected RoomHandler currentRoomHandler;
  protected String playerName;
  protected EndGameDetection endGameDetection;

  public PlayerHandler(Messenger toClientMessenger) {
    this.messenger = toClientMessenger;
    endGameDetection = new EndGameDetection();
  }

  /**
   * This is the function that would run when a player is connected to the server.
   * To start a new action, player must send a messageVo and put their wanted
   * action
   * in the field message. There are actions including
   * create/join/resume/placement/action.
   */
  @Override
  public void run() {
    try {
      while (true) {
        MessageVO messageVO = (MessageVO) this.messenger.recv();
        String message = messageVO.getMessage();
        System.out.println(message);
        if (message.equals("login")) { // login stage
          this.validatePassword(messageVO.getUserName(), messageVO.getPassword());
        } else if (message.equals("signup")) { // sign up process
          this.setupPassword(messageVO.getUserName(), messageVO.getPassword());
        } else if (message.equals("create")) {
          this.doCreateRoom(messageVO);
        } else if (message.equals("join")) {
          this.doJoinRoom(messageVO);
        } else if (message.equals("resume")) {
          this.doResumeRoom(messageVO);
        } else if (message.equals("placement")) { // placement phase-
          this.doPlacement(messageVO.getGameMap());
        } else if (message.equals("action")) {
          if (this.doAction(messageVO.getAction()) == true) {
            // client side should prompt to menu
            continue;
          }
        } else if (message.equals("exit")) {
          break;
        }
      }
    } catch (ClassNotFoundException | IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public void doResumeRoom(MessageVO messageVO) throws IOException, ClassNotFoundException {
    this.playerName = messageVO.getUserName();
    Set<String> joinedRoomName = this.getJoinedRoomName();
    this.messenger.send(joinedRoomName);
    if (joinedRoomName.size() == 0) {
      return;
    }
    String roomName = (String) this.messenger.recv();

    if(roomName.equals("break")){
      return;
    }

    this.currentRoomHandler = Server.roomMap.get(roomName);
    int status = this.currentRoomHandler.name2Status.get(this.playerName);
    int id = this.currentRoomHandler.playerName2Id.get(this.playerName);
    this.messenger.send(status);
    
    MessageVO messageVO2 = new MessageVO("", id, this.currentRoomHandler.playerId2Color.get(id),
        this.currentRoomHandler.gameMap,
        this.currentRoomHandler.gameMap.getTerritoryNamesInGroups().get(id),
        this.currentRoomHandler.playerNum, this.currentRoomHandler.playerNum, roomName, "", null, null,
        this.currentRoomHandler.name2Status.get(this.playerName));
    this.messenger.send(messageVO2);
    this.messenger.send(this.currentRoomHandler.serverActionHelper.resourceHelpers.get(this.currentRoomHandler.playerId2Color.get(id)));
    this.messenger.send(this.currentRoomHandler.serverActionHelper.visionHelpers.get(this.currentRoomHandler.playerId2Color.get(id)));
  }

  public void doJoinRoom(MessageVO messageVO) throws ClassNotFoundException, IOException {
    this.playerName = messageVO.getUserName();
    String roomName = messageVO.getRoomName();
    Set<String> availableRoomName = getAvailableRoomName();
    // send joinable room to player
    this.messenger.send(availableRoomName);
    if (availableRoomName.size() == 0) {
      return;
    }
    // recv room name that player'd like to join
    roomName = (String) this.messenger.recv();
    if (roomName.equals("menu") || roomName.equals("break")) {
      return;
    }
    this.currentRoomHandler = roomMap.get(roomName);
    this.currentRoomHandler.acceptOnePlayer(messageVO.getUserName(), messenger);
    this.currentRoomHandler.name2Status.put(this.playerName, Status.TO_PLACEMENT);

    System.out.println(this.currentRoomHandler.getCurrentPlayerNum().get() + "+" + this.currentRoomHandler.playerNum);
    System.out.println(this.currentRoomHandler.getCurrentPlayerNum().get() + "+" + this.currentRoomHandler.playerNum);
    System.out.println(this.currentRoomHandler.getCurrentPlayerNum().get() + "+" + this.currentRoomHandler.playerNum);

    if (this.currentRoomHandler.getCurrentPlayerNum().get() == this.currentRoomHandler.playerNum) {
      System.out.println("Waiting server to create the room");
      threadPoolExecutor.submit(this.currentRoomHandler);
      this.currentRoomHandler.sendInitMessageToAll();
      this.currentRoomHandler.setCurrentPlayerNumToZero();
      System.out.println("Create room successfully!");
    }
  }

  public void doCreateRoom(MessageVO messageVO) throws IOException {
    this.playerName = messageVO.getUserName();
    String roomName = messageVO.getRoomName();
    int playerNum = messageVO.getPlayerNum();
    if (roomMap.containsKey(roomName)) {
      this.messenger.send(false); // room existed
      return;
      // roomName = (String) this.messenger.recv();
    }
    this.messenger.send(true); // create room

    Server.roomService.saveRoom(new Room(roomName, playerNum, 0));
    Room room = roomService.selectRoom(roomName);
    int roomId = room.getId();

    GameMap gameMap = new GameMap(playerNum);
    RoomHandler roomHandler = new RoomHandler(playerNum, roomName, gameMap, roomId, 0);
    roomMap.put(roomName, roomHandler);
    roomHandler.acceptOnePlayer(messageVO.getUserName(), messenger);
    this.currentRoomHandler = roomHandler;
    this.currentRoomHandler.name2Status.put(this.playerName, Status.TO_PLACEMENT);
  }

  /**
   * When received "placement" from player, this function will doPlacement in
   * certain room,
   * and when every player has sent placement info, roomhandler will send the
   * initialized
   * game map to all players.
   * 
   * @param gameMap The game map from player, which includes players' own
   *                placement info
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public void doPlacement(GameMap gameMap) throws ClassNotFoundException, IOException {
    this.currentRoomHandler.recvInitialPlacementFromOnePlayer(this.playerName, gameMap);
    System.out.println(this.currentRoomHandler.getCurrentPlayerNum());
    int currentPlayerNum = this.currentRoomHandler.addCurrentPlayerNumByOne();
    this.currentRoomHandler.name2Status.put(this.playerName, Status.TO_DOACTION);
    System.out.println(currentPlayerNum + " " + this.currentRoomHandler.playerNum);
    if (currentPlayerNum == this.currentRoomHandler.playerNum) {
      this.currentRoomHandler.sendGameMapAfterPlacementDoneToAll();

      this.currentRoomHandler.setCurrentPlayerNumToZero(); // set to zero
      System.out.println("placement phase finished");
    }
  }

  /**
   * This function is to have certain room receive action from one player, and
   * when everyone
   * has sent their action, the room will execute thos actions.
   * 
   * @param action action from one player
   * @return true if game end, else return false and let game continue
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public Boolean doAction(Action action) throws ClassNotFoundException, IOException {
    // this.currentRoomHandler.playerNum =
    // endGameDetection.getActivePlayer(this.currentRoomHandler.gameMap).size();
    this.currentRoomHandler.recvActionFromOnePlayer(this.playerName, action);
    int currentPlayerNum = this.currentRoomHandler.addCurrentPlayerNumByOne();
    this.currentRoomHandler.name2Status.put(this.playerName, Status.TO_DOACTION);
    int currentActivePlayerNum = endGameDetection.getActivePlayer(this.currentRoomHandler.gameMap).size();
    if (currentPlayerNum == currentActivePlayerNum) {
      this.currentRoomHandler.updateTerritoryUnit();
      this.currentRoomHandler.serverActionHelper.updatePlayerResourcePerRound();
      this.currentRoomHandler.serverActionHelper.doExecute();
      Map<String, VisionHelper> visionHelpers = this.currentRoomHandler.serverActionHelper.visionHelpers;
//      for (VisionHelper visionHelper: visionHelpers.values()) {
//        visionHelper.updateVisionPerRound(currentRoomHandler.gameMap, visionHelpers);
//        visionHelper.updateCloakingPerRound();
//      }
      this.currentRoomHandler.serverActionHelper.updatePlayerVisionPerRound();
      this.currentRoomHandler.sendGameMapAfterOneRoundToAll();
      Set<String> active_player = this.currentRoomHandler.endGameDetection
          .getActivePlayer(this.currentRoomHandler.gameMap);
      if (this.currentRoomHandler.endGameDetection.isEndGame(active_player)) {
        String winner = active_player.iterator().next();
        System.out.println(winner + " player win!");
        playerService.calcScore(this.currentRoomHandler.playerId2Name.get(PlayerId2ColorHelper.playerColor2Id.get(winner)), this.currentRoomHandler.playerNames);
        return true;
      }

      this.currentRoomHandler.serverActionHelper.resetAction();
      this.currentRoomHandler.setCurrentPlayerNumToZero(); // set to zero
      System.out.println("this action phase finished");
    }
    return false;
  }

  /**
   * This function is to get available room names currently, when search
   * for available rooms, resource roomMap should not be writed by other threads.
   * 
   * @return a set of room names
   */
  public synchronized Set<String> getAvailableRoomName() {
    Set<String> availableRoomName = new HashSet<>();
    for (Map.Entry<String, RoomHandler> m : roomMap.entrySet()) {
      if (m.getValue().roomAvailable() && !m.getValue().playerNames.contains(this.playerName) && playerService.playersMatchable(playerName, m.getValue().playerNames)) {
        availableRoomName.add(m.getKey());
      }
    }
    return availableRoomName;
  }

  /**
   * This function is to get player's joined room names
   * 
   * @return a set of room names
   */
  public Set<String> getJoinedRoomName() {
    Set<String> joinedRoomName = new HashSet<>();
    for (Map.Entry<String, RoomHandler> m : roomMap.entrySet()) {
      RoomHandler roomHandler = m.getValue();
      if (roomHandler.playerNames.contains(this.playerName)) {
        joinedRoomName.add(m.getKey());
        continue;
      }
    }
    return joinedRoomName;
  }

  /**
   * This function match the username with password using a hashmap.
   * 
   * @param username the username
   * @param password the password receives from the Player
   */
  public void validatePassword(String username, String password) throws ClassNotFoundException, IOException {
    if (playerService.login(new Player(username, password))) {
      allName2Messenger.put(username, this.messenger);
      MessageVO verifyLogin = new MessageVO("Login successfully", Status.LOGINSUCCESS);
      this.messenger.send(verifyLogin);
    } else {
      MessageVO verifyLogin = new MessageVO("Login error, check your name and password please", Status.NONE);
      this.messenger.send(verifyLogin);
    }
    // if (UserPassword.containsKey(username)) {
    // if (UserPassword.get(username).equals(password)) {
    // 
    // MessageVO verifyLogin = new MessageVO("Login successfully",
    // Status.LOGINSUCCESS);
    // this.messenger.send(verifyLogin); // correct password
    // } else {
    // MessageVO verifyLogin = new MessageVO("Login fail, wrong password",
    // Status.NONE);
    // this.messenger.send(verifyLogin); // wrong password
    // }
    // } else {
    // MessageVO verifyLogin = new MessageVO("Username doesn't exist", Status.NONE);
    // this.messenger.send(verifyLogin); // Username doesn't exist
    // }
  }

  /**
   * This function setup new password for an username into the hashmap.
   * 
   * @param username the username
   * @param password the new password that will be linked to this username
   */
  public void setupPassword(String username, String password) throws ClassNotFoundException, IOException {
    if (playerService.register(new Player(username, password))) {
      MessageVO verifyLogin = new MessageVO("Successfully setup new password", Status.SIGNUPSUCCESS);
      this.messenger.send(verifyLogin); // Username doesn't exist
    } else {
      MessageVO verifyLogin = new MessageVO("Username has already exist", Status.NONE);
      this.messenger.send(verifyLogin); // username already exist
    }
    // if (UserPassword.containsKey(username)) {
    // MessageVO verifyLogin = new MessageVO("Username has already exist",
    // Status.NONE);
    // this.messenger.send(verifyLogin); // username already exist
    // } else {
    // UserPassword.put(username, password);
    // }
  }
}
