package edu.duke.ece651.risc.shared;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.common.util.concurrent.MoreExecutors;

/**
 * This is the Action Checker class
 */
public class ActionChecker {

    /**
     * This is the game map that the checker relies on
     */
    private GameMap gameMap;

    /**
     * This is a list of all players' attributions
     */
    public Map<String, ResourceHelper> resourceHelpers;

    /**
     *
     */
    public Map<String, VisionHelper> visionHelpers;
    
    private Map<String, Integer> spyMoveMap;

    /**
     * This is the constructor of the class
     *
     * @param map is the game map instance that the checker should rely on
     */
    public ActionChecker(GameMap map, Map<String, ResourceHelper> resourceHelpers,
            Map<String, VisionHelper> visionHelpers) {
        this.gameMap = map;
        this.resourceHelpers = resourceHelpers;
        this.visionHelpers = visionHelpers;
        this.spyMoveMap = new HashMap<>();
    }

    public void resetSpyMoveMap(){
        this.spyMoveMap = new HashMap<>();
    }

    /**
     * check all the rules for move action
     *
     * @param moveAction is the to check MoveAction
     * @return error message or null if legal
     */
    public String check(MoveAction moveAction) throws IllegalArgumentException {
        String owner = moveAction.getName();
        String src = moveAction.getSource();
        String dst = moveAction.getDestination();
        try {
            Territory src_t = check_territory_exist(src);
            Territory dst_t = check_territory_exist(dst);
            check_own(src_t, owner, true);
            check_own(dst_t, owner, true);
            check_unit(src_t, moveAction.getUnitInfo());
            check_linked(src, dst, owner);
            check_foodResources(moveAction);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * check all the rules for move action
     *
     * @param moveSpyAction is the to check MoveAction
     * @return error message or null if legal
     */
    public String check(MoveSpyAction moveSpyAction) throws IllegalArgumentException {
        String owner = moveSpyAction.getName();
        String src = moveSpyAction.getSrcTerritory();
        String dst = moveSpyAction.getDestTerritory();
        int num = moveSpyAction.getNum();
        try {
            Territory src_t = check_territory_exist(src);
            Territory dst_t = check_territory_exist(dst);
            if (!visionHelpers.get(owner).checkSpyAt(src_t)) {
                throw new IllegalArgumentException("Your Spy not exist in this territory");
            }
            int moveSpy=0;
            if (spyMoveMap.containsKey(src)){
                moveSpy=spyMoveMap.get(src);
            }
            //System.out.println(visionHelpers.get(owner).getSpyInfo().get(src).toString());
            if (visionHelpers.get(owner).getSpyInfo().get(src) - moveSpy<num) {
                throw new IllegalArgumentException("Your have no sufficient Spy to move in this territory");
            }
            check_adjacent(src, dst);
            if (resourceHelpers.get(owner).getResourcesAttr().get("food") < resourceHelpers.get(owner).calSpyMoveConsume(moveSpyAction)) {
                throw new IllegalArgumentException("Insufficient food resource");
            }
            spyMoveMap.put(dst, moveSpy+ num);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        return null;
    }

    /**
     * check all the rules for attack action
     *
     * @param attackAction is the to check AttackAction
     * @return error message or null if legal
     */
    public String check(AttackAction attackAction) {
        String owner = attackAction.getName();
        String src = attackAction.getSource();
        String dst = attackAction.getDestination();
        try {
            Territory src_t = check_territory_exist(src);
            Territory dst_t = check_territory_exist(dst);
            check_own(src_t, owner, true);
            check_own(dst_t, owner, false);
            check_unit(src_t, attackAction.getUnitInfo());
            check_adjacent(src, dst);
            check_foodResources(attackAction);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    };

    /**
     * check all the rules for upgrade action
     *
     * @param upgradeUnitAction is the to check UpgradeAction
     * @return error message or null if legal
     */
    public String check(UpgradeUnitAction upgradeUnitAction) {
        String tName = upgradeUnitAction.getUpgradeInfo().getTerritory();
        String owner = upgradeUnitAction.getName();
        try {
            Territory territory = check_territory_exist(tName);
            check_own(territory, owner, true);
            check_upgrade_unit(upgradeUnitAction, territory, owner);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * check all the rules for upgrade action
     *
     * @param upgradeSpyAction is the to check UpgradeAction
     * @return error message or null if legal
     */
    public String check(UpgradeSpyAction upgradeSpyAction) {
        String tName = upgradeSpyAction.getTerritory();
        String srcUnitType = upgradeSpyAction.getSrcUnitType();
        String owner = upgradeSpyAction.getName();
        int num=upgradeSpyAction.getNum();
        try {
            Territory territory = check_territory_exist(tName);
            check_own(territory, owner, true);
            Map<String, Integer> unitInfo = new HashMap<>();
            unitInfo.put(srcUnitType, num);
            check_unit(territory, unitInfo);
            if (resourceHelpers.get(owner).getResourcesAttr().get("technology") < resourceHelpers.get(owner).calSpyUpgradeConsume(upgradeSpyAction)) {
                throw new IllegalArgumentException("Insufficient technology resource");
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * check all the rules for upgrade tech action
     *
     * @param upgradeTechAction is the to check UpgradeAction
     * @return error message or null if legal
     */
    public String check(UpgradeTechAction upgradeTechAction) {
        try {
            check_levelResources(upgradeTechAction);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * check all the rules for upgrade tech action
     *
     * @param upgradeTechAction is the to check UpgradeAction
     * @return error message or null if legal
     */
    public String check(UpgradeCloakAction upgradeCloakAction) {
        String owner=upgradeCloakAction.getName();
        try {
            if (resourceHelpers.get(owner).getTechLevel()<3){
                throw new IllegalArgumentException("Insufficient Tech Level to research Cloak");
            }
            if (resourceHelpers.get(owner).getResourcesAttr().get("technology")<200){
                throw new IllegalArgumentException("Insufficient technology resource to research Cloak");
            }
            resourceHelpers.get(owner).cloakFlag=true;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * check all the rules for upgrade tech action
     *
     * @param upgradeTechAction is the to check UpgradeAction
     * @return error message or null if legal
     */
    public String check(CloakingAction clockingAction) {
        try {
            // if (cloakFlag==false){
            if (this.resourceHelpers.get(clockingAction.getName()).cloakFlag == false) {
                throw new IllegalArgumentException("Need research cloaking first");
            }
            String owner=clockingAction.getName();
            String tName=clockingAction.getTerritory();
            Territory territory = check_territory_exist(tName);
            check_own(territory, owner, true);
            if (resourceHelpers.get(owner).getResourcesAttr().get("technology") < resourceHelpers.get(owner).calClockConsume()) {
                throw new IllegalArgumentException("Insufficient technology resource");
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * check if the given territory exist
     *
     * @param t is the territory name
     * @return Territory object of the given name or throw exception if not found
     * @throws IllegalArgumentException
     */
    private Territory check_territory_exist(String t) throws IllegalArgumentException {
        Territory territory = gameMap.getTerritoryByName(t);
        if (territory != null) {
            return territory;
        } else {
            throw new IllegalArgumentException("Invalid territory " + t);
        }
    };

    /**
     * check if the given territory owned by owner
     *
     * @param territory is the territory to examine
     * @param owner     is the owner of the action
     * @param is_own    specify whether the territory should be owned or not
     * @return True is the ownership is correct or throw exception if not found
     * @throws IllegalArgumentException
     */
    private boolean check_own(Territory territory, String owner, boolean is_own) throws IllegalArgumentException {
        if (is_own) {
            if (territory.getOwner().compareTo(owner) == 0) {
                return true;
            } else {
                throw new IllegalArgumentException(territory.getName() + " Territory isn't own by you and illegal");
            }
        } else {
            if (territory.getOwner().compareTo(owner) != 0) {
                return true;
            } else {
                throw new IllegalArgumentException(territory.getName() + " Territory is own by you and illegal");
            }
        }
    };

    /**
     * check if the unit num given is legal
     * void is the unit and number is legal or throw exception if unit not exist or
     * number>unit.num
     *
     * @param territory is the territory to examine
     * @param unitinfo  is the unitInfo of the action
     * @throws IllegalArgumentException
     */
    private void check_unit(Territory territory, Map<String, Integer> unitinfo) throws IllegalArgumentException {
        for (String unitname : unitinfo.keySet()) {
            Unit unit = territory.getUnitByName(unitname);
            if (unit == null) {
                throw new IllegalArgumentException(unitname + " unit does not exist");
            }
            if (unitinfo.get(unitname) > unit.getNum()) {
                throw new IllegalArgumentException("Invalid Unit num input " + unitname + " " + unitinfo.get(unitname));
            }
        }
    };

    private void check_upgrade_unit(UpgradeUnitAction upgradeUnitAction, Territory territory, String owner) {
        UpgradeInfo upgradeInfo = upgradeUnitAction.getUpgradeInfo();
        Map<String, Integer> unitinfo = new HashMap<>();
        unitinfo.put(upgradeInfo.getSrcUnitType(), upgradeInfo.getNum());
        unitinfo.put(upgradeInfo.getTarUnitType(), 0);
        check_unit(territory, unitinfo);
        Unit toCal = new Unit("toCal");
        int src_level = toCal.unitNameList.indexOf(upgradeInfo.getSrcUnitType());
        int dst_level = toCal.unitNameList.indexOf(upgradeInfo.getTarUnitType());
        if (dst_level < src_level) {
            throw new IllegalArgumentException("Cannot upgrade to lower level");
        }
        if (dst_level > resourceHelpers.get(owner).getTechLevel()) {
            // System.out.println(dst_level);
            // System.out.println(resourceHelpers.get(owner).getTechLevel());
            throw new IllegalArgumentException("You technology level is insufficient for this upgrade");
        }
        ResourceHelper resource = resourceHelpers.get(owner);
        if (resource.calUnitUpgradeConsume(src_level, dst_level, upgradeInfo.getNum()) > resource.getResourcesAttr()
                .get("technology")) {
            // System.out.println(resource.calUnitUpgradeConsume(src_level,
            // dst_level,upgradeInfo.getNum()));
            // System.out.println(resource.getResourcesAttr().get("technology"));
            throw new IllegalArgumentException("Insufficient technology resources for upgrade");
        }
    }

    /**
     * for move action, check if two territories linked together
     *
     * @param src_t is the source territory
     * @param dst_t is the destination territory
     * @param owner is the owner of the action
     * @return true if two territories are linked else false
     * @throws IllegalArgumentException
     */
    private boolean check_linked(String src_t, String dst_t, String owner) throws IllegalArgumentException {
        Map<String, List<String>> neigh_lib = gameMap.getTerritoryInfo();
        HashSet<String> neigh_set = new HashSet<String>();
        Stack<String> neigh_stack = new Stack<String>();
        neigh_stack.addAll(neigh_lib.get(src_t));
        while (!neigh_stack.isEmpty()) {
            String curr = neigh_stack.pop();
            if (neigh_set.contains(curr)) {
                continue;
            }
            if (curr.compareTo(dst_t) == 0) {
                return true;
            } else {
                if (gameMap.getTerritoryByName(curr).getOwner().compareTo(owner) == 0) {
                    neigh_stack.addAll(neigh_lib.get(curr));
                }
                neigh_set.add(curr);
            }
        }
        throw new IllegalArgumentException(src_t + " is not linked to " + dst_t);
    };

    /**
     * for attack action, check if two territories are directly adjacent
     *
     * @param src_t is the source territory
     * @param dst_t is the destination territory
     * @return true if two territories are adjacent else false
     * @throws IllegalArgumentException
     */
    private boolean check_adjacent(String src_t, String dst_t) throws IllegalArgumentException {
        Map<String, List<String>> neigh_lib = gameMap.getTerritoryInfo();
        List<String> neigh = neigh_lib.get(src_t);
        for (String t : neigh) {
            if (t.compareTo(dst_t) == 0) {
                return true;
            }
        }
        throw new IllegalArgumentException(src_t + " is not adjacent to " + dst_t);
    };

    private void check_foodResources(MoveAttackAction action) throws IllegalArgumentException {
        ResourceHelper playerResourceHelper = resourceHelpers.get(action.getName());
        if (playerResourceHelper.getResourcesAttr().get("food") < playerResourceHelper.calFoodConsume(gameMap,
                action)) {
            throw new IllegalArgumentException("Food resources is insufficient for action");
        }
    }

    private void check_levelResources(Action action) throws IllegalArgumentException {
        ResourceHelper playerResourceHelper = resourceHelpers.get(action.getName());
        if (playerResourceHelper.getResourcesAttr().get("technology") < playerResourceHelper.calLevelUpgradeConsume()) {
            throw new IllegalArgumentException("Technology resources is insufficient for upgrade");
        }
        ;
    }
};
