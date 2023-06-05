package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResourceHelper implements Serializable {

    public boolean cloakFlag=false;

    /**
     * This is the player's technology level
     */
    private int techLevel;

    /**
     *
     */
    private Map<String, Integer> resources;
    /**
     * this is the cost map of technology upgrade
     * key is the level, 2,3,4,5,6
     * value is the cost of upgrade
     */
    public static final Map<Integer, Integer> technologyLevelCostMap = new HashMap<Integer, Integer>() {
        {
            put(2, 50); // 1->2 takes 50
            put(3, 75); // 2->3 takes 75
            put(4, 125); // 3->4 takes 125
            put(5, 200); // 4->5 takes 200
            put(6, 300); // 5->6 takes 300
        }
    };
    public static final Map<Integer, Integer> technologyUnitCostMap = new LinkedHashMap<Integer, Integer>() {
        {
            put(1, 3);
            put(2, 8);
            put(3, 19);
            put(4, 25);
            put(5, 35);
            put(6, 50);
        }
    };

    /**
     *
     */
    public ResourceHelper() {
        this.resources = new HashMap<String, Integer>() {
            {
                put("food", 0);
                put("technology", 0);
            }
        };
        this.techLevel = 1;
    }

    /**
     *
     * @param techLevel
     * @param resources
     */
    public ResourceHelper(int techLevel, Map<String, Integer> resources) {
        this.techLevel = techLevel;
        this.resources = new HashMap<>();
        for (String key : resources.keySet())
            this.resources.put(key, resources.get(key));
    }

    /**
     *
     * @return
     */
    public Map<String, Integer> getResourcesAttr() {
        return resources;
    }

    /**
     * Get the player's technology level
     *
     * @return the player's technology level
     */
    public int getTechLevel() {
        return this.techLevel;
    }

    /**
     * This function is to update Resourse per player in one round
     * 
     * @param map,   the Gamemap
     * @param owner, the player to update
     */
    public void updateResoursePerRound(GameMap map, String owner) {
        Map<String, Territory> territories = map.getMyTerritories();
        for (Territory t : territories.values()) {
            if (t.getOwner().equals(owner)) {
                for (String resource : resources.keySet()) {
                    this.resources.put(resource, t.getMyResources().get(resource) + resources.get(resource));
                }
            }
        }
    }

    private int[][] setUpDistMap(GameMap map, String owner) {
        Map<String,Map<String,Integer>> territoryMap=map.getCompleteTerritoryInfo();
        ArrayList<String> territoryList=new ArrayList<>(territoryMap.keySet());
        int tNum=territoryMap.size();
        int[][] distMap=new int[tNum][tNum];
        for (int i=0;i<tNum;i++){
          for (int j=0;j<tNum;j++){
            Territory src=map.getTerritoryByName(territoryList.get(i));
            Territory dst=map.getTerritoryByName(territoryList.get(j));
            if (territoryMap.get(territoryList.get(i)).containsKey(territoryList.get(j)) && src.getOwner().equals(owner) && dst.getOwner().equals(owner)){
              distMap[i][j]=territoryMap.get(territoryList.get(i)).get(territoryList.get(j));
            }
            else{
              distMap[i][j]=Integer.MAX_VALUE>>1;
            }
          }
          distMap[i][i]=0;
        }
        for (int k=0;k<tNum;k++){
          for (int i=0;i<tNum;i++){
            for (int j=0;j<tNum;j++){
              distMap[i][j]=Math.min(distMap[i][j],distMap[i][k]+distMap[k][j]);
            }
          }
        }
        return distMap;
      }

    /**
     * This function is used to calculate the resource consume for one move action
     * per unit
     * 
     * @param map
     * @param action
     */
    public int calFoodConsume(GameMap map, MoveAttackAction action) {
        if (action instanceof AttackAction) {
            return action.getTotalUnitNum();
        } else {
            String src = action.getSource();
            String dst = action.getDestination();
            String owner=action.getName();
            Map<String, Map<String, Integer>> territoryMap = map.getCompleteTerritoryInfo();
            ArrayList<String> territoryList = new ArrayList<>(territoryMap.keySet());

            int src_index = territoryList.indexOf(src);
            int dst_index = territoryList.indexOf(dst);
            int [][] distMap=setUpDistMap(map, owner);

            return distMap[src_index][dst_index] * action.getTotalUnitNum();
        }
    }

    /**
     * This function is used to calculate total technology resource consume for one
     * upgrade unit
     * action
     * 
     * @param src_level: int, source level of unit
     * @param dst_level: int target level of unit
     * @param num:       number of the unit
     */
    public int calUnitUpgradeConsume(int src_level, int dst_level, int num) {
        return num * technologyUnitCostMap.keySet().stream().filter(a -> (a > src_level && a <= dst_level))
                .map(item -> technologyUnitCostMap.get(item)).reduce(0, Integer::sum);
    }

    public int calLevelUpgradeConsume() {
        return technologyLevelCostMap.get(techLevel + 1);
    }

    public int calSpyUpgradeConsume(UpgradeSpyAction action) {
        return 20*action.getNum();
    }

    public int calSpyMoveConsume(MoveSpyAction action) {
        return 2*action.getNum();
    }

    public int calClockConsume() {
        return 20;
    }

    public int calCloakUpgradeConsume() {return 200;}

    public void updateTechnologyLevel() {
        this.techLevel += 1;
    }
}