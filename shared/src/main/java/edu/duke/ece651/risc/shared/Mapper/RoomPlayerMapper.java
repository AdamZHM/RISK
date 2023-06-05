package edu.duke.ece651.risc.shared.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import edu.duke.ece651.risc.shared.Entity.RoomPlayer;

public interface RoomPlayerMapper {
  @Select("select * from room_player where player_name = #{playerName}")
  public List<RoomPlayer> selectJoinedRooms(@Param("playerName") String playerName);

  @Insert("insert into room_player (room_id, player_id, player_name, food, tech_resource, tech_level, cloak) values (#{roomId}, #{playerId}, #{playerName}, #{food}, #{techResource}, #{techLevel}, #{cloak})")
  public void saveOnePlayerToRoom(RoomPlayer roomPlayer);

  @Delete("delete from room_player where room_id = #{roomId} and player_name = #{playerName}")
  public void deleteOnePlayerInRoom(RoomPlayer roomPlayer);

  @Update("update room_player set round = round + 1, action = #{action} where room_id = #{roomId} and player_name = #{playerName}")
  public void updatePlayerActionAndRound(RoomPlayer roomPlayer);

  @Update("update room_player set cloak = #{cloak}, food = #{food}, tech_resource = #{techResource}, tech_level = #{techLevel} where room_id = #{roomId} and player_name = #{playerName}")
  public void updateResource(RoomPlayer roomPlayer);

  // @Update("update room_player set cloak = #{cloak} where room_id = #{roomId} and player_name = #{playerName}")
  // public void updateCloak(RoomPlayer roomPlayer);

  @Select("select * from room_player where room_id = #{roomId}")
  public List<RoomPlayer> selectAllByRoomId(int roomId);
}
