package edu.duke.ece651.risc.shared;

public class UpgradeSpyAction extends Action {

    /**
     * This is the territory at where to upgrade spy
     */
    private String territory;

    /**
     * This is the name of the unit to be upgraded to spy
     */
    private String srcUnitType;

    /**
     * This is the number of unit to upgrade
     */
    private int num;

    /**
     * This is the constructor for Action class
     *
     * @param name       is the name of the player who owns the action
     */
    public UpgradeSpyAction(String name) {
        super(name, null);
    }

    /**
     * This is the constructor of the class
     *
     * @param territory is the territory at where to upgrade spy
     * @param srcUnitType is the name of the unit to be upgraded to spy
     * @param num is the number of unit to upgrade
     * @param owner is the name of the player who owns the action
     * @param nextAction is the next action instance
     */
    public UpgradeSpyAction(String territory, String srcUnitType, int num, String owner, Action nextAction) {
        super(owner, nextAction);
        this.territory = territory;
        this.srcUnitType = srcUnitType;
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
     * @param p1 territory
     * @param p2 place hold
     * @param p3 source Unit Type
     * @param p4 num
     */
    @Override
    public void updateParam(String p1, String p2, String p3, int p4) {
        this.territory=p1;
        this.srcUnitType=p3;
        this.num=p4;
    }

    /**
     * get the name of the territory at where to upgrade spy
     *
     * @return the name of the territory at where to upgrade spy
     */
    public String getTerritory(){
        return territory;
    }

    /**
     * the name of the unit to be upgraded to spy
     *
     * @return the name of the unit to be upgraded to spy
     */
    public String getSrcUnitType(){
        return srcUnitType;
    }

    /**
     * get the number of the unit to upgrade
     *
     * @return the number of the unit to upgrade
     */
    public int getNum() {
        return num;
    }


}
