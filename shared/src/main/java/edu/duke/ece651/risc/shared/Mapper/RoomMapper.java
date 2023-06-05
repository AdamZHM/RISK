package edu.duke.ece651.risc.shared.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import edu.duke.ece651.risc.shared.Entity.Room;

public interface RoomMapper {
  @Insert("insert into room (name, player_num, round) values (#{name}, #{playerNum}, #{round})")
  public void saveRoom(Room room);

  @Select("select * from room where name = #{name}")
  public Room selectRoom(Room room);

  @Update("update room set round = round + 1 where name = #{roomName}")
  public void updateRoomRound(String roomName);

  @Select("select * from room")
  public List<Room> selectAllRooms();

  @Delete("Delete from room where round = 0")
  public void clearUpRooms();

  @Delete("delete from room where name = #{name}")
  public void deleteRoom(String name);
}
