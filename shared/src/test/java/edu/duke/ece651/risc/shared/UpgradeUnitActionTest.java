package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpgradeUnitActionTest {

    @Test
    public void test_upgrade_action() {
        GameMap gameMap = getMapHelper();
        Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
        ActionCheckerRehearser actionCheckerRehearser = new ActionCheckerRehearser(gameMap, resourceHelpers, getVisionHelpers());
        UpgradeInfo upgradeInfo1 = new UpgradeInfo("Scadrial", "soldier", "warrior", 3);
        UpgradeInfo upgradeInfo2 = new UpgradeInfo("Scadrial", "warrior", "king", 3);
        UpgradeUnitAction upgradeUnitAction = new UpgradeUnitAction("p");
        upgradeUnitAction.updateParam("a", "b", "c", 1);

        assertEquals(1000, resourceHelpers.get("Player1").getResourcesAttr().get("technology"));
        assertEquals(5, gameMap.getTerritoryByName("Scadrial").getMyUnits().get("soldier").getNum());
        assertEquals(0, gameMap.getTerritoryByName("Scadrial").getMyUnits().get("warrior").getNum());

        UpgradeUnitAction upgradeUnitAction1 = new UpgradeUnitAction("Player1", null, upgradeInfo1);
        String error=upgradeUnitAction1.accept(actionCheckerRehearser);
        System.out.println(error);
        assertEquals(991, resourceHelpers.get("Player1").getResourcesAttr().get("technology"));
        assertEquals(2, gameMap.getTerritoryByName("Scadrial").getMyUnits().get("soldier").getNum());
        assertEquals(3, gameMap.getTerritoryByName("Scadrial").getMyUnits().get("warrior").getNum());

        UpgradeUnitAction upgradeUnitAction2 = new UpgradeUnitAction("Player1", null, upgradeInfo2);
        upgradeUnitAction2.accept(actionCheckerRehearser);

        assertEquals(580, resourceHelpers.get("Player1").getResourcesAttr().get("technology"));
        assertEquals(2, gameMap.getTerritoryByName("Scadrial").getMyUnits().get("soldier").getNum());
        assertEquals(0, gameMap.getTerritoryByName("Scadrial").getMyUnits().get("warrior").getNum());
        assertEquals(3, gameMap.getTerritoryByName("Scadrial").getMyUnits().get("king").getNum());
    }

    private Map<String, ResourceHelper> getResourceHelpers() {
        Map<String, ResourceHelper> res = new HashMap<>(){{
            put("Player1", new ResourceHelper(6, new HashMap<>(){{put("food", 100); put("technology", 1000);}}));
            put("Player2", new ResourceHelper(6, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
            put("Player3", new ResourceHelper(6, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
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
