package edu.duke.ece651.risc.shared.Service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;

import edu.duke.ece651.risc.shared.Configure.SingletonSqlFactory;
import edu.duke.ece651.risc.shared.Entity.Room;
import edu.duke.ece651.risc.shared.Mapper.RoomMapper;

public class RoomService {
  /**
   * Get instance of sqlsession
   * 
   * @return SqlSession
   */
  private SqlSession getSerilizableSession() {
    return SingletonSqlFactory.getSingletonInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
  }

  /**
   * This function is to save a room into database
   * 
   * @param room room object
   * @return true if room saved, false if the room name is already there
   */
  public boolean saveRoom(Room room) {
    SqlSession session = this.getSerilizableSession();
    if (selectRoom(room.getname()) != null) {
      return false;
    }
    RoomMapper roomMapper = session.getMapper(RoomMapper.class);
    roomMapper.saveRoom(room);
    session.commit();
    return true;
  }

  /**
   * select room by room name
   * 
   * @param roomName
   * @return Room object
   */
  public Room selectRoom(String roomName) {
    SqlSession session = this.getSerilizableSession();
    RoomMapper roomMapper = session.getMapper(RoomMapper.class);
    Room room = new Room(roomName);
    Room res = roomMapper.selectRoom(room);
    session.commit();
    return res;
  }

  /**
   * This function is to update room round by plus 1
   * 
   * @param roomName name of the room
   */
  public void updateRoomRound(String roomName) {
    SqlSession session = this.getSerilizableSession();
    RoomMapper roomMapper = session.getMapper(RoomMapper.class);
    roomMapper.updateRoomRound(roomName);
    session.commit();
  }

  /**
   * This function is to select all the rooms in database
   * 
   * @return rooms in database
   */
  public List<Room> selectAllRooms() {
    SqlSession session = this.getSerilizableSession();
    RoomMapper roomMapper = session.getMapper(RoomMapper.class);
    List<Room> res = roomMapper.selectAllRooms();
    session.commit();
    return res;
  }

  /**
   * This function is to clear all rooms that have been created and not started
   * yet, but server shut down.
   */
  public void clearUpRooms() {
    SqlSession session = this.getSerilizableSession();
    RoomMapper roomMapper = session.getMapper(RoomMapper.class);
    roomMapper.clearUpRooms();
    session.commit();
  }

  /**
   * helper function, delete room
   * 
   * @param name room name
   */
  public void deleteRoom(String name) {
    SqlSession session = this.getSerilizableSession();
    RoomMapper roomMapper = session.getMapper(RoomMapper.class);
    roomMapper.deleteRoom(name);
    session.commit();

  }
}
