package edu.duke.ece651.risc.shared.Entity;

public class Territorys {
  int id;

  String name;

  String unitType;

  int unitNumber;

  String owner;

  int roomId;

  // public Territorys(String name, String unitType, int roomId) {
  //   this.name = name;
  //   this.unitType = unitType;
  //   this.roomId = roomId;
  // }

  public Territorys(String name, String unitType, int unitNumber, String owner, int roomId) {
    this.name = name;
    this.unitType = unitType;
    this.unitNumber = unitNumber;
    this.owner = owner;
    this.roomId = roomId;
  }

  public Territorys(int id, String name, String unitType, int unitNumber, String owner, int roomId) {
    this.id = id;
    this.name = name;
    this.unitType = unitType;
    this.unitNumber = unitNumber;
    this.owner = owner;
    this.roomId = roomId;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUnitType() {
    return unitType;
  }

  public void setUnitType(String unitType) {
    this.unitType = unitType;
  }

  public int getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(int unitNumber) {
    this.unitNumber = unitNumber;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

}
