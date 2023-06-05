package edu.duke.ece651.risc.shared;

import java.util.Map;

/**
 * This is the MoveAction class
 */
public class MoveAction extends MoveAttackAction {

    /**
     * This is the default constructor of the class
     */
    public MoveAction() {
        super();
    }

    /**
     * This is the constructor of the class
     * @param name is the owner of the action
     */
    public MoveAction(String name) {
        super(name);
    }

    /**
     * This is the constructor of the class
     *
     * @param num is the number of unit in the action
     * @param source is the name of the source territory
     * @param destination is the name of the destination territory
     * @param name is the name of the owner of the action
     * @param next is a pointer to the next action
     */
    public MoveAction(int num, String source, String destination, String name, String unitName, Action next) {
        super(num, source, destination, name, unitName, next);
    }

    /**
     * This is the constructor of the class
     *
     * @param moveInfo is the moveInfo map that specifies this action
     * @param source is the source territory
     * @param destination is the destination territory
     * @param name is the name of the owner of this action
     * @param next is a pointer to the next action
     */
    public MoveAction(Map<String, Integer> moveInfo, String source, String destination, String name, Action next) {
        super(moveInfo, source, destination, name, next);
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
}
