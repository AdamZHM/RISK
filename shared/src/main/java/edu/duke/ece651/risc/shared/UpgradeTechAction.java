package edu.duke.ece651.risc.shared;

public class UpgradeTechAction extends Action {

    /**
     * This is the constructor for Action class
     *
     * @param name       is the name of the player who owns the action
     * @param nextAction is a pointer to the next action
     */
    public UpgradeTechAction(String name, Action nextAction) {
        super(name, nextAction);
    }

    /**
     * This is the constructor of the class
     *
     * @param name is the name of the action owner
     */
    public UpgradeTechAction(String name) {
        this(name, null);
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

    @Override
    public void updateParam(String p1, String p2, String p3, int p4) {
        
    }

}
