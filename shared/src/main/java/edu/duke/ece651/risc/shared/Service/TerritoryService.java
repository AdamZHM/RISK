package edu.duke.ece651.risc.shared.Service;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.Territory;
import edu.duke.ece651.risc.shared.Unit;
import edu.duke.ece651.risc.shared.Configure.SingletonSqlFactory;
import edu.duke.ece651.risc.shared.Entity.Territorys;
import edu.duke.ece651.risc.shared.Mapper.TerritoryMapper;

public class TerritoryService {
  /**
   * Get instance of sqlsession
   * 
   * @return SqlSession
   */
  private SqlSession getSerilizableSession() {
    return SingletonSqlFactory.getSingletonInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
  }

  /**
   * this function to update the map info in database
   * 
   * @param gameMap game map object
   * @param roomId  room id
   */
  public void updateMap(GameMap gameMap, int roomId) {
    SqlSession session = this.getSerilizableSession();
    TerritoryMapper territoryMapper = session.getMapper(TerritoryMapper.class);
    Set<String> territoryNames = gameMap.getTerritoryNames();
    for (String territoryName : territoryNames) {
      Territory territory = gameMap.getTerritoryByName(territoryName);
      for (String unitType : Unit.unitNameList) {
        Territorys territorys = new Territorys(territoryName, unitType, territory.getUnitByName(unitType).getNum(),
            territory.getOwner(), roomId);
        territoryMapper.saveTerritory(territorys);
      }
    }
    session.commit();
  }

  /**
   * save the territory info into database
   * 
   * @param territorys
   */
  // public void saveTerritory(Territorys territorys) {
  // SqlSession session = this.getSerilizableSession();
  // TerritoryMapper territoryMapper = session.getMapper(TerritoryMapper.class);
  // territoryMapper.saveTerritory(territorys);
  // session.commit();
  // }

  // public void deleteTerritory(Territorys territorys) {
  // SqlSession session = this.getSerilizableSession();
  // TerritoryMapper territoryMapper = session.getMapper(TerritoryMapper.class);
  // territoryMapper.deleteTerritory(territorys);
  // session.commit();
  // }

  /**
   * select one territory
   * 
   * @param territorys
   * @return
   */
  public Territorys selectTerritory(Territorys territorys) {
    SqlSession session = this.getSerilizableSession();
    TerritoryMapper territoryMapper = session.getMapper(TerritoryMapper.class);
    Territorys territorys2 = territoryMapper.selectTerritory(territorys);
    session.commit();
    return territorys2;
  }

  /**
   * get territory list according to the room id
   * 
   * @param roomId
   * @return territory list
   */
  public List<Territorys> selectTerritorysByRoomId(int roomId) {
    SqlSession session = this.getSerilizableSession();
    TerritoryMapper territoryMapper = session.getMapper(TerritoryMapper.class);
    List<Territorys> res = territoryMapper.selectTerritorysByRoomId(roomId);
    session.commit();
    return res;
  }

  /**
   * recover GameMap object when the server restart
   * 
   * @param playerNum player number of this room
   * @param roomId    room id
   * @return gamemap
   */
  public GameMap recoverGampeMap(int playerNum, int roomId) {
    GameMap gameMap = new GameMap(playerNum);
    List<Territorys> res = selectTerritorysByRoomId(roomId);
    for (Territorys t : res) {
      String territoryName = t.getName();
      Territory territory = gameMap.getTerritoryByName(territoryName);
      territory.setOwner(t.getOwner());
      int unitNum = t.getUnitNumber();
      // System.out.println(territoryName + " " + unitNum + " " + t.getUnitType());
      territory.getUnitByName(t.getUnitType()).setNum(unitNum);
    }
    return gameMap;
  }
}
