package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionExecutorTest {

    private GameMap test_helper() {
        GameMap m=new GameMap(3);
        Map<String,Territory> mt=m.getMyTerritories();
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

    private GameMap test_helper_2() {
        GameMap m=new GameMap(3);
        Map<String,Territory> mt=m.getMyTerritories();
        mt.get("Scadrial").setOwner("Player1");
        mt.get("Scadrial").getUnitByName("soldier").setNum(5);
        mt.get("Roshar").setOwner("Player1");
        mt.get("Roshar").getUnitByName("soldier").setNum(3);
        mt.get("Mordor").setOwner("Player2");
        mt.get("Mordor").getUnitByName("soldier").setNum(2);
        mt.get("Narnia").setOwner("Player1");
        mt.get("Narnia").getUnitByName("soldier").setNum(10);
        mt.get("Oz").setOwner("Player2");
        mt.get("Oz").getUnitByName("soldier").setNum(8);
        mt.get("Midkemia").setOwner("Player2");
        mt.get("Midkemia").getUnitByName("soldier").setNum(12);
        return m;
    }

    @Test
    public void test_executor_visit_1() {
        GameMap map = test_helper();
        ActionExecutor actionExecutor = new ActionExecutor(map, getResourceHelpers(), getVisionHelpers(), 36);
        Map<String, Integer> moveInfo1 = new HashMap<>(){{
            put("soldier", 2);
        }};
        MoveAction moveAction1 = new MoveAction(moveInfo1, "Scadrial", "Roshar", "Player1", null);
        Map<String, Integer> moveInfo2 = new HashMap<>(){{
            put("soldier", 2);
        }};
        MoveAction moveAction2 = new MoveAction(moveInfo2, "Scadrial", "Roshar", "Player1", moveAction1);
        Map<String, Integer> attackInfo1 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction1 = new AttackAction(attackInfo1, "Scadrial", "Mordor", "Player1", moveAction2);
        Map<String, Integer> attackInfo2 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction2 = new AttackAction(attackInfo2, "Scadrial", "Mordor", "Player1", attackAction1);
        Map<String, Integer> attackInfo3 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction3 = new AttackAction(attackInfo3, "Scadrial", "Mordor", "Player1", attackAction2);
        Map<String, Integer> attackInfo4 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction4 = new AttackAction(attackInfo4, "Scadrial", "Mordor", "Player1", attackAction3);
        Map<String, Integer> attackInfo5 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction5 = new AttackAction(attackInfo5, "Scadrial", "Mordor", "Player1", attackAction4);
        Map<String, Integer> attackInfo6 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction6 = new AttackAction(attackInfo6, "Scadrial", "Mordor", "Player1", attackAction5);

        actionExecutor.visit(attackAction6);
        GameMap toCheck = actionExecutor.gameMap;
        GameMapTextView view = new GameMapTextView();
        System.out.println(view.displayMyTerritory(toCheck,"Player1"));
        System.out.println(view.displayMyTerritory(toCheck,"Player2"));
    }

    @Test
    public void test_executor_visit_2() {
        GameMap map = test_helper();
        ActionExecutor actionExecutor = new ActionExecutor(map, getResourceHelpers(), getVisionHelpers(), 36);
        Map<String, Integer> attackInfo1 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction1 = new AttackAction(attackInfo1, "Scadrial", "Mordor", "Player1", null);
        Map<String, Integer> moveInfo1 = new HashMap<>(){{
            put("soldier", 2);
        }};
        MoveAction moveAction1 = new MoveAction(moveInfo1, "Scadrial", "Roshar", "Player1", attackAction1);
        Map<String, Integer> moveInfo2 = new HashMap<>(){{
            put("soldier", 2);
        }};
        MoveAction moveAction2 = new MoveAction(moveInfo2, "Scadrial", "Roshar", "Player1", moveAction1);
        actionExecutor.visit(moveAction2);
        GameMap toCheck = actionExecutor.gameMap;
        GameMapTextView view = new GameMapTextView();
        System.out.println(view.displayMyTerritory(toCheck,"Player1"));
        System.out.println(view.displayMyTerritory(toCheck,"Player2"));
    }

    @Test
    public void test_executor_visit_3() {
        GameMap map = test_helper_2();
        ActionExecutor actionExecutor = new ActionExecutor(map, getResourceHelpers(), getVisionHelpers_2(), 36);
        Map<String, Integer> attackInfo1 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction1 = new AttackAction(attackInfo1, "Scadrial", "Mordor", "Player1", null);
        Map<String, Integer> attackInfo2 = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction attackAction2 = new AttackAction(attackInfo2, "Scadrial", "Mordor", "Player1", attackAction1);
        actionExecutor.visit(attackAction2);
        GameMap toCheck = actionExecutor.gameMap;
        GameMapTextView view = new GameMapTextView();
        System.out.println(view.displayMyTerritory(toCheck,"Player1"));
        System.out.println(view.displayMyTerritory(toCheck,"Player2"));
    }

    @Test
    public void test_executor_visit_5() {
        GameMap map = test_helper_2();

        ActionExecutor actionExecutor = new ActionExecutor(map, getResourceHelpers(), getVisionHelpers_2(), 36);

        UpgradeInfo upgradeInfo2 = new UpgradeInfo("Scadrial", "general", "king", 3);
        UpgradeUnitAction upgradeUnitAction2 = new UpgradeUnitAction("Player1", null, upgradeInfo2);

        UpgradeInfo upgradeInfo1 = new UpgradeInfo("Scadrial", "soldier", "warrior", 5);
        UpgradeUnitAction upgradeUnitAction1 = new UpgradeUnitAction("Player1", upgradeUnitAction2, upgradeInfo1);

        String visitRes = actionExecutor.visit(upgradeUnitAction1);
        assertEquals(true, visitRes == null);
    }

    @Test
    public void test_checker_rehearser_visit_4() {
        GameMap map = test_helper();
        ActionExecutor actionExecutor = new ActionExecutor(map, getResourceHelpers(), getVisionHelpers(), 36);

        UpgradeInfo upgradeInfo1 = new UpgradeInfo("Scadrial", "soldier", "warrior", 5);
        UpgradeUnitAction upgradeUnitAction1 = new UpgradeUnitAction("Player1", null, upgradeInfo1);
        UpgradeTechAction upgradeTechAction = new UpgradeTechAction("Player1", upgradeUnitAction1);

        actionExecutor.visit(upgradeTechAction);

        assertEquals(6, actionExecutor.resourceHelpers.get("Player1").getTechLevel());
    }

    private Map<String, ResourceHelper> getResourceHelpers() {
        Map<String, ResourceHelper> res = new HashMap<>(){{
            put("Player1", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
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

    public Map<String, VisionHelper> getVisionHelpers_2() {
        Map<String, VisionHelper> res = new HashMap<>() {{
            put("Player1", new VisionHelper(test_helper_2(), "Player1"));
            put("Player2", new VisionHelper(test_helper_2(), "Player2"));
            put("Player3", new VisionHelper(test_helper_2(), "Player3"));
        }};
        return res;
    }

    public Map<String, VisionHelper> getVisionHelpersForAttack() {
        Map<String, VisionHelper> res = new HashMap<>() {{
            put("Player1", new VisionHelper(getMapForAttackTest(), "Player1"));
            put("Player2", new VisionHelper(getMapForAttackTest(), "Player2"));
            put("Player3", new VisionHelper(getMapForAttackTest(), "Player3"));
        }};
        return res;
    }

    private GameMap getMapForAttackTest() {
        GameMap map = new GameMap(3);
        Map<String,Territory> myTerritories = map.getMyTerritories();

        myTerritories.get("Scadrial").setOwner("Player1");
        Territory scadrial = myTerritories.get("Scadrial");
        scadrial.getUnitByName("warrior").setNum(3);
        scadrial.getUnitByName("knight").setNum(5);

        myTerritories.get("Mordor").setOwner("Player2");
        myTerritories.get("Mordor").getUnitByName("warrior").setNum(4);
        myTerritories.get("Mordor").getUnitByName("knight").setNum(4);

        return map;
    }

    @Test
    public void test_attack_execution() {
        GameMap map = getMapForAttackTest();
        ActionExecutor actionExecutor = new ActionExecutor(map, getResourceHelpers(), getVisionHelpersForAttack(), 36);

        Map<String, Integer> attackInfo = new HashMap<>(){{
            put("warrior", 3);
            put("knight", 5);
        }};
        AttackAction attackAction = new AttackAction(attackInfo, "Scadrial", "Mordor", "Player1", null);
        actionExecutor.visit(attackAction);
    }

    @Test
    public void test_attack_execution2() {
        GameMap map = getMapForAttackTest();
        Map<String,Territory> myTerritories = map.getMyTerritories();
        Territory scadrial = myTerritories.get("Scadrial");
        scadrial.getUnitByName("warrior").setNum(1);
        scadrial.getUnitByName("knight").setNum(1);

        ActionExecutor actionExecutor = new ActionExecutor(map, getResourceHelpers(), getVisionHelpersForAttack(), 36);

        Map<String, Integer> attackInfo = new HashMap<>(){{
            put("warrior", 4);
            put("knight", 4);
        }};
        AttackAction attackAction = new AttackAction(attackInfo, "Mordor", "Scadrial", "Player2", null);
        actionExecutor.visit(attackAction);
    }

}
