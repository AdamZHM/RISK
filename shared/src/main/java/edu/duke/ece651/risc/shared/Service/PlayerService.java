package edu.duke.ece651.risc.shared.Service;

import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;

import edu.duke.ece651.risc.shared.Configure.SingletonSqlFactory;
import edu.duke.ece651.risc.shared.Entity.Player;
import edu.duke.ece651.risc.shared.Mapper.PlayerMapper;

public class PlayerService {
  /**
   * Get sqlSession with the designated transcation isolation level
   * 
   * @return sql session
   */
  private SqlSession getSerilizableSession() {
    return SingletonSqlFactory.getSingletonInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
  }

  /**
   * This function is to register a player
   * 
   * @param player
   * @return false if player existed, true if player registered successfully
   */
  public Boolean register(Player player) {
    try (SqlSession session = this.getSerilizableSession()) {
      PlayerMapper playerMapper = session.getMapper(PlayerMapper.class);

      Player ifPlayerExisted = playerMapper.ifPlayerExisted(player.getName());
      if (ifPlayerExisted != null) {
        return false;
      } else {
        playerMapper.registerPlayer(player);
      }
      session.commit();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return true;
  }

  /**
   * This function is to login, a simple CRUD
   * 
   * @param player
   * @return true if login successfully
   */
  public boolean login(Player player) {
    try (SqlSession session = this.getSerilizableSession()) {
      PlayerMapper playerMapper = session.getMapper(PlayerMapper.class);

      Player ifPlayerExisted = playerMapper.playerLogin(player);
      System.out.println(ifPlayerExisted);
      if (null != ifPlayerExisted) {
        return true;
      }
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * This function is to delete player
   * 
   * @param name player name
   */
  public void deletePlayer(String name) {
    SqlSession session = this.getSerilizableSession();
    PlayerMapper playerMapper = session.getMapper(PlayerMapper.class);
    playerMapper.deletePlayer(name);
    session.commit();
  }

  /**
   * get player score
   * 
   * @param name player name
   * @return player score
   */
  public int playerScore(String name) {
    SqlSession session = this.getSerilizableSession();
    PlayerMapper playerMapper = session.getMapper(PlayerMapper.class);
    int res = playerMapper.selectPlayerScore(name);
    session.commit();
    return res;
  }

  /**
   * the score added to winner when the winner win the game according
   * to elo system
   * 
   * @param winnerScore winner score
   * @param loserScore  loser score
   * @return the score to add
   */
  public int getWinnerE(int winnerScore, int loserScore) {
    double E = getE(winnerScore, loserScore);
    double res = (1 - E) * 32;
    return (int) res;
  }

  /**
   * get the win probability of winner according to elo system
   * 
   * @param winner winner score
   * @param loser  loser score
   * @return win probability of winner
   */
  public double getE(int winner, int loser) {
    double res = 1 / (1 + Math.pow(10, (loser - winner) / 400.0));
    return res;
  }

  /**
   * setup the winner and loser score in database
   * 
   * @param winnerName
   * @param playerNames
   */
  public void calcScore(String winnerName, Set<String> playerNames) {
    SqlSession session = this.getSerilizableSession();
    PlayerMapper playerMapper = session.getMapper(PlayerMapper.class);
    Player winner = playerMapper.selectPlayer(winnerName);
    int winnerScore = winner.getScore();
    for (String playerName : playerNames) {
      Player loser = playerMapper.selectPlayer(playerName);
      int score = getWinnerE(winnerScore, loser.getScore());
      if (!playerName.equals(winnerName)) {
        winner.setScore(winner.getScore() + score);
        loser.setScore(loser.getScore() - score);
        playerMapper.updatePlayerScore(winner);
        playerMapper.updatePlayerScore(loser);
      }
    }
    session.commit();
  }

  /**
   * this function is to check if two players can play in one room
   * 
   * @param playerName
   * @param playersInRoom the players already in room
   * @return true if they can, false if the difference of two players larger than
   *         200
   */
  public boolean playersMatchable(String playerName, Set<String> playersInRoom) {
    SqlSession session = this.getSerilizableSession();
    PlayerMapper playerMapper = session.getMapper(PlayerMapper.class);
    for (String player : playersInRoom) {
      int score1 = playerMapper.selectPlayerScore(playerName);
      int score2 = playerMapper.selectPlayerScore(player);
      if (Math.abs(score1 - score2) > 200) {
        return false;
      }
    }
    return true;
  }
}
