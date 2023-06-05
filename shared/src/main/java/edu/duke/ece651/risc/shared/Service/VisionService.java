package edu.duke.ece651.risc.shared.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.VisionHelper;
import edu.duke.ece651.risc.shared.Configure.SingletonSqlFactory;
import edu.duke.ece651.risc.shared.Entity.Vision;
import edu.duke.ece651.risc.shared.Mapper.VisionMapper;
import edu.duke.ece651.risc.shared.Utils.PlayerId2ColorHelper;

public class VisionService {
  /**
   * Get instance of sqlsession
   * 
   * @return SqlSession
   */
  private SqlSession getSerilizableSession() {
    return SingletonSqlFactory.getSingletonInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
  }

  /**
   * insert all the vision of each player into the database,
   * which is used to recover from restart
   * 
   * @param visionHelper
   * @param gameMap      gameMap object
   * @param roomId
   * @param id
   */
  public void insertIntoVision(VisionHelper visionHelper, GameMap gameMap, int roomId, int id) {
    SqlSession session = this.getSerilizableSession();
    VisionMapper visionMapper = session.getMapper(VisionMapper.class);
    for (Map.Entry<String, Territory> m : gameMap.getMyTerritories().entrySet()) {
      int cloakingRound = visionHelper.getCloakInfo().get(m.getKey());
      String visionInfo = visionHelper.getVisionInfo().get(m.getKey());
      int spyNum = visionHelper.getSpyInfo().get(m.getKey());
      Vision vision = new Vision(roomId, id, m.getKey(), cloakingRound, visionInfo, spyNum);
      visionMapper.insertIntoVision(vision);
    }
    session.commit();
  }

  /**
   * update vision in database after one round
   * 
   * @param visionHelper
   * @param gameMap
   * @param roomId
   * @param id
   */
  public void updateVision(VisionHelper visionHelper, GameMap gameMap, int roomId, int id) {
    SqlSession session = this.getSerilizableSession();
    VisionMapper visionMapper = session.getMapper(VisionMapper.class);
    for (Map.Entry<String, Territory> m : gameMap.getMyTerritories().entrySet()) {
      int cloakingRound = visionHelper.getCloakInfo().get(m.getKey());
      String visionInfo = visionHelper.getVisionInfo().get(m.getKey());
      int spyNum = visionHelper.getSpyInfo().get(m.getKey());
      Vision vision = new Vision(roomId, id, m.getKey(), cloakingRound, visionInfo, spyNum);
      visionMapper.updateVision(vision);
    }
    session.commit();
  }

  /**
   * after the restart of server, this function will be called and VisionHelper
   * will be recovered
   * 
   * @param gameMap
   * @param playerId
   * @param roomId
   * @return VisionHelper of certain player in one room
   */
  public VisionHelper recoverVisionHelper(GameMap gameMap, int playerId, int roomId) {
    SqlSession session = this.getSerilizableSession();
    VisionMapper visionMapper = session.getMapper(VisionMapper.class);
    VisionHelper res = new VisionHelper(gameMap, PlayerId2ColorHelper.playerId2Color.get(playerId));
    Map<String, Integer> cloakInfo = new HashMap<>();
    Map<String, String> visionInfo = new HashMap<>();
    Map<String, Integer> spyInfo = new HashMap<>();
    for (Entry<String, Territory> m : gameMap.getMyTerritories().entrySet()) {
      String territoryName = m.getKey();
      Vision vision = visionMapper.selectVision(new Vision(roomId, playerId, territoryName));
      // System.out.println(vision.getCloakingRound() + " " + vision.getVisionInfo());
      cloakInfo.put(territoryName, vision.getCloakingRound());
      visionInfo.put(territoryName, vision.getVisionInfo());
      spyInfo.put(territoryName, vision.getSpyNum());
      res.cloakInfo = cloakInfo;
      res.visionInfo = visionInfo;
      res.spyInfo = spyInfo;
      res.owner = PlayerId2ColorHelper.playerId2Color.get(playerId);
    }
    return res;
  }
}
