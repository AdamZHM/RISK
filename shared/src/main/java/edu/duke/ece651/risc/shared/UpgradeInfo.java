package edu.duke.ece651.risc.shared;

import java.io.Serializable;

/**
 * This is the Upgrade Info Class
 */
public class UpgradeInfo implements Serializable {

    /**
     *
     */
    private String territory;

    /**
     * This is the source unit type
     */
    private String srcUnitType;

    /**
     * This is the target unit type
     */
    private String tarUnitType;

    /**
     * This is the specified number of unit to upgrade
     */
    private int num;

    /**
     * This is the constructor of the class
     *
     * @param srcUnitType is the source unit type
     * @param tarUnitType is the target unit type
     * @param num is the specified number of unit to upgrade
     */
    public UpgradeInfo(String territory, String srcUnitType, String tarUnitType, int num) {
        this.territory = territory;
        this.srcUnitType = srcUnitType;
        this.tarUnitType = tarUnitType;
        this.num = num;
    }

    /**
     *
     * @return
     */
    public String getTerritory() {
        return this.territory;
    }

    /**
     * Get the source unit type
     *
     * @return the source unit type
     */
    public String getSrcUnitType() {
        return this.srcUnitType;
    }

    /**
     * Get the target unit type
     *
     * @return the target unit type
     */
    public String getTarUnitType() {
        return this.tarUnitType;
    }

    /**
     * Get the number of unit to upgrade
     *
     * @return the number of unit to upgrade
     */
    public int getNum() {
        return this.num;
    }

}

