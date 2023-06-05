package edu.duke.ece651.risc.shared.Mapper;

import java.util.Set;

import org.apache.ibatis.annotations.*;

import edu.duke.ece651.risc.shared.Entity.Player;

public interface PlayerMapper {
  final String registerPlayer = "insert into player (name, password, score) values (#{name}, #{password}, #{score});";

  /**
   * Insert player into account
   * 
   * @param player player object
   */
  @Insert(registerPlayer)
  void registerPlayer(Player player);

  @Select("SELECT * FROM player WHERE name=#{name}")
  Player ifPlayerExisted(String name);

  @Select("SELECT * FROM player WHERE name=#{name} and password = #{password}")
  Player playerLogin(Player player);

  @Delete("delete from player where name = #{name}")
  void deletePlayer(String name);

  @Select("SELECT score FROM player WHERE name=#{name}")
  int selectPlayerScore(String name);

  @Select("SELECT * FROM player WHERE name=#{name}")
  Player selectPlayer(String name);

  @Update("update player set score = #{score} where name = #{name}")
  void updatePlayerScore(Player player);
}
