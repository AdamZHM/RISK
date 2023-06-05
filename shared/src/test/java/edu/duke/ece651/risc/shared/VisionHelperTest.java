package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisionHelperTest {

    @Test
    public void test_check_spy_at() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        VisionHelper visionHelper = visionHelpers.get("Player1");
        assertEquals(false, visionHelper.checkSpyAt(map.getTerritoryByName("Scadrial")));
        visionHelper.getSpyInfo().put("Scadrial", 1);
        assertEquals(true, visionHelper.checkSpyAt(map.getTerritoryByName("Scadrial")));
    }

    @Test
    public void test_check_territory_cloak() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        VisionHelper visionHelper = visionHelpers.get("Player1");
        assertEquals(false, visionHelper.checkTerritoryCloak(visionHelpers, map.getTerritoryByName("Roshar")));
        visionHelpers.get("Player2").getCloakInfo().put("Roshar", 2);
        assertEquals(true, visionHelper.checkTerritoryCloak(visionHelpers, map.getTerritoryByName("Roshar")));
    }

    @Test
    public void test_get_old_info_from_latest() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        VisionHelper visionHelper = visionHelpers.get("Player1");
        String raw = "test\nthis should be kept\n";
        String after = visionHelper.getOldInfoFromLatest(raw);
        assertEquals("<Old Info>\nthis should be kept\n", after);
    }

    @Test
    public void test_update_cloaking_per_round() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        VisionHelper visionHelper = visionHelpers.get("Player1");
        Map<String, Integer> myCloak = visionHelper.getCloakInfo();
        for (Territory t: map.getMyTerritories().values()) {
            if (t.getOwner().equals("Player1")) {
                myCloak.put(t.getName(), 2);
                assertEquals(2, myCloak.get(t.getName()));
            }
        }
        visionHelper.updateCloakingPerRound();
        for (Territory t: map.getMyTerritories().values()) {
            if (t.getOwner().equals("Player1")) {
                assertEquals(1, myCloak.get(t.getName()));
            }
        }
    }

    @Test
    public void test_update_vision_per_round() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        VisionHelper visionHelper = visionHelpers.get("Player1");
        String expected = "";
        for (String visionInfo: visionHelper.getVisionInfo().values()) {
            expected += visionInfo;
        }
        // System.out.println("-------------------------------------------\n");
        visionHelper.updateVisionPerRound(map, visionHelpers);
        String actual = "";
        for (String visionInfo: visionHelper.getVisionInfo().values()) {
            actual += visionInfo;
        }
        assertEquals(expected, actual);
    }

    @Test
    public void test_getSpyInfo() {
        GameMap map = test_helper();
        Map<String, VisionHelper> visionHelpers = getVisionHelpers();
        VisionHelper visionHelper = visionHelpers.get("Player1");
        assertEquals("Player1", visionHelper.getOwner());
        visionHelper.spyInfo.put("Oz",1);
        assertEquals("<Spy Info>\n"+"Oz: 1\n", visionHelper.getSpyInfoStr());
        visionHelper.spyInfo.clear();
        assertEquals("<Spy Info>\n"+"You currently have no spy\n", visionHelper.getSpyInfoStr());
    }


    private GameMap test_helper() {
        GameMap m = new GameMap(3);
        Map<String, Territory> mt = m.getMyTerritories();
        mt.get("Scadrial").setOwner("Player1");
        mt.get("Scadrial").getUnitByName("soldier").setNum(10);
        mt.get("Roshar").setOwner("Player2");
        mt.get("Roshar").getUnitByName("soldier").setNum(4);
        mt.get("Mordor").setOwner("Player2");
        mt.get("Mordor").getUnitByName("soldier").setNum(14);
        mt.get("Narnia").setOwner("Player1");
        mt.get("Narnia").getUnitByName("soldier").setNum(10);
        mt.get("Oz").setOwner("Player2");
        mt.get("Oz").getUnitByName("soldier").setNum(8);
        mt.get("Midkemia").setOwner("Player2");
        mt.get("Midkemia").getUnitByName("soldier").setNum(12);
        mt.get("Gondor").setOwner("Player3");
        mt.get("Elantris").setOwner("Player3");
        mt.get("Hogwarts").setOwner("Player3");
        return m;
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
