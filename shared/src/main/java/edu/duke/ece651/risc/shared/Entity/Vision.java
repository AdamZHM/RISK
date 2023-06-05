package edu.duke.ece651.risc.shared.Entity;

public class Vision {
  Integer id;

  Integer roomId;

  Integer playerId;

  String territoryName;

  Integer cloakingRound;

  String visionInfo;

  Integer spyNum;

  public Vision(Integer roomId, Integer playerId, String territoryName) {
    this.roomId = roomId;
    this.playerId = playerId;
    this.territoryName = territoryName;
  }

  public Vision(Integer id, Integer roomId, Integer playerId, String territoryName, Integer cloakingRound,
      String visionInfo, Integer spyNum) {
    this.id = id;
    this.roomId = roomId;
    this.playerId = playerId;
    this.territoryName = territoryName;
    this.cloakingRound = cloakingRound;
    this.visionInfo = visionInfo;
    this.spyNum = spyNum;
  }

  public Vision(Integer roomId, Integer playerId, String territoryName, Integer cloakingRound, String visionInfo,
      Integer spyNum) {
    this.roomId = roomId;
    this.playerId = playerId;
    this.territoryName = territoryName;
    this.cloakingRound = cloakingRound;
    this.visionInfo = visionInfo;
    this.spyNum = spyNum;
  }

  public Integer getCloakingRound() {
    return this.cloakingRound;
  }

  public String getVisionInfo() {
    return this.visionInfo;
  }

  public Integer getSpyNum() {
    return this.spyNum;
  }

}
