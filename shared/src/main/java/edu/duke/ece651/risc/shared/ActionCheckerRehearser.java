package edu.duke.ece651.risc.shared;

import java.util.Map;

/**
 * This is the ActionCheckerRehearser class
 */
public class ActionCheckerRehearser implements ActionVisitor {

    /**
     * This is the action check instance
     */
    public ActionChecker actionChecker;

    /**
     * This is the action rehearser instance
     */
    public ActionRehearser actionRehearser;

    /**
     * This is the game map instance the class relies on
     */
    public GameMap gameMap;

    /**
     * This is a list of all players' attributions
     */
    public Map<String, ResourceHelper> resourceHelpers;

    /**
     *
     */
    public Map<String, VisionHelper> visionHelpers;

    /**
     *
     * @param gameMap
     * @param resourceHelpers
     * @param visionHelpers
     */
    public ActionCheckerRehearser(GameMap gameMap, Map<String, ResourceHelper> resourceHelpers, Map<String, VisionHelper> visionHelpers) {
        this.gameMap = gameMap;
        this.resourceHelpers = resourceHelpers;
        this.visionHelpers = visionHelpers;

        // TODO needs refactor
        this.actionChecker = new ActionChecker(this.gameMap, this.resourceHelpers, this.visionHelpers);
        this.actionRehearser = new ActionRehearser(this.gameMap, this.resourceHelpers, this.visionHelpers);
    }

    /**
     * The visit method to visit MoveAction class
     *
     * @param moveAction is the moveAction instance to visit
     * @return error message in String if there's any else null
     */
    @Override
    public String visit(MoveAction moveAction) {
        String checkResult = actionChecker.check(moveAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(moveAction);
        if (moveAction.hasNext()) {
            return moveAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit AttackAction class
     *
     * @param attackAction is the attackAction instance to visit
     * @return error message in String if there's any else null
     */
    @Override
    public String visit(AttackAction attackAction) {
        String checkResult = actionChecker.check(attackAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(attackAction);
        if (attackAction.hasNext()) {
            return attackAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit UpgradeAction class
     *
     * @param upgradeUnitAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    @Override
    public String visit(UpgradeUnitAction upgradeUnitAction) {
        String checkResult = actionChecker.check(upgradeUnitAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(upgradeUnitAction);
        if (upgradeUnitAction.hasNext()) {
            return upgradeUnitAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit UpgradeTechAction class
     *
     * @param upgradeTechAction is the upgradeTechAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeTechAction upgradeTechAction) {
        String checkResult = actionChecker.check(upgradeTechAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(upgradeTechAction);
        if (upgradeTechAction.hasNext()) {
            return upgradeTechAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit UpgradeSpyAction class
     *
     * @param upgradeSpyAction is the upgradeTechAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeSpyAction upgradeSpyAction) {
        String checkResult = actionChecker.check(upgradeSpyAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(upgradeSpyAction);
        if (upgradeSpyAction.hasNext()) {
            return upgradeSpyAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     *
     * @param cloakingAction is the CloakingAction instance to visit
     * @return
     */
    public String visit(CloakingAction cloakingAction) {
        String checkResult = actionChecker.check(cloakingAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(cloakingAction);
        if (cloakingAction.hasNext()) {
            return cloakingAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     *
     * @param moveSpyAction is the MoveSpyAction instance to visit
     * @return
     */
    public String visit(MoveSpyAction moveSpyAction) {
        String checkResult = actionChecker.check(moveSpyAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(moveSpyAction);
        if (moveSpyAction.hasNext()) {
            return moveSpyAction.nextAction.accept(this);
        }
        return null;
    }

    @Override
    public String visit(UpgradeCloakAction upgradeCloakAction) {
        String checkResult = actionChecker.check(upgradeCloakAction);
        if (checkResult != null) {
            return checkResult;
        }
        actionRehearser.rehearse(upgradeCloakAction);
        if (upgradeCloakAction.hasNext()) {
            return upgradeCloakAction.nextAction.accept(this);
        }
        return null;
    }
}
