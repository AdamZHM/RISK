package edu.duke.ece651.risc.shared;

public class MoveSpyAction extends Action {

    /**
     * This is the name of the source territory
     */
    private String srcTerritory;

    /**
     * This is the name of the destination territory
     */
    private String destTerritory;

    /**
     * This is the number of spy to move
     */
    private int num;

    /**
     * This is the constructor for Action class
     *
     * @param name       is the name of the player who owns the action
     */
    public MoveSpyAction(String name) {
        super(name, null);
    }

    /**
     * This is the constructor of the class
     *
     * @param srcTerritory is the name of the source territory
     * @param destTerritory is the name of the destination territory
     * @param num is the number of spy to move
     * @param name is the name of the player
     * @param nextAction is the nextAction instance
     */
    public MoveSpyAction(String srcTerritory, String destTerritory, int num, String name, Action nextAction) {
        super(name, nextAction);
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.num = num;
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
     * @param p1 srcTerritory
     * @param p2 destTerritory
     * @param p3 spacehold
     * @param p4 num
     */
    @Override
    public void updateParam(String p1, String p2, String p3, int p4) {
        this.srcTerritory=p1;
        this.destTerritory=p2;
        this.num=p4;
    }

    /**
     * get the name of the source territory
     *
     * @return the name of the source territory
     */
    public String getSrcTerritory() {
        return this.srcTerritory;
    }

    /**
     * get the name of the destination territory
     *
     * @return the name of the destination territory
     */
    public String getDestTerritory() {
        return this.destTerritory;
    }

    /**
     * get the number of spy to move
     *
     * @return the number of spy to move
     */
    public int getNum() {
        return this.num;
    }

}
