package edu.duke.ece651.risc.shared.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.risc.shared.Action;
import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Utils.Status;

public class MessageVO implements Serializable {
  private final String message;

  private final Integer id;

  private final String color;

  public GameMap gameMap;

  private final List<String> territory_group;

  private final Integer playerNum; // capacity

  private Integer currentPlayerNum; 

  private String roomName;

  private String userName;

  private String password;

  private int status;

  private Action action;

  /**
   * contruct function to construct a MessageVO
   * 
   * @param id              the player id
   * @param color           the color of player
   * @param gameMap         the gameMap
   * @param territory_group the territory group belongs the the player
   */
  public MessageVO(String message, int id, String color, GameMap gameMap, List<String> territory_group,
      Integer playerNum, Integer currentPlayerNum, String roomName, String userName, String password, Action action, int status) {
    this.message = message;
    this.id = id;
    this.color = color;
    this.gameMap = gameMap;
    this.territory_group = territory_group;
    this.playerNum = playerNum;
    this.currentPlayerNum = currentPlayerNum;
    this.roomName = roomName;
    this.userName = userName;
    this.password = password;
    this.action = action;
    this.status = status;
  }

  public MessageVO(String message, GameMap gameMap){
    this(message, -1, null, gameMap, null, -1, -1, null, null, null, null, -1);
  }

  public MessageVO(String message, String userName){
    this(message, -1, null, null, null, -1, -1, null, userName, null, null, -1);
  }

  public MessageVO(String message, Action action){
    this(message, -1, null, null, null, -1, -1, null, null, null, action, -1);
  }

  public MessageVO(Integer status, GameMap gameMap){
    this(null, -1, null, gameMap, null, -1, -1, null, null, null, null, status);
  }

  public MessageVO(String message, Integer status){
    this(message, -1, null, null, null, -1, -1, null, null, null, null, status);
  }

  /**
   * This is the get function of message
   * 
   * @return message
   */
  public String getMessage() {
    return message;
  }

  /**
   * this is the get function of id
   * 
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * this is the get function of color
   * 
   * @return color
   */
  public String getColor() {
    return color;
  }

  /**
   * this is the gamemap's get function
   * 
   * @return game map
   */
  public GameMap getGameMap() {
    return gameMap;
  }

  /**
   * this is the get function of territory group which
   * belongs to the player
   * 
   * @return territory_group
   */
  public List<String> getTerritory_group() {
    return territory_group;
  }

  /**
   * This function return the player num of current room
   * 
   * @return player num
   */
  public Integer getPlayerNum() {
    return playerNum;
  }

  public Integer getCurrentPlayerNum() {
    return currentPlayerNum;
  }

  public void setCurrentPlayerNum(Integer currentPlayerNum) {
    this.currentPlayerNum = currentPlayerNum;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  
}
