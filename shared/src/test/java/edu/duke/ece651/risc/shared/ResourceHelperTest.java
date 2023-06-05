package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ResourceHelperTest {
    @Test
    public void test_updateResouces(){
        GameMap map=new GameMap(3);
        Map<String,Territory> mt=map.getMyTerritories();
        mt.get("Scadrial").setOwner("red");
        mt.get("Roshar").setOwner("red");
        mt.get("Oz").setOwner("red");
        ResourceHelper redResources=new ResourceHelper();
        redResources.updateResoursePerRound(map, "red");
        Map<String,Integer> redRmap=redResources.getResourcesAttr();
        System.out.println(redRmap.get("food"));
        System.out.println(redRmap.get("technology"));
        for (String resource:redRmap.keySet()){
            if (resource.equals("food")){
                assertEquals(15, redRmap.get(resource));
            }
            if (resource.equals("technology")){
                assertEquals(150, redRmap.get(resource));
            }
        }
        redResources.updateResoursePerRound(map, "red");
        redRmap=redResources.getResourcesAttr();
        for (String resource:redRmap.keySet()){
            if (resource.equals("food")){
                assertEquals(30, redRmap.get(resource));
            }
            if (resource.equals("technology")){
                assertEquals(300, redRmap.get(resource));
            }
        }
        redResources.updateResoursePerRound(map, "green");
        redRmap=redResources.getResourcesAttr();
        for (String resource:redRmap.keySet()){
            if (resource.equals("food")){
                assertEquals(30, redRmap.get(resource));
            }
            if (resource.equals("technology")){
                assertEquals(300, redRmap.get(resource));
            }
        }
    }


    @Test
    public void test_calUnitConsume(){
        Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
       int cost=resourceHelpers.get("Player1").calUnitUpgradeConsume(0, 1, 5);
        assertEquals(15, cost);
        int cost2=resourceHelpers.get("Player1").calUnitUpgradeConsume(1, 6, 2);
        assertEquals(274, cost2);
    }

    @Test
    public void test_updateTechLevel(){
        Map<String, ResourceHelper> resourceHelpers = getResourceHelpers();
        ResourceHelper player3=resourceHelpers.get("Player3");
        assertEquals(player3.getTechLevel(), 5);
        player3.updateTechnologyLevel();
        assertEquals(player3.getTechLevel(), 6);
        assertEquals(player3.getTechLevel(), 6);
    }

    private Map<String, ResourceHelper> getResourceHelpers() {
        Map<String, ResourceHelper> res = new HashMap<>(){{
          put("Player1", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
          put("Player2", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
          put("Player3", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 300);}}));
        }};
        return res;
      }


    
}
