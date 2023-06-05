package edu.duke.ece651.risc.shared.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import edu.duke.ece651.risc.shared.Entity.Territorys;

public interface TerritoryMapper {

  @Insert("INSERT INTO territorys ( name, unit_type, unit_number, owner, room_id ) " +
  "VALUES( #{name}, #{unitType}, #{unitNumber}, #{owner}, #{roomId} ) " +
  "ON CONFLICT ( name, unit_type, room_id ) DO " +
  "UPDATE SET owner = #{owner}, unit_number = #{unitNumber}")
  public void saveTerritory(Territorys territorys);

  @Delete("delete from territorys where name = #{name} and unit_type = #{unitType} and room_id = #{roomId}")
  public void deleteTerritory(Territorys territorys);

  @Select("select * from territorys where name = #{name} and unit_type = #{unitType} and room_id = #{roomId}")
  public Territorys selectTerritory(Territorys territorys);

  @Select("select * from territorys where room_id = #{roomId}")
  public List<Territorys> selectTerritorysByRoomId(int roomId);
}
