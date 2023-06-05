package edu.duke.ece651.risc.shared;

import java.io.Serializable;

/**
 * This is the abstract class for Action
 */
public abstract class Action implements Serializable {
    /**
     * This is the name of the player who owns the action
     */
    private String name;

    /**
     * This is a pointer to the next action
     */
    public Action nextAction;

    /**
     * This is the constructor for Action class
     *
     * @param name is the name of the player who owns the action
     * @param nextAction is a pointer to the next action
     */
    public Action(String name, Action nextAction) {
        this.name = name;
        this.nextAction = nextAction;
    }

    /**
     * This method accepts a visitor to visit itself
     *
     * @param actionVisitor is the visitor to take in
     */
    public abstract String accept(ActionVisitor actionVisitor);

        /**
     * This method update action parameter per system input
     *
     * @param 
     */
    public abstract void updateParam(String p1, String p2, String p3, int p4);

    /**
     * Get the name of the owner of the action
     *
     * @return the name of the owner of the action
     */
    public String getName() {
        return this.name;
    }

    /**
     * get the next Action
     *
     * @return the next Action
     */
    public Action next() {
        return this.nextAction;
    }

    /**
     * check if next Action exists
     *
     * @return true if next Action exists else false
     */
    public boolean hasNext() {
        return this.nextAction == null ? false : true;
    }

}
