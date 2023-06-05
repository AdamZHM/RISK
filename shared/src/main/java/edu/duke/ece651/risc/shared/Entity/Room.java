package edu.duke.ece651.risc.shared.Entity;

public class Room {
  int id;

  String name;

  int playerNum;

  int round;

  public Room(String name) {
    this.name = name;
  }

  public Room(String name, int playerNum, int round) {
    this.name = name;
    this.playerNum = playerNum;
    this.round = round;
  }

  public int getId() {
    return id;
  }

  public Room(int id, String name, int playerNum, int round) {
    this.id = id;
    this.name = name;
    this.playerNum = playerNum;
    this.round = round;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getname() {
    return name;
  }

  public void setname(String name) {
    this.name = name;
  }

  public int getPlayerNum() {
    return playerNum;
  }

  public void setPlayerNum(int playerNum) {
    this.playerNum = playerNum;
  }

  public int getRound() {
    return round;
  }

  public void setRound(int round) {
    this.round = round;
  }

}
