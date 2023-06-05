package edu.duke.ece651.risc.shared;

public class CloakingAction extends Action {

    // only hides from “adjacency” viewing, not from a spy in the territory (spy has higher priority than cloaking)

    /**
     * This is the name of the territory to place cloak
     */
    private String territory;

    /**
     * This is the constructor for Action class
     *
     * @param name       is the name of the player who owns the action
     * @param nextAction is a pointer to the next action
     */
    public CloakingAction(String name, Action nextAction) {
        super(name, nextAction);
    }

    /**
     * This is the constructor of the class
     *
     * @param name is the name of the player who owns the action
     */
    public CloakingAction(String name) {
        this(name, null);
    }

    /**
     * This is the constructor of the class
     *
     * @param territory is the name of the territory to place cloak
     * @param name is the name of the player who owns the action
     * @param nextAction is the nextAction instance
     */
    public CloakingAction(String territory, String name, Action nextAction) {
        super(name, nextAction);
        this.territory = territory;
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
     *
     * @param p1 territory
     * @param p2 place hold
     * @param p3 place hold
     * @param p4 place hold
     */
    @Override
    public void updateParam(String p1, String p2, String p3, int p4) {
        this.territory=p1;
    }

    /**
     * Get the name of the territory to place cloak at
     *
     * @return the name of the territory to place cloak at
     */
    public String getTerritory() {
        return this.territory;
    }


}
