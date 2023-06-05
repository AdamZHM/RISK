package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

public class ActionCheckerTest {

  private GameMap test_helper() {
    GameMap m = new GameMap(3);
    Map<String, Territory> mt = m.getMyTerritories();
    mt.get("Scadrial").setOwner("Player1");
    mt.get("Scadrial").getUnitByName("soldier").setNum(5);
    mt.get("Roshar").setOwner("Player1");
    mt.get("Roshar").getUnitByName("soldier").setNum(3);
    mt.get("Mordor").setOwner("Player2");
    mt.get("Mordor").getUnitByName("soldier").setNum(14);
    mt.get("Narnia").setOwner("Player1");
    mt.get("Narnia").getUnitByName("soldier").setNum(16);
    mt.get("Oz").setOwner("Player2");
    mt.get("Oz").getUnitByName("soldier").setNum(8);
    mt.get("Midkemia").setOwner("Player2");
    mt.get("Midkemia").getUnitByName("soldier").setNum(12);
    return m;
  }

  @Test
  public void test_move_check() {
    GameMap map = test_helper();

    Map<String, ResourceHelper> resourceHelpers = getRawResourceHelpers();
    ResourceHelper resourceHelper1 = resourceHelpers.get("Player1");
    ActionChecker mov_check = new ActionChecker(map, resourceHelpers, getVisionHelpers());
    resourceHelper1.updateResoursePerRound(map, "Player1");
    ResourceHelper resourceHelper2 = resourceHelpers.get("Player2");
    resourceHelper2.updateResoursePerRound(map, "Player2");


    System.out.println(resourceHelper1.getResourcesAttr());
    System.out.println(resourceHelper2.getResourcesAttr());

    MoveAction ac1 = new MoveAction(3, "Roshar", "Scadrial", "Player1", "soldier", null);
    assertEquals(null, mov_check.check(ac1));
    MoveAction ac7 = new MoveAction(2, "Mordor", "Midkemia", "Player2", "soldier", null);
    assertEquals(null, mov_check.check(ac7));
    MoveAction ac8 = new MoveAction(5, "Mordor", "Midkemia", "Player2", "soldier", null);
    assertEquals("Food resources is insufficient for action", mov_check.check(ac8));
    MoveAction ac5 = new MoveAction(3, "Ros", "Scadrial", "Player1", "soldier", null);
    assertEquals("Invalid territory Ros", mov_check.check(ac5));
    MoveAction ac6 = new MoveAction(3, "Roshar", "Scadrial", "Player1", "solid", null);
    assertEquals("solid unit does not exist", mov_check.check(ac6));
    MoveAction ac2 = new MoveAction(4, "Roshar", "Scadrial", "Player1", "soldier", null);
    assertEquals("Invalid Unit num input soldier 4", mov_check.check(ac2));
    MoveAction ac3 = new MoveAction(3, "Roshar", "Scadrial", "Player2", "soldier", null);
    assertEquals("Roshar Territory isn't own by you and illegal", mov_check.check(ac3));
    MoveAction ac4 = new MoveAction(3, "Scadrial", "Narnia", "Player1", "soldier", null);
    assertEquals("Scadrial is not linked to Narnia", mov_check.check(ac4));
  }

  @Test
  public void test_attack_check() {
    GameMap map = test_helper();
    Map<String, ResourceHelper> resourceHelpers = getRawResourceHelpers();
    ResourceHelper resourceHelper = resourceHelpers.get("Player1");
    ActionChecker attack_check = new ActionChecker(map, resourceHelpers, getVisionHelpers());
    resourceHelper.updateResoursePerRound(map, "Player1");
    AttackAction ac1 = new AttackAction(5, "Scadrial", "Mordor", "Player1", "soldier", null);
    assertEquals(null, attack_check.check(ac1));
    AttackAction ac2 = new AttackAction(16, "Narnia", "Midkemia", "Player1", "soldier", null);
    assertEquals("Food resources is insufficient for action", attack_check.check(ac2));
    AttackAction ac5 = new AttackAction(5, "Scadrial", "Roshar", "Player1", "soldier", null);
    assertEquals("Roshar Territory is own by you and illegal", attack_check.check(ac5));
    AttackAction ac6 = new AttackAction(3, "Roshar", "Mordor", "Player1", "soldier", null);
    assertEquals("Roshar is not adjacent to Mordor", attack_check.check(ac6));
    // AttackAction ac2=new AttackAction(4,"Roshar","Scadrial",
    // "Player1","soldier",null);
    // assertEquals("Invalid Unit num input 4", attack_check.check(ac2));
    // AttackAction ac3=new AttackAction(3,"Roshar","Scadrial",
    // "Player2","soldier",null);
    // assertEquals("Roshar Territory isn't own by you and illegal",
    // attack_check.check(ac3));
    // AttackAction ac4=new AttackAction(3,"Mordor","Narnia",
    // "Player2","soldier",null);
    // assertEquals("Mordor is not linked to Narnia", attack_check.check(ac4));

  }

  @Test
  public void test_move_spy_check() {
    GameMap map = test_helper();
    Map<String, ResourceHelper> resourceHelpers = getRawResourceHelpers();
    ResourceHelper resourceHelper = resourceHelpers.get("Player1");
    ActionChecker move_spy_check = new ActionChecker(map, resourceHelpers, getVisionHelpers());
    resourceHelper.updateResoursePerRound(map, "Player1");
    move_spy_check.visionHelpers.get("Player1").getSpyInfo().put("Scadrial",2);
    move_spy_check.visionHelpers.get("Player1").getSpyInfo().put("Mordor",2);
    move_spy_check.visionHelpers.get("Player1").getSpyInfo().put("Roshar",100);
    MoveSpyAction ac1 = new MoveSpyAction("Scadrial", "Mordor", 2,"Player1", null);
    assertEquals(null, move_spy_check.check(ac1));
    MoveSpyAction ac2 = new MoveSpyAction("Mordor", "Oz", 2,"Player1", null);
    assertEquals("Your have no sufficient Spy to move in this territory", move_spy_check.check(ac2));
    MoveSpyAction ac5 = new MoveSpyAction("Narnia", "Midkemia", 2,"Player1", null);
    assertEquals("Your Spy not exist in this territory", move_spy_check.check(ac5));
    MoveSpyAction ac6 = new MoveSpyAction("Roshar", "Mordor", 2,"Player1", null);
    assertEquals("Roshar is not adjacent to Mordor", move_spy_check.check(ac6));
    MoveSpyAction ac7 = new MoveSpyAction("Roshar", "Scadrial", 100,"Player1", null);
    assertEquals("Insufficient food resource", move_spy_check.check(ac7));

  }

  private Map<String, ResourceHelper> getRawResourceHelpers() {
    Map<String, ResourceHelper> res = new HashMap<>(){{
      put("Player1", new ResourceHelper());
      put("Player2", new ResourceHelper());
      put("Player3", new ResourceHelper());
    }};
    return res;
  }

  private Map<String, ResourceHelper> getResourceHelpers() {
    Map<String, ResourceHelper> res = new HashMap<>(){{
      put("Player1", new ResourceHelper(3, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
      put("Player2", new ResourceHelper(3,new HashMap<>(){{put("food", 100); put("technology", 100);}}));
      put("Player3", new ResourceHelper(2, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
    }};
    return res;
  }

  public Map<String, VisionHelper> getVisionHelpers() {
    Map<String, VisionHelper> res = new HashMap<>() {{
      put("Player1", new VisionHelper(test_helper(), "Player1"));
      put("Player2", new VisionHelper(test_helper(), "Player2"));
      put("Player3", new VisionHelper(test_helper(), "Player3"));
    }};
    return res;
  }

  public Map<String, VisionHelper> getVisionHelpersForMapHelper() {
    Map<String, VisionHelper> res = new HashMap<>() {{
      put("Player1", new VisionHelper(getMapHelper(), "Player1"));
      put("Player2", new VisionHelper(getMapHelper(), "Player2"));
      put("Player3", new VisionHelper(getMapHelper(), "Player3"));
    }};
    return res;
  }

  private GameMap getMapHelper() {
    GameMap m = new GameMap(3);
    Map<String, Territory> mt = m.getMyTerritories();
    mt.get("Scadrial").setOwner("Player1");
    mt.get("Scadrial").getUnitByName("soldier").setNum(5);
    mt.get("Roshar").setOwner("Player1");
    mt.get("Roshar").getUnitByName("soldier").setNum(3);
    mt.get("Mordor").setOwner("Player2");
    mt.get("Mordor").getUnitByName("soldier").setNum(14);
    mt.get("Narnia").setOwner("Player1");
    mt.get("Narnia").getUnitByName("soldier").setNum(16);
    mt.get("Oz").setOwner("Player2");
    mt.get("Oz").getUnitByName("soldier").setNum(8);
    mt.get("Midkemia").setOwner("Player2");
    mt.get("Midkemia").getUnitByName("soldier").setNum(12);
    return m;
}

  @Test
  public void test_check_unitupgrade(){
    GameMap gameMap = getMapHelper();
    Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
    ActionChecker actionChecker = new ActionChecker(gameMap, resourceHelpers, getVisionHelpersForMapHelper());
    UpgradeInfo upgradeInfo1 = new UpgradeInfo("Scadrial", "soldier", "captain", 4);
    UpgradeInfo upgradeInfo2 = new UpgradeInfo("Scadrial", "soldier", "commander", 1);
    UpgradeInfo upgradeInfo3 = new UpgradeInfo("Scadrial", "warrior", "soldier", 0);
    UpgradeUnitAction upgradeUnitAction1 = new UpgradeUnitAction("Player1", null, upgradeInfo1);
    assertEquals("Insufficient technology resources for upgrade" , actionChecker.check(upgradeUnitAction1));
    assertEquals(resourceHelpers.get("Player1").getResourcesAttr().get("technology"), 100);
    UpgradeUnitAction upgradeUnitAction2 = new UpgradeUnitAction("Player1", null, upgradeInfo2);
    assertEquals("You technology level is insufficient for this upgrade" , actionChecker.check(upgradeUnitAction2));

    UpgradeUnitAction upgradeUnitAction3 = new UpgradeUnitAction("Player1", null, upgradeInfo3);
    assertEquals("Cannot upgrade to lower level" , actionChecker.check(upgradeUnitAction3));
    
  }

  @Test
  public void test_check_spyupgrade(){
    GameMap gameMap = getMapHelper();
    Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
    ActionChecker actionChecker = new ActionChecker(gameMap, resourceHelpers, getVisionHelpersForMapHelper());
    UpgradeSpyAction UpgradeSpyAction1 = new UpgradeSpyAction("Narnia","soldier",10,"Player1", null);
    assertEquals("Insufficient technology resource" , actionChecker.check(UpgradeSpyAction1));
    assertEquals(resourceHelpers.get("Player1").getResourcesAttr().get("technology"), 100);
    UpgradeSpyAction UpgradeSpyAction2 = new UpgradeSpyAction("Scadrial","soldier",5,"Player1", null);
    assertEquals(null , actionChecker.check(UpgradeSpyAction2));
    UpgradeSpyAction UpgradeSpyAction3 = new UpgradeSpyAction("Scadrial","soldier",6,"Player1", null);   
    assertEquals("Invalid Unit num input soldier 6" , actionChecker.check(UpgradeSpyAction3));
    
  }

  @Test
  public void test_check_Levelupgrade(){
    GameMap gameMap = getMapHelper();
    Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
    ActionChecker actionChecker = new ActionChecker(gameMap, resourceHelpers, getVisionHelpersForMapHelper());
    UpgradeTechAction upgradeTech = new UpgradeTechAction("Player1", null);

    assertEquals("Technology resources is insufficient for upgrade" , actionChecker.check(upgradeTech));
    
  }

  @Test
  public void test_check_cloak(){
    GameMap gameMap = getMapHelper();
    Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
    ActionChecker actionChecker = new ActionChecker(gameMap, resourceHelpers, getVisionHelpersForMapHelper());
    CloakingAction cloakingAction = new CloakingAction("Scadrial","Player1", null);
    assertEquals("Need research cloaking first" , actionChecker.check(cloakingAction));
    UpgradeCloakAction upgradeCloakAction =new UpgradeCloakAction("Player1");
    UpgradeCloakAction upgradeCloakAction3 =new UpgradeCloakAction("Player3");
    assertEquals("Insufficient Tech Level to research Cloak" , actionChecker.check(upgradeCloakAction3));
    assertEquals("Insufficient technology resource to research Cloak" , actionChecker.check(upgradeCloakAction));
    resourceHelpers.get("Player1").getResourcesAttr().put("technology", 250);
    assertEquals(null , actionChecker.check(upgradeCloakAction));
    assertEquals(null , actionChecker.check(cloakingAction));
    CloakingAction cloakingAction2 = new CloakingAction("Mordor","Player1", null);
    assertEquals("Mordor Territory isn't own by you and illegal", actionChecker.check(cloakingAction2));
    resourceHelpers.get("Player1").getResourcesAttr().put("technology", 19);
    assertEquals("Insufficient technology resource" , actionChecker.check(cloakingAction));
  }

}
