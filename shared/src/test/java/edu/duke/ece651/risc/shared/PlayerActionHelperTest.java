package edu.duke.ece651.risc.shared;

import org.checkerframework.checker.nullness.qual.PolyNull;
import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.nullable;

public class PlayerActionHelperTest {
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
        mt.get("Gondor").setOwner("Player3");
        mt.get("Elantris").setOwner("Player3");
        mt.get("Hogwarts").setOwner("Player3");

        return m;
    }

    @Test
    public void test_doCheckRehheaser() {
        GameMap map = test_helper();
        Map<String, ResourceHelper> resourceHelpers=getResourceHelpers();
        PlayerActionHelper p = new PlayerActionHelper(map, resourceHelpers, getVisionHelpers());
        MoveAction ac5 = new MoveAction(3, "Ros", "Scadrial", "Player1", "soldier", null);
        assertEquals("Invalid territory Ros", p.doCheckRehearser(ac5));
        // Action:ac1
        MoveAction ac1 = new MoveAction(3, "Roshar", "Scadrial", "Player1", "soldier", null);
        assertEquals(null, p.doCheckRehearser(ac1));
        assertEquals(p.getTailAction(), ac1);

        // Action: ac1-ac2
        AttackAction ac2 = new AttackAction(5, "Scadrial", "Mordor", "Player1", "soldier", null);
        assertEquals(null, p.doCheckRehearser(ac2));
        assertEquals(p.getTailAction(), ac2);
        // Action: ac1-ac7-ac2
        MoveAction ac7 = new MoveAction(1, "Mordor", "Midkemia", "Player2", "soldier", null);
        assertEquals(null, p.doCheckRehearser(ac7));
        assertEquals(p.getTailAction(), ac2);
        assertEquals(p.getHeadAction().nextAction, ac7);
        MoveAction ac8 = new MoveAction(14, "Mordor", "Midkemia", "Player2", "soldier", null);
        assertEquals("Invalid Unit num input soldier 14", p.doCheckRehearser(ac8));
        assertEquals(p.getTailAction(), ac2);
        assertEquals(p.getHeadAction().nextAction, ac7);
        MoveAction ac9 = new MoveAction(3, "Scadrial", "Roshar", "Player1", "soldier", null);
        assertEquals(null, p.doCheckRehearser(ac9));
        assertEquals(p.getTailAction(), ac2);
        assertEquals(p.getHeadAction().nextAction.nextAction, ac9);

        // Action: ac1-ac7-ac2-ac21
        UpgradeTechAction ac21=new UpgradeTechAction("Player1", null);
        System.out.println(resourceHelpers.get("Player1").getResourcesAttr().get("technology"));
        assertEquals(null, p.doCheckRehearser(ac21));
        System.out.println(resourceHelpers.get("Player1").getResourcesAttr().get("technology"));
        
        System.out.println(p.dummyTailAction);
        System.out.println(p.tailAction);
        // assertEquals(p.getTailAction(), ac21);

        // Action: ac1-ac7-ac2-ac21
        resourceHelpers.get("Player1").updateResoursePerRound(map, "Player1");

        UpgradeTechAction ac22=new UpgradeTechAction("Player1", null);
        System.out.println(p.dummyTailAction);
        assertEquals("You could only upgrade technology level once in one round!", p.doCheckRehearser(ac22));
        assertEquals(p.getTailAction(), ac21);
    }

    @Test
    public void test_doCheckRehheaser2() {
        GameMap map = test_helper();
        Map<String, ResourceHelper> resourceHelpers=getResourceHelpers();
        PlayerActionHelper p = new PlayerActionHelper(map, resourceHelpers, getVisionHelpers());
        p.updatePlayerVisionPerRound();
        p.visionHelpers.get("Player1").updateVisionPerAction(map);
        MoveSpyAction ac1=new MoveSpyAction("Player1");
        ac1.updateParam("Mordor", "Oz", null ,0);
        assertEquals("Your Spy not exist in this territory", p.doCheckRehearser(ac1));
        UpgradeCloakAction ac2 = new UpgradeCloakAction("Player1");
        ac2.updateParam("", null, null , 0);
        assertEquals("Insufficient Tech Level to research Cloak", p.doCheckRehearser(ac2));
        assertEquals(p.getTailAction(), null);

        UpgradeSpyAction ac3 = new UpgradeSpyAction("Player1");
        ac3.updateParam("Mordor", null, null , 0);
        assertEquals("Mordor Territory isn't own by you and illegal", p.doCheckRehearser(ac3));
        assertEquals(p.getTailAction(), null);

        CloakingAction ac4 = new CloakingAction("Player1");
        ac4.updateParam("", null, null , 0);
        assertEquals("Need research cloaking first", p.doCheckRehearser(ac4));
        assertEquals(p.getTailAction(), null);

    }

    @Test
    public void test_reset_action() {
        GameMap map = test_helper();
        ResourceHelper resourceHelper = new ResourceHelper();
        resourceHelper.updateResoursePerRound(map, "Player1");
        MoveAction ac5 = new MoveAction(3, "Roshar", "Scadrial", "Player1", "soldier", null);
        PlayerActionHelper p1 = new PlayerActionHelper(map);
        Action action = new UpgradeTechAction("Player1", null);
        p1.headAction = null;
        p1.tailAction = null;
        p1.setTailAction(action);
        p1.getHeadAction();
        
        PlayerActionHelper p = new PlayerActionHelper(map, getResourceHelpers(), getVisionHelpers());
        p.setupHelper("Player1", resourceHelper,getVisionHelpers().get("Player1"));
        p.doCheckRehearser(ac5);
        assertEquals(true, p.getHeadAction() != null);
        p.resetAction();
        assertEquals(true, p.getHeadAction() == null);
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
            put("Player1", new VisionHelper(test_helper(), "Player1"));
            put("Player2", new VisionHelper(test_helper(), "Player2"));
            put("Player3", new VisionHelper(test_helper(), "Player3"));
        }};
        return res;
    }
}
