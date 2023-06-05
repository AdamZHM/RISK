package edu.duke.ece651.risc.shared;

import java.util.HashMap;
import java.util.Map;

public class UpgradeUnitAction extends Action {

    /**
     * This is a map containing upgrade info of the action
     * Key is the specific name/level of the unit
     * Value is the specific number of each type of unit to upgrade
     */
    private UpgradeInfo upgradeInfo;

    /**
     * This is the constructor of the class
     *
     * @param name is the name of the owner of the action
     * @param next is a pointer to the next action
     */
    public UpgradeUnitAction(String name, Action next, UpgradeInfo upgradeInfo) {
        super(name, next);
        // this.territory = territory;
        this.upgradeInfo = upgradeInfo;
    }

    public UpgradeUnitAction(String name) {
        super(name, null);
    }

    @Override
    public  void updateParam(String territory, String srcUnitType, String dstUnitType, int unitNum) {
        this.upgradeInfo = new UpgradeInfo(territory, srcUnitType, dstUnitType, unitNum);
    }

    /**
     * This method accepts a visitor to visit itself
     *
     * @param actionVisitor is the visitor to take in
     */
    @Override
    public String accept(ActionVisitor actionVisitor) {
        String visitResult = actionVisitor.visit(this);
        if (visitResult != null) {
            return visitResult;
        }
        return null;
    }

    /**
     * Get the unit upgrade information in this action
     *
     * @return the unit upgrade information involved in this action
     */
    public UpgradeInfo getUpgradeInfo() {
        return this.upgradeInfo;
    }
    
}

