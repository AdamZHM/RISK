package edu.duke.ece651.risc.shared.MapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Configure.SingletonSqlFactory;
import edu.duke.ece651.risc.shared.Entity.Player;
import edu.duke.ece651.risc.shared.Service.PlayerService;

public class PlayerMapperTest {

  @Test
  public void test() {
    PlayerService playerService = new PlayerService();
    Player player = new Player("a", "a");
    boolean res = playerService.register(player);
    boolean res1 = playerService.login(player);
    Player player1 = new Player("b", "a");
    Player player3 = new Player("test", "test");
    boolean res3 = playerService.register(player3);
    assertEquals(true, res3);

    boolean res2 = playerService.login(player1);
    assertEquals(false, res);
    assertEquals(true, res1);
    assertEquals(false, res2);

    // assertEquals(1500, player.getScore());
    // player.setScore(playerService.getWinnerE("b", "a"));

    System.out.println(player.getScore());
    Set<String> set = new HashSet<>();
    set.add("a");
    set.add("c");
    playerService.calcScore("b", set);
    Set<String> set1 = new HashSet<>();
    set1.add("username");
    // System.out.println(playerService.getLoserE("a", "b"));
    // assertEquals(1520, player.getScore());
    assertEquals(true, playerService.playersMatchable("test", set1));
    playerService.deletePlayer("test");
    assertEquals(1500, playerService.playerScore("username"));
  }
}
