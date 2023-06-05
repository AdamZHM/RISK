package edu.duke.ece651.risc.shared;

import java.util.*;

public abstract class MoveAttackAction extends Action {


    /**
     * This is the name of the source territory
     */
    private String source;

    /**
     * This is the name of the destination territory
     */
    private String destination;

    /**
     * This is a map containing unit info of the action
     * Key is the specific name/level of the unit
     * Value is the specific number of that unit involved in the action
     */
    private Map<String, Integer> unitInfo;


    /**
     * This is the default constructor of the class
     */
    public MoveAttackAction() {
        super("", null);
        this.source = "";
        this.destination = "";
        this.unitInfo = new HashMap<>();
        for (String unitKey: Unit.unitNameList) {
            this.unitInfo.put(unitKey, 0);
        }
    }

    /**
     * This is the constructor of the class 
     * @param name is the name of player
     */
    public MoveAttackAction(String name) {
        super(name, null);
        this.source = "";
        this.destination = "";
        this.unitInfo = new HashMap<>();
        for (String unitKey: Unit.unitNameList) {
            this.unitInfo.put(unitKey, 0);
        }
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
    public MoveAttackAction(int num, String source, String destination, String name, String unitname,Action next) {
        super(name, next);
        this.source = source;
        this.destination = destination;
        this.unitInfo = new HashMap<>();
        // for (String unitKey: Unit.unitNameMap.keySet()) {
        //     this.unitInfo.put(unitKey, 0);
        // }
        this.unitInfo.put(unitname,num);
    }

    /**
     * This is the constructor of the class
     *
     * @param moveAttackInfo is the Info map that specifies this action
     * @param source is the source territory
     * @param destination is the destination territory
     * @param name is the name of the owner of this action
     * @param next is a pointer to the next action
     */
    public MoveAttackAction(Map<String, Integer> moveAttackInfo, String source, String destination, String name, Action next) {
        super(name, next);
        this.source = source;
        this.destination = destination;
        this.unitInfo = new HashMap<>();
        for (String unitKey: moveAttackInfo.keySet()) {
            this.unitInfo.put(unitKey, moveAttackInfo.get(unitKey));
        }
    }

    /**
     * Insert attributes into this unit
     *
     * @param source is the source territory
     * @param destination is the destination territory
     * @param unitName is the unit name
     * @param num is the number of unit
     */
    @Override
    public void updateParam(String source, String destination, String unitName, int num) {
        this.source=source;
        this.destination=destination;
        this.unitInfo.put(unitName, num);
    }


    /**
     * Get the name of the source territory
     *
     * @return the name of the source territory
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Get the name of the destination territory
     *
     * @return the name of the destination territory
     */
    public String getDestination() {
        return this.destination;
    }

    /**
     * Get the unit information in this action
     *
     * @return the unit information involved in this action
     */
    public Map<String, Integer> getUnitInfo() {
        return this.unitInfo;
    }

    /**
     * Get units' numbers involved in this action as list, units not involved have number of 0
     *
     * @return units' numbers involved in this action as list
     */
    public List<Integer> getCompleteUnitInfoAsList() {
        List<Integer> res = new ArrayList<>();
        for (String name : Unit.unitNameList) {
            res.add(this.unitInfo.containsKey(name) ? this.unitInfo.get(name) : 0);
        }
        return res;
    }

    public int getTotalUnitNum(){
        return unitInfo.values().stream().reduce(0,Integer::sum);
    }
}
