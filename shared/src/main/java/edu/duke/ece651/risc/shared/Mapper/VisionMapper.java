package edu.duke.ece651.risc.shared.Mapper;

import org.apache.ibatis.annotations.*;

import edu.duke.ece651.risc.shared.Entity.Vision;

public interface VisionMapper {
  @Insert("insert into vision (room_id, player_id, territory_name, cloaking_round, vision_info, spy_num) " +
      "values(#{roomId}, #{playerId}, #{territoryName}, #{cloakingRound}, #{visionInfo}, #{spyNum})")
  void insertIntoVision(Vision vision);

  @Update("update vision set cloaking_round = #{cloakingRound}, vision_info = #{visionInfo}, spy_num = #{spyNum}" +
      " where room_id = #{roomId} and player_id = #{playerId} and territory_name = #{territoryName}")
  void updateVision(Vision vision);

  @Select("select * from vision where room_id = #{roomId} and player_id = #{playerId} and territory_name = #{territoryName}")
  Vision selectVision(Vision vision);
}
