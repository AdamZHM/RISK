package edu.duke.ece651.risc.shared.Service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;

import edu.duke.ece651.risc.shared.Configure.SingletonSqlFactory;
import edu.duke.ece651.risc.shared.Entity.RoomPlayer;
import edu.duke.ece651.risc.shared.Mapper.RoomMapper;
import edu.duke.ece651.risc.shared.Mapper.RoomPlayerMapper;

public class RoomPlayerService {
  /**
   * Get sqlSession with the designated transcation isolation level
   * 
   * @return sql session
   */
  private SqlSession getSerilizableSession() {
    return SingletonSqlFactory.getSingletonInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
  }

  /**
   * select the joined room of the player
   * 
   * @param playerName player name
   * @return the list of player names
   */
  public List<RoomPlayer> selectJoinedRooms(String playerName) {
    SqlSession session = this.getSerilizableSession();
    RoomPlayerMapper roomPlayerMapper = session.getMapper(RoomPlayerMapper.class);
    List<RoomPlayer> res = roomPlayerMapper.selectJoinedRooms(playerName);
    session.commit();
    return res;
  }

  /**
   * save the player to a room
   * 
   * @param roomId       room id
   * @param playerId     player id in this room
   * @param playerName   the player name
   * @param food         food resource
   * @param techResource tech resource
   * @param techLevel    tehc level
   */
  public void saveOnePlayerToRoom(int roomId, int playerId, String playerName, int food, int techResource,
      int techLevel) {
    SqlSession session = this.getSerilizableSession();
    RoomPlayerMapper roomPlayerMapper = session.getMapper(RoomPlayerMapper.class);
    RoomPlayer roomPlayer = new RoomPlayer(roomId, playerId, playerName, food, techResource, techLevel, false);
    roomPlayerMapper.saveOnePlayerToRoom(roomPlayer);
    session.commit();
  }

  /**
   * this function is used in test to delele one player in a room
   * 
   * @param roomPlayer roomPlayer object
   */
  public void deleteOnePlayerInRoom(RoomPlayer roomPlayer) {
    SqlSession session = this.getSerilizableSession();
    RoomPlayerMapper roomPlayerMapper = session.getMapper(RoomPlayerMapper.class);
    roomPlayerMapper.deleteOnePlayerInRoom(roomPlayer);
    session.commit();
  }

  /**
   * get all players in the room
   * 
   * @param roomId room id
   * @return list of players in the room
   */
  public List<RoomPlayer> selectAllByRoomId(int roomId) {
    SqlSession session = this.getSerilizableSession();
    RoomPlayerMapper roomPlayerMapper = session.getMapper(RoomPlayerMapper.class);
    List<RoomPlayer> res = roomPlayerMapper.selectAllByRoomId(roomId);
    // System.out.println(res.size());
    session.commit();
    return res;
  }

  /**
   * update the resource of food, techResource, techLevel
   * 
   * @param roomPlayer roomPlayer object
   */
  public void updateResource(RoomPlayer roomPlayer) {
    SqlSession session = this.getSerilizableSession();
    RoomPlayerMapper roomPlayerMapper = session.getMapper(RoomPlayerMapper.class);
    roomPlayerMapper.updateResource(roomPlayer);
    session.commit();
  }
}
