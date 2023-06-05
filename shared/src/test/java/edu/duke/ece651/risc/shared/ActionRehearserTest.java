package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionRehearserTest {

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

    @Test
    public void test_move_rehearse() {
        GameMap map = test_helper();
        ActionRehearser actionRehearser = new ActionRehearser(map, getResourceHelpers(), getVisionHelpers());
        Map<String, Integer> moveInfo = new HashMap<>(){{
            put("soldier", 2);
        }};
        MoveAction action = new MoveAction(moveInfo, "Scadrial", "Roshar", "Player1", null);
        actionRehearser.rehearse(action);
        GameMap toCheck = actionRehearser.gameMap;
        //assertEquals(map.getTerritoryByName("Scadrial").getUnitByName("soldier").getNum(), 5);
        //assertEquals(map.getTerritoryByName("Roshar").getUnitByName("soldier").getNum(), 3);
        assertEquals(toCheck.getTerritoryByName("Scadrial").getUnitByName("soldier").getNum(), 3);
        assertEquals(toCheck.getTerritoryByName("Roshar").getUnitByName("soldier").getNum(), 5);
    }

    @Test
    public void test_attack_rehearse() {
        GameMap map = test_helper();
        ActionRehearser actionRehearser = new ActionRehearser(map, getResourceHelpers(), getVisionHelpers());
        Map<String, Integer> attackInfo = new HashMap<>(){{
            put("soldier", 2);
        }};
        AttackAction action = new AttackAction(attackInfo, "Scadrial", "Mordor", "Player1", null);
        actionRehearser.rehearse(action);
        GameMap toCheck = actionRehearser.gameMap;
        //assertEquals(map.getTerritoryByName("Scadrial").getUnitByName("soldier").getNum(), 5);
        //assertEquals(map.getTerritoryByName("Roshar").getUnitByName("soldier").getNum(), 3);
        assertEquals(toCheck.getTerritoryByName("Scadrial").getUnitByName("soldier").getNum(), 3);
        assertEquals(toCheck.getTerritoryByName("Roshar").getUnitByName("soldier").getNum(), 3);
    }

    @Test
    public void test_upgrade_spy_rehearse() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        ActionRehearser actionRehearser = new ActionRehearser(map, getResourceHelpers(), visionHelpers);
        UpgradeSpyAction action = new UpgradeSpyAction("Scadrial", "soldier", 2, "Player1", null);
        actionRehearser.rehearse(action);
        assertEquals(2, visionHelpers.get("Player1").getSpyInfo().get("Scadrial"));
    }

    @Test
    public void test_cloaking_rehearse() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        ActionRehearser actionRehearser = new ActionRehearser(map, getResourceHelpers(), visionHelpers);
        CloakingAction action = new CloakingAction("Scadrial", "Player1", null);
        actionRehearser.rehearse(action);
        assertEquals(3, visionHelpers.get("Player1").getCloakInfo().get("Scadrial"));
    }

    @Test
    public void test_move_spy_rehearse() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        ActionRehearser actionRehearser = new ActionRehearser(map, getResourceHelpers(), visionHelpers);
        UpgradeSpyAction upgradeAction = new UpgradeSpyAction("Scadrial", "soldier", 2, "Player1", null);
        MoveSpyAction moveAction = new MoveSpyAction("Scadrial", "Roshar", 1, "Player1", null);
        actionRehearser.rehearse(upgradeAction);
        actionRehearser.rehearse(moveAction);
        assertEquals(1, visionHelpers.get("Player1").getSpyInfo().get("Roshar"));
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

}
