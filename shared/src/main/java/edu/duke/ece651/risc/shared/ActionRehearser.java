package edu.duke.ece651.risc.shared;

import java.util.Map;

/**
 * This is the Action Rehearser class
 */
public class ActionRehearser {

    /**
     * This is the game map instance
     */
    public GameMap gameMap;

    /**
     * This is a list of all players' attributions
     */
    public Map<String, ResourceHelper> resourceHelpers;

    /**
     * This is a list of all players' visions
     */
    public Map<String, VisionHelper> visionHelpers;

    /**
     *
     * @param gameMap
     * @param resourceHelpers
     */
    public ActionRehearser(GameMap gameMap, Map<String, ResourceHelper> resourceHelpers, Map<String, VisionHelper> visionHelpers) {
        this.gameMap = gameMap;
        this.resourceHelpers = resourceHelpers;
        this.visionHelpers = visionHelpers;
    }

    /**
     * This method performs rehearse on MoveAction class
     *
     * @param moveAction is the moveAction instance to perform rehearse on
     */
    public void rehearse(MoveAction moveAction) {
        // Step1: Update number of units involved in the action
        deductUnitNumFromSrc(moveAction);
        String destTerritory = moveAction.getDestination();
        Map<String, Integer> moveInfo = moveAction.getUnitInfo();
        for (String moveUnit: moveInfo.keySet()) {
            Unit destUnit = gameMap.getTerritoryByName(destTerritory).getUnitByName(moveUnit);
            destUnit.setNum(destUnit.getNum() + moveInfo.get(moveUnit));
        }

        // Step2: Update player's resources involved in the action
        moveAttackActionUpdateResourceHelper(moveAction);
    }

    /**
     * This method performs rehearse on AttackAction class
     *
     * @param attackAction is the attackAction instance to perform rehearse on
     */
    public void rehearse(AttackAction attackAction) {
        // Step1: Update number of units involved in the action
        deductUnitNumFromSrc(attackAction);

        // Step2: Update player's resources involved in the action
        moveAttackActionUpdateResourceHelper(attackAction);
    }

    /**
     * This method performs rehearse on UpgradeAction class
     *
     * @param upgradeUnitAction is the attackAction instance to perform rehearse on
     */
    public void rehearse(UpgradeUnitAction upgradeUnitAction) {
        // Step1: Update number of units involved in the action
        UpgradeInfo upgradeInfo = upgradeUnitAction.getUpgradeInfo();
        Map<String, Unit> units = gameMap.getTerritoryByName(upgradeInfo.getTerritory()).getMyUnits();
        int num = upgradeInfo.getNum();
        Unit srcUnit = units.get(upgradeInfo.getSrcUnitType());
        Unit tarUnit = units.get(upgradeInfo.getTarUnitType());
        srcUnit.setNum(srcUnit.getNum() - num);
        tarUnit.setNum(tarUnit.getNum() + num);

        // Step2: Update player's resources involved in the action
        String actionOwner = upgradeUnitAction.getName();
        ResourceHelper resourceHelper = resourceHelpers.get(actionOwner);
        Map<String, Integer> resources = resourceHelper.getResourcesAttr();
        int srcLevel=srcUnit.unitNameList.indexOf(srcUnit.getName());
        int tarLevel=tarUnit.unitNameList.indexOf(tarUnit.getName());
        int cost = resourceHelper.calUnitUpgradeConsume(srcLevel, tarLevel,num);
        resources.put("technology", (resources.get("technology") - cost));
    }

    /**
     * This method performs rehearse on UpgradeTechAction class
     *
     * @param upgradeTechAction is the attackAction instance to perform rehearse on
     */
    public void rehearse(UpgradeTechAction upgradeTechAction) {
        // Step1: Update player's resources involved in the action
        ResourceHelper resourceHelper = this.resourceHelpers.get(upgradeTechAction.getName());
        Map<String, Integer> myResources = resourceHelper.getResourcesAttr();
        int techLevel = resourceHelper.getTechLevel();
        myResources.put("technology", myResources.get("technology") - ResourceHelper.technologyLevelCostMap.get(techLevel + 1));
        return;
    }

    /**
     * This method performs rehearse on UpgradeSpyAction class
     *
     * @param upgradeSpyAction is the UpgradeSpyAction instance to perform rehearse on
     */
    public void rehearse(UpgradeSpyAction upgradeSpyAction) {
        String owner = upgradeSpyAction.getName();
        String srcUnit = upgradeSpyAction.getSrcUnitType();
        String territory = upgradeSpyAction.getTerritory();
        int num = upgradeSpyAction.getNum();
        Unit unit = gameMap.getTerritoryByName(territory).getMyUnits().get(srcUnit);
        unit.setNum(unit.getNum() - num);
        Map<String, Integer> mySpies = this.visionHelpers.get(owner).getSpyInfo();
        mySpies.put(territory, mySpies.getOrDefault(territory, 0) + num);
        ResourceHelper resourceHelper = this.resourceHelpers.get(upgradeSpyAction.getName());
        Map<String, Integer> myResources = resourceHelper.getResourcesAttr();
        myResources.put("technology", myResources.get("technology") - resourceHelper.calSpyUpgradeConsume(upgradeSpyAction));
        return;
    }

    /**
     * This method performs rehearse on CloakingAction class
     *
     * @param cloakingAction is the CloakingAction instance to perform rehearse on
     */
    public void rehearse(CloakingAction cloakingAction) {
        String owner = cloakingAction.getName();
        String territory = cloakingAction.getTerritory();
        Map<String, Integer> myCloak = this.visionHelpers.get(owner).getCloakInfo();
        myCloak.put(territory, myCloak.getOrDefault(territory, 0) + 3);
        ResourceHelper resourceHelper = this.resourceHelpers.get(cloakingAction.getName());
        Map<String, Integer> myResources = resourceHelper.getResourcesAttr();
        myResources.put("technology", myResources.get("technology") - resourceHelper.calClockConsume());
        return;
    }

    /**
     * This method performs rehearser on UpgradeCloakAction class
     *
     * @param upgradeCloakAction is the UpgradeCloakAction instance to perform rehearse on
     */
    public void rehearse(UpgradeCloakAction upgradeCloakAction) {
        String owner = upgradeCloakAction.getName();
        ResourceHelper resourceHelper = this.resourceHelpers.get(upgradeCloakAction.getName());
        resourceHelper.cloakFlag = true;
        Map<String, Integer> myResources = resourceHelper.getResourcesAttr();
        myResources.put("technology", myResources.get("technology") -resourceHelper.calCloakUpgradeConsume());
        return;
    }

    /**
     * This method performs rehearse on MoveSpyAction class
     *
     * @param moveSpyAction is the MoveSpyAction instance to perform rehearse on
     */
    public void rehearse(MoveSpyAction moveSpyAction) {
        String owner = moveSpyAction.getName();
        String srcTerritory = moveSpyAction.getSrcTerritory();
        String destTerritory = moveSpyAction.getDestTerritory();
        int num = moveSpyAction.getNum();
        Map<String, Integer> mySpies = this.visionHelpers.get(owner).getSpyInfo();
        mySpies.put(srcTerritory, mySpies.get(srcTerritory) - num);
        mySpies.put(destTerritory, mySpies.get(destTerritory) + num);
        ResourceHelper resourceHelper = this.resourceHelpers.get(moveSpyAction.getName());
        Map<String, Integer> myResources = resourceHelper.getResourcesAttr();
        myResources.put("food", myResources.get("food") - resourceHelper.calSpyMoveConsume(moveSpyAction));
        return;
    }

    /**
     * This is the helper function to reduce unit num from source territory
     *
     * @param action is the examined MoveAttackAction instance
     */
    private void deductUnitNumFromSrc(MoveAttackAction action) {
        //int numToChange = action.getNum();
        String srcTerritory = action.getSource();
        //String unitName = action.getUnitName();
        Map<String, Integer> moveAttackInfo = action.getUnitInfo();
        for (String moveAttackUnit: moveAttackInfo.keySet()) {
            Unit srcUnit = gameMap.getTerritoryByName(srcTerritory).getUnitByName(moveAttackUnit);
            srcUnit.setNum(srcUnit.getNum() - moveAttackInfo.get(moveAttackUnit));
        }
    }

    /**
     * This is the helper function update resource for move/attack actions
     *
     * @param action is the action the update resource for
     */
    private void moveAttackActionUpdateResourceHelper(MoveAttackAction action) {
        String actionOwner = action.getName();
        ResourceHelper resourceHelper = resourceHelpers.get(actionOwner);
        int cost = resourceHelper.calFoodConsume(this.gameMap, action);
        Map<String, Integer> resources = resourceHelper.getResourcesAttr();
        resources.put("food", resources.get("food") - cost);
    }

}
