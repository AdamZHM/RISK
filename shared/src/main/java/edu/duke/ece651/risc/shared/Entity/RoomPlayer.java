package edu.duke.ece651.risc.shared.Entity;

public class RoomPlayer {
  Integer id;

  Integer roomId;

  Integer playerId; // player id in that room

  String playerName;

  Integer food;

  Integer techResource;

  Integer techLevel;

  Boolean cloak;

  public RoomPlayer(Integer roomId) {
    this.roomId = roomId;
  }

  public RoomPlayer(Integer roomId, String playerName) {
    this.roomId = roomId;
    this.playerName = playerName;
  }

  public RoomPlayer(Integer id, Integer roomId, Integer playerId, String playerName) {
    this.id = id;
    this.roomId = roomId;
    this.playerId = playerId;
    this.playerName = playerName;
  }

  public RoomPlayer(Integer roomId, Integer playerId, String playerName) {
    this.roomId = roomId;
    this.playerId = playerId;
    this.playerName = playerName;
  }

  public RoomPlayer(Integer id, Integer roomId, Integer playerId, String playerName, Integer food, Integer techResource,
      Integer techLevel) {
    this.id = id;
    this.roomId = roomId;
    this.playerId = playerId;
    this.playerName = playerName;
    this.food = food;
    this.techResource = techResource;
    this.techLevel = techLevel;
  }

  public RoomPlayer(Integer roomId, Integer playerId, String playerName, Integer food, Integer techResource,
      Integer techLevel, Boolean cloak) {
    this.roomId = roomId;
    this.playerId = playerId;
    this.playerName = playerName;
    this.food = food;
    this.techResource = techResource;
    this.techLevel = techLevel;
    this.cloak = cloak;
  }

  public RoomPlayer(Integer id, Integer roomId, Integer playerId, String playerName, Integer food, Integer techResource,
      Integer techLevel, Boolean cloak) {
    this.id = id;
    this.roomId = roomId;
    this.playerId = playerId;
    this.playerName = playerName;
    this.food = food;
    this.techResource = techResource;
    this.techLevel = techLevel;
    this.cloak = cloak;
  }

  public RoomPlayer(Integer roomId, String playerName, Boolean cloak) {
    this.roomId = roomId;
    this.playerName = playerName;
    this.cloak = cloak;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getRoomId() {
    return roomId;
  }

  public void setRoomId(Integer roomId) {
    this.roomId = roomId;
  }

  public Integer getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Integer playerId) {
    this.playerId = playerId;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public Integer getFood() {
    return food;
  }

  public void setFood(Integer food) {
    this.food = food;
  }

  public Integer getTechResource() {
    return techResource;
  }

  public void setTechResource(Integer techResource) {
    this.techResource = techResource;
  }

  public Integer getTechLevel() {
    return techLevel;
  }

  public void setTechLevel(Integer techLevel) {
    this.techLevel = techLevel;
  }

  public Boolean getCloak(){
    return cloak;
  }

}
