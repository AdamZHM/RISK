package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpgradeTechActionTest {

    @Test
    void test_upgrade_tech_action() {
        GameMap gameMap = getMapHelper();
        Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(gameMap, resourceHelpers, getVisionHelpers());
        UpgradeTechAction upgradeTechAction = new UpgradeTechAction("Player1", null);
        assertEquals(null, upgradeTechAction.accept(actionCheckerRehearser));
        ActionExecutor actionExecutor = new ActionExecutor(gameMap, resourceHelpers, getVisionHelpers(), 36);
        UpgradeTechAction upgradeTechAction2 = new UpgradeTechAction("a");
        upgradeTechAction2.updateParam("player1", "technology", "Roshar", 1);
        upgradeTechAction.accept(actionExecutor);
    }

    @Test
    void test_upgrade_tech_action_fail() {
        GameMap gameMap = getMapHelper();
        Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
        resourceHelpers.get("Player1").getResourcesAttr().put("technology", 10);
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(gameMap, resourceHelpers, getVisionHelpers());
        UpgradeTechAction upgradeTechAction = new UpgradeTechAction("Player1", null);
        String res = upgradeTechAction.accept(actionCheckerRehearser);
        assertEquals(true, res != null);
    }

    private Map<String, ResourceHelper> getResourceHelpers() {
        Map<String, ResourceHelper> res = new HashMap<>(){{
            put("Player1", new ResourceHelper(1, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
            put("Player2", new ResourceHelper(1, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
            put("Player3", new ResourceHelper(1, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
        }};
        return res;
    }

    public Map<String, VisionHelper> getVisionHelpers() {
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

}
