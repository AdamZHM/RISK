package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionCheckerRehearserTest {

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
        mt.get("Narnia").getUnitByName("soldier").setNum(10);
        mt.get("Oz").setOwner("Player2");
        mt.get("Oz").getUnitByName("soldier").setNum(8);
        mt.get("Midkemia").setOwner("Player2");
        mt.get("Midkemia").getUnitByName("soldier").setNum(12);
        return m;
    }

    @Test
    public void test_checker_rehearser_visit_1() {
        GameMap map = test_helper();
        ResourceHelper resourceHelper = new ResourceHelper();
        resourceHelper.updateResoursePerRound(map, "Player1");
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, getResourceHelpers(), getVisionHelpers());
        Map<String, Integer> moveInfo = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction = new MoveAction(moveInfo, "Scadrial", "Roshar", "Player1", null);
        Map<String, Integer> attackInfo = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction = new AttackAction(attackInfo, "Scadrial", "Mordor", "Player1", moveAction);
        actionCheckerRehearser.visit(attackAction);
        GameMap toCheck = actionCheckerRehearser.gameMap;
        GameMapTextView test = new GameMapTextView();
        assertEquals(1, toCheck.getTerritoryByName("Scadrial").getUnitByName("soldier").getNum());
        assertEquals(5, toCheck.getTerritoryByName("Roshar").getUnitByName("soldier").getNum());
    }

    @Test
    public void test_checker_rehearser_visit_2() {
        GameMap map = test_helper();
        ResourceHelper resourceHelper = new ResourceHelper();
        resourceHelper.updateResoursePerRound(map, "Player1");
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, getResourceHelpers(), getVisionHelpers());
        Map<String, Integer> attackInfo = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction = new AttackAction(attackInfo, "Scadrial", "Mordor", "Player1", null);
        Map<String, Integer> moveInfo = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction = new MoveAction(moveInfo, "Scadrial", "Roshar", "Player1", attackAction);
        actionCheckerRehearser.visit(moveAction);
        GameMap toCheck = actionCheckerRehearser.gameMap;
        assertEquals(1, toCheck.getTerritoryByName("Scadrial").getUnitByName("soldier").getNum());
        assertEquals(5, toCheck.getTerritoryByName("Roshar").getUnitByName("soldier").getNum());
    }

    @Test
    public void test_checker_rehearser_visit_3() {
        GameMap map = test_helper();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, getResourceHelpers(), getVisionHelpers());

        UpgradeInfo upgradeInfo2 = new UpgradeInfo("Scadrial", "general", "king", 3);
        UpgradeUnitAction upgradeUnitAction2 = new UpgradeUnitAction("Player1", null, upgradeInfo2);

        UpgradeInfo upgradeInfo1 = new UpgradeInfo("Scadrial", "soldier", "warrior", 5);
        UpgradeUnitAction upgradeUnitAction1 = new UpgradeUnitAction("Player1", upgradeUnitAction2, upgradeInfo1);

        String visitRes = actionCheckerRehearser.visit(upgradeUnitAction1);

        assertEquals(true, visitRes != null);
    }

    @Test
    public void test_checker_rehearser_visit_4() {
        GameMap map = test_helper();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, getResourceHelpers(), getVisionHelpers());

        Map<String, Integer> moveInfo = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction = new MoveAction(moveInfo, "Scadrial", "Roshar", "Player1", null);
        UpgradeTechAction upgradeTechAction1 = new UpgradeTechAction("Player1", moveAction);

        String visitRes = actionCheckerRehearser.visit(upgradeTechAction1);

        assertEquals(true, visitRes == null);
    }

    @Test
    public void test_checker_rehearser_visit_5() {
        GameMap map = test_helper();
        Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
        resourceHelpers.get("Player1").getResourcesAttr().put("technology", 100);
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, resourceHelpers, getVisionHelpers());

        UpgradeTechAction upgradeTechAction = new UpgradeTechAction("Player1", null);

        String visitRes = actionCheckerRehearser.visit(upgradeTechAction);

        assertEquals(true, visitRes != null);
    }

    @Test
    public void test_bad_attack_actions() {
        GameMap map = test_helper();
        ResourceHelper resourceHelper = new ResourceHelper();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, getResourceHelpers(), getVisionHelpers());
        Map<String, Integer> attackInfo = new HashMap<>() {
            {
                put("soldier", 1);
            }
        };
        AttackAction attackAction = new AttackAction(attackInfo, "Scadrial", "Oz", "Player2", null);
        String result = actionCheckerRehearser.visit(attackAction);
        assertEquals("Scadrial Territory isn't own by you and illegal", result);
    }

    @Test
    public void test_bad_move_actions() {
        GameMap map = test_helper();
        ResourceHelper resourceHelper = new ResourceHelper();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, getResourceHelpers(), getVisionHelpers());
        Map<String, Integer> moveInfo = new HashMap<>() {
            {
                put("soldier", 1);
            }
        };
        MoveAction moveAction = new MoveAction(moveInfo, "Scadrial", "Oz", "Player2", null);
        String result = moveAction.accept(actionCheckerRehearser);
        assertEquals("Scadrial Territory isn't own by you and illegal", result);

    }

    @Test
    public void test_upgrade_spy_action() {
        GameMap map = test_helper();
        Map<String, ResourceHelper> resourceHelpers = getMoreResourceHelpers();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, resourceHelpers, visionHelpers);
        UpgradeSpyAction action3 = new UpgradeSpyAction("Scadrial", "soldier", 1, "Player1", null);
        MoveSpyAction action2 = new MoveSpyAction("Scadrial", "Roshar", 3, "Player1", action3);
        UpgradeSpyAction action1 = new UpgradeSpyAction("Scadrial", "soldier", 3, "Player1", action2);
        actionCheckerRehearser.visit(action1);
        assertEquals(94, resourceHelpers.get("Player1").getResourcesAttr().get("food"));
        assertEquals(420, resourceHelpers.get("Player1").getResourcesAttr().get("technology"));
        assertEquals(3, visionHelpers.get("Player1").getSpyInfo().get("Roshar"));
        assertEquals(1, visionHelpers.get("Player1").getSpyInfo().get("Scadrial"));
    }

    @Test
    public void test_upgrade_move_spy_action() {
        GameMap map = test_helper();
        Map<String, ResourceHelper> resourceHelpers = getMoreResourceHelpers();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, resourceHelpers, visionHelpers);
        MoveSpyAction action2 = new MoveSpyAction("Scadrial", "Roshar", 3, "Player1", null);
        UpgradeSpyAction action1 = new UpgradeSpyAction("Scadrial", "soldier", 3, "Player1", action2);
        actionCheckerRehearser.visit(action1);
        assertEquals(3, visionHelpers.get("Player1").getSpyInfo().get("Roshar"));
    }

    @Test
    public void test_upgrade_cloak_action() {
        GameMap map = test_helper();
        Map<String, ResourceHelper> resourceHelpers = getMoreResourceHelpers();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, resourceHelpers, visionHelpers);
        UpgradeCloakAction action3 = new UpgradeCloakAction("Player1", null);
        CloakingAction action2 = new CloakingAction("Scadrial", "Player1", action3);
        UpgradeCloakAction action1 = new UpgradeCloakAction("Player1", action2);
        actionCheckerRehearser.visit(action1);
        assertEquals(80, resourceHelpers.get("Player1").getResourcesAttr().get("technology"));
        assertEquals(3, visionHelpers.get("Player1").getCloakInfo().get("Scadrial"));
        visionHelpers.get("Player1").updateCloakingPerRound();
        assertEquals(2, visionHelpers.get("Player1").getCloakInfo().get("Scadrial"));
    }

    @Test
    public void test_cloaking_action() {
        GameMap map = test_helper();
        Map<String, ResourceHelper> resourceHelpers = getMoreResourceHelpers();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(map, resourceHelpers, visionHelpers);
        CloakingAction action2 = new CloakingAction("Scadrial", "Player1", null);
        UpgradeCloakAction action1 = new UpgradeCloakAction("Player1", action2);
        actionCheckerRehearser.visit(action1);
        assertEquals(3, visionHelpers.get("Player1").getCloakInfo().get("Scadrial"));
        visionHelpers.get("Player1").updateCloakingPerRound();
        assertEquals(2, visionHelpers.get("Player1").getCloakInfo().get("Scadrial"));
    }

    private Map<String, ResourceHelper> getResourceHelpers() {
        Map<String, ResourceHelper> res = new HashMap<>(){{
            put("Player1", new ResourceHelper(3, new HashMap<>(){{put("food", 100); put("technology", 150);}}));
            put("Player2", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
            put("Player3", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
        }};
        return res;
    }

    private Map<String, ResourceHelper> getMoreResourceHelpers() {
        Map<String, ResourceHelper> res = new HashMap<>(){{
            put("Player1", new ResourceHelper(3, new HashMap<>(){{put("food", 100); put("technology", 500);}}));
            put("Player2", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
            put("Player3", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
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

}
