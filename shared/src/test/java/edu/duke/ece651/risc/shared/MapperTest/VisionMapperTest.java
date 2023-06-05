package edu.duke.ece651.risc.shared.MapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.GameMap;
import edu.duke.ece651.risc.shared.VisionHelper;
import edu.duke.ece651.risc.shared.Entity.Vision;
import edu.duke.ece651.risc.shared.Service.TerritoryService;
import edu.duke.ece651.risc.shared.Service.VisionService;

public class VisionMapperTest {
  @Test
  public void test() {
    VisionService visionService = new VisionService();
    TerritoryService territoryService = new TerritoryService();
    // Vision vision = new Vision(1, 1, "Oz", 23, "a", 1);
    // visionService.updateVision(vision);
    // Vision vision2 = visionService.selectVision(vision);
    // assertEquals(23, vision2.getCloakingRound());
    GameMap gameMap = territoryService.recoverGampeMap(2, 211);
    VisionHelper visionHelper = visionService.recoverVisionHelper(gameMap, 0, 211);
    visionService.insertIntoVision(visionHelper, gameMap, 1, 1);
    visionService.updateVision(visionHelper, gameMap, 1, 1);
    
  }
}
