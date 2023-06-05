package edu.duke.ece651.risc.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.shared.Action;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Messenger;
import edu.duke.ece651.risc.shared.PlacementChecker;
import edu.duke.ece651.risc.shared.ResourceHelper;
import edu.duke.ece651.risc.shared.ServerActionHelper;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.VisionHelper;
import edu.duke.ece651.risc.shared.Entity.RoomPlayer;
import edu.duke.ece651.risc.shared.Entity.Vision;
import edu.duke.ece651.risc.shared.Service.RoomPlayerService;
import edu.duke.ece651.risc.shared.Service.VisionService;
import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;
import edu.duke.ece651.risc.shared.Utils.Status;
import edu.duke.ece651.risc.shared.vo.MessageVO;
import edu.duke.ece651.risc.shared.EndGameDetection;

public class RoomHandler extends Server implements Runnable {
  protected Set<String> playerNames;
  protected Map<String, Integer> name2Status;
  protected int playerNum;
  protected Map<Integer, String> playerId2Name;
  protected Map<String, Integer> playerName2Id;
  protected Map<Integer, String> playerId2Color;
  protected GameMap gameMap;
  protected ServerActionHelper serverActionHelper;
  protected PlacementChecker placementChecker;
  protected String roomName;
  protected EndGameDetection endGameDetection;
  // indicate that at current phase, how many players has connected to server
  // after every phase, this number would be set to 0
  protected AtomicInteger currentPlayerNum;
  protected int roomId;
  protected int round;

  public RoomHandler(int playerNum, String roomName, GameMap gameMap, int roomId, int round) {
    this.playerNames = new HashSet<>();
    this.playerNum = playerNum;
    this.playerId2Color = PlayerId2ColorHelper.playerId2Color;
    this.gameMap = gameMap;
    this.serverActionHelper = new ServerActionHelper(gameMap);
    this.placementChecker = new PlacementChecker(gameMap);
    this.endGameDetection = new EndGameDetection();
    this.playerId2Name = new ConcurrentHashMap<>();
    this.playerName2Id = new ConcurrentHashMap<>();
    this.roomName = roomName;
    this.currentPlayerNum = new AtomicInteger(0);
    this.name2Status = new ConcurrentHashMap<>();
    this.roomId = roomId;
    this.round = round;
  }

  @Override
  public void run() {
    // territoryService.updateMap(this.gameMap, roomId);
  }

  /**
   * This function is to accept one player to join the room
   * 
   * @param playerName player name
   * @param messenger  socket of that player
   */
  public void acceptOnePlayer(String playerName, Messenger messenger) {
    System.out.println("One player connected");
    this.playerNames.add(playerName);
    // this.name2Messenger.put(playerName, messenger);
    int id = this.currentPlayerNum.getAndIncrement();
    this.playerId2Name.put(id, playerName);
    this.playerName2Id.put(playerName, id);
    setupTerritory(id, this.playerId2Color.get(id));
    this.serverActionHelper.initPlayerResource(this.playerId2Color.get(id));
    ResourceHelper resourceHelper = this.serverActionHelper.resourceHelpers.get(this.playerId2Color.get(id));
    int food = resourceHelper.getResourcesAttr().get("food");
    int techLevel = resourceHelper.getTechLevel();
    int techResource = resourceHelper.getResourcesAttr().get("technology");
    roomPlayerService.saveOnePlayerToRoom(roomId, id, playerName, food, techResource, techLevel);
    VisionHelper visionHelper = this.serverActionHelper.visionHelpers.get(this.playerId2Color.get(id));
    setupInitVisionHelper(visionHelper, id);
    // VisionHelper visionHelper = this.serverActionHelper.visionHelpers.get(this.playerId2Color.get(id));

  }

  /**
   * This function send initial message to all players. message has color, initial
   * game map.
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void sendInitMessageToAll() throws IOException, ClassNotFoundException {
    Map<Integer, List<String>> territoryNamesInGroups = this.gameMap.getTerritoryNamesInGroups();

    serverActionHelper.updatePlayerResourcePerRound();
    serverActionHelper.updatePlayerVisionPerRound();
    
    for (int i = 0; i < playerNum; i++) {
      MessageVO messageVO = new MessageVO("", i, this.playerId2Color.get(i), this.gameMap,
          territoryNamesInGroups.get(i), playerNum, playerNum, roomName, "", null, null, Status.TO_PLACEMENT);
      Messenger playerMessenger = allName2Messenger.get(this.playerId2Name.get(i));
      playerMessenger.send(messageVO);
      playerMessenger.send(serverActionHelper.resourceHelpers.get(this.playerId2Color.get(i)));
      playerMessenger.send(serverActionHelper.visionHelpers.get(this.playerId2Color.get(i)));
    
      System.out.println("Send init messageVo to player " + this.playerId2Color.get(i));
    }
    updateRoomInfo();
  }

  /**
   * This function is to receive the placement from one player. After recv the placement,
   * there are one thing to do:
   * 1) update player's round to round + 1 and save the latest action
   * 
   * @param playerName the name of player
   * @param newGameMap the updated game map from player
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void recvInitialPlacementFromOnePlayer(String playerName, GameMap newGameMap)
      throws IOException, ClassNotFoundException {
    Messenger playerMessenger = allName2Messenger.get(playerName);
    this.placementChecker.updateMap(newGameMap);
    int id = this.playerName2Id.get(playerName);
    System.out.println("receive placements from player " + id);
    while (placementChecker.check(id) != null) {
      playerMessenger.send(false);
      MessageVO messageVO = (MessageVO) playerMessenger.recv();
      newGameMap = messageVO.getGameMap();
      this.placementChecker.updateMap(newGameMap);
    }
    playerMessenger.send(true);
    this.gameMap.updateMyTerritories(newGameMap, this.playerId2Color.get(id));
  }

  /**
   * This function is to receive action from one player, basic check rehearser
   * will
   * triggered here
   * 
   * @param playerName the name of player
   * @param action     the action list from player
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public void recvActionFromOnePlayer(String playerName, Action action) throws ClassNotFoundException, IOException {
    Messenger playerMessenger = allName2Messenger.get(playerName);
    Set<String> active_player = this.endGameDetection.getActivePlayer(this.gameMap);
    int playerId = this.playerName2Id.get(playerName);
    if (!active_player.contains(playerId2Color.get(playerId))) {
      return;
    }
    Boolean flag = false;
    while (false == flag) {
      if (this.serverActionHelper.doCheckRehearser(action) == null) {
        flag = true;
      }
      playerMessenger.send(flag);
      if (flag == false) {
        MessageVO messageVO = (MessageVO) playerMessenger.recv();
        action = messageVO.getAction();
      }
    }
    System.out.println("Receive action from player: " + playerId);
  }

  /**
   * This function is to sned gameMap after one action round to all the players in
   * this room
   * 
   * @throws IOException
   */
  public void sendGameMapAfterOneRoundToAll() throws IOException {
    for (int i = 0; i < playerNum; i++) {
      Set<String> active_player = this.endGameDetection.getActivePlayer(this.gameMap);
      Messenger playerMessenger = allName2Messenger.get(this.playerId2Name.get(i));
      if (!active_player.contains(playerId2Color.get(i))) {
        try {
          playerMessenger.messengerAlive();
        } catch (Exception e) {
          continue;
        }
      }
      playerMessenger.send(this.gameMap);
      System.out.println("send techLevel to "+this.playerId2Color.get(i)+" "+serverActionHelper.resourceHelpers.get(this.playerId2Color.get(i)).getTechLevel());
      // serverActionHelper.updatePlayerResourcePerRound();
      playerMessenger.send(serverActionHelper.resourceHelpers.get(this.playerId2Color.get(i)));
      playerMessenger.send(serverActionHelper.visionHelpers.get(this.playerId2Color.get(i)));
    }
    updateRoomInfo();
  }

  /**
   * This function is that server indicates to all clients when placement phase
   * done
   * 
   * @throws IOException
   * @throws CloneNotSupportedException
   */
  public void sendGameMapAfterPlacementDoneToAll() throws IOException {
    // serverActionHelper.updatePlayerResourcePerRound();
    // serverActionHelper.updatePlayerVisionPerRound();
    for (int i = 0; i < playerNum; i++) {
      Messenger playerMessenger = allName2Messenger.get(this.playerId2Name.get(i));
      MessageVO messageVO = new MessageVO(
          "From server to" + playerId2Color.get(i) + ": All players have finished their initial placement!!!!\n", i,
          this.playerId2Color.get(i), this.gameMap, this.gameMap.getTerritoryNamesInGroups().get(i),
          this.playerNum, this.playerNum, this.roomName, "", null, null, -1);
      System.out.println(messageVO.getMessage());
      playerMessenger.send(messageVO);
    // VisionHelper visionHelper = this.serverActionHelper.visionHelpers.get(this.playerId2Color.get(i));
      // setupInitVisionHelper(visionHelper, i);
    }
    updateRoomInfo();
  }

  public void setupInitVisionHelper(VisionHelper visionHelper, int id){
    visionService.insertIntoVision(visionHelper, gameMap, roomId, id);
  }

  /**
   * This function setup the game map's territories owner
   * 
   * @param id          the player id, related to the territory group id
   * @param playerColor the color of player
   */
  public void setupTerritory(int id, String playerColor) {
    Map<Integer, List<String>> territory_group = gameMap.getTerritoryNamesInGroups();
    Map<String, Territory> my_territories = gameMap.getMyTerritories();
    for (String territory_name : territory_group.get(id)) {
      my_territories.get(territory_name).setOwner(playerColor);
    }
  }

  /**
   * This function update the number of unit in each territory
   * 
   */
  public void updateTerritoryUnit() {
    Map<String, Territory> my_territories = gameMap.getMyTerritories();
    for (String territory_name : gameMap.getTerritoryNames()) {
      Territory curr_territory = my_territories.get(territory_name);
      int curr_unit = curr_territory.getUnitByName("soldier").getNum();
      curr_territory.getUnitByName("soldier").setNum(curr_unit + 1);
    }
  }

  /**
   * This function is to tell if room still has seats
   * 
   * @return true if seats available, false if room is full
   */
  public Boolean roomAvailable() {
    if (this.playerNames.size() == this.playerNum) {
      return false;
    }
    return true;
  }

  /**
   * This is the helper function to get current player num
   * who has sent the message at certain round. Therefore when this
   * number equals to player number, roomhandler would do its thing
   * Also, after each round this number would be set to zero
   */
  public AtomicInteger getCurrentPlayerNum() {
    return currentPlayerNum;
  }

  /**
   * Whenever a player sent message to server during one round, call this
   * function.
   * 
   * @return number after add one
   */
  public Integer addCurrentPlayerNumByOne() {
    return this.currentPlayerNum.incrementAndGet();
  }

  /**
   * Helpr function to set currentPlayerNum to 0
   */
  public void setCurrentPlayerNumToZero() {
    this.currentPlayerNum = new AtomicInteger(0);
  }

  /**
   * update the game map of room and add 1 to the round
   */
  public void updateRoomInfo(){
    territoryService.updateMap(this.gameMap, roomId);
    // set current room round to 1, which is the placement round
    roomService.updateRoomRound(roomName);
    for (int i = 0; i < playerNum; i++){
      String name = this.playerId2Name.get(i);
      ResourceHelper resourceHelper = this.serverActionHelper.resourceHelpers.get(this.playerId2Color.get(i));
      int food = resourceHelper.getResourcesAttr().get("food");
      int techLevel = resourceHelper.getTechLevel();
      int techResource = resourceHelper.getResourcesAttr().get("technology");

      RoomPlayer roomPlayer = new RoomPlayer(roomId, i, name, food, techResource, techLevel, resourceHelper.cloakFlag);
      roomPlayerService.updateResource(roomPlayer);

      VisionHelper visionHelper = this.serverActionHelper.visionHelpers.get(this.playerId2Color.get(i));
      visionService.updateVision(visionHelper, gameMap, roomId, i);

    }
  }
}