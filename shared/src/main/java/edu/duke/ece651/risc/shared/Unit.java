package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

/**
 * This is the Unit class
 */
public class Unit implements Serializable {

    /**
     * This is a static list containing all units' names
     */
    static public final List<String> unitNameList = new ArrayList<>(Arrays.asList("soldier", "warrior", "knight", "captain", "commander", "general", "king"));

    /**
     * This is a static list containing all units' bonus information
     */
    static public final List<Integer> unitBonusList = new ArrayList<>(Arrays.asList(0, 1, 3, 5, 8, 11, 15));

    /**
     * This is a static list containing all units' cost information
     */
    static public final List<Integer> unitCostList = new ArrayList<>(Arrays.asList(0, 3, 11, 30, 55, 90, 140));

    /**
     * This indicates the number of the unit
     */
    private int num;

    /**
     * This is the name of the unit
     */
    private String name;

    /**
     * This is the constructor of the Unit class
     */
    public Unit(String name) {
        this.num = 0;
        this.name = name;
    }

    /**
     * Set the number of the unit to specified value
     *
     * @param num is the specified value to set the number of the unit to
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Get the number of the unit
     *
     * @return the number in int type of the unit
     */
    public int getNum() {
        return this.num;
    }

    /**
     * Get the name of the unit
     *
     * @return the name of the unit
     */
    public String getName() {
        return this.name;
    }
}
