package edu.duke.ece651.risc.shared.EntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.VisionHelper;
import edu.duke.ece651.risc.shared.Entity.Vision;
import edu.duke.ece651.risc.shared.Service.VisionService;

public class VisionTest {
  @Test
  public void test(){
    Vision vision = new Vision(1, 1, "a");
    Vision vision2 = new Vision(1, 1, 1, "territoryName", 1, "visionInfo", 1);
    Vision vision3 = new Vision(1, 1, "territoryName", 1, "visionInfo", 1);
    assertEquals(1, vision2.getCloakingRound());
    assertEquals(1, vision3.getSpyNum());
    assertEquals("visionInfo", vision3.getVisionInfo());
    VisionService visionService = new VisionService();
    GameMap gameMap = new GameMap(2);
    // visionService.insertIntoVision(new VisionHelper(), gameMap, 1, 1);
  }
}
