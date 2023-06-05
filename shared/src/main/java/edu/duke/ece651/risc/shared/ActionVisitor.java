package edu.duke.ece651.risc.shared;

/**
 * This is the Action Visitor interface
 */
public interface ActionVisitor {

    /**
     * The visit method to visit MoveAction class
     *
     * @param moveAction is the moveAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(MoveAction moveAction);

    /**
     * The visit method to visit AttackAction class
     *
     * @param attackAction is the attackAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(AttackAction attackAction);

    /**
     * The visit method to visit UpgradeAction class
     *
     * @param upgradeUnitAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeUnitAction upgradeUnitAction);

    /**
     * The visit method to visit UpgradeTechAction class
     *
     * @param upgradeTechAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeTechAction upgradeTechAction);

    /**
     * The visit method to visit UpgradeSpyAction class
     *
     * @param upgradeSpyAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeSpyAction upgradeSpyAction);

     /**
     * The visit method to visit UpgradeSpyAction class
     *
     * @param upgradeSpyAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeCloakAction upgradeCloakAction);

    /**
     * The visit method to visit CloakingAction class
     *
     * @param cloakingAction is the CloakingAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(CloakingAction cloakingAction);

    /**
     * The visit method to visit MoveSpyAction class
     *
     * @param moveSpyAction is the MoveSpyAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(MoveSpyAction moveSpyAction);

}
