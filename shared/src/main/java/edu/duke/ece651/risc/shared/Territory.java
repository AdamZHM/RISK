package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

/**
 * This is the territory class
 */
public class Territory implements Serializable{

    /**
     * This is the name of the territory
     */
    private String name;

    /**
     * This is the owner of the territory
     */
    private String owner;

    /**
     * This contains a list of unit objects in the territory
     */
    private Map<String, Unit> myUnits;

    /**
     * This tells how much of each resource this territory produces each round
     */
    private Map<String, Integer> myResources;

    /**
     * This contains the neighbors of the territory
     */
    private Map<String, Territory> myNeighbors;

    /**
     * This contains the cost to this territory's neighbors
     */
    private Map<String, Integer> myCostToNeighInfo;

    /**
     * This is the constructor of Territory class
     *
     * @param name specifies value to set the name field to
     * @param owner specifies value to set the owner field to
     */
    public Territory(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.myUnits = new HashMap<>();
        setUpMyUnits();
        this.myNeighbors = new HashMap<>();
        this.myCostToNeighInfo = new HashMap<>();
        this.myResources = new HashMap<>();
    }

    /**
     * Set the owner of the territory to specified value
     *
     * @param owner is the specified value to set the owner of the territory to
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Get the owner of the territory
     *
     * @return the territory's owner in string type
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Get the units of the territory
     *
     * @return a list of unit objects in the territory
     */
    public Map<String, Unit> getMyUnits() {
        return this.myUnits;
    }

    /**
     * Get the resource creation information of this territory
     *
     * @return a map containing the map creation information of this territory
     */
    public Map<String, Integer> getMyResources() {
        return this.myResources;
    }

    /**
     * Get unit instance by its name
     *
     * @param unitName is the specified unit name
     * @return the unit instance correspondent to the specified unit name
     */
    public Unit getUnitByName(String unitName) {
        return this.myUnits.get(unitName);
    }

    /**
     * Get a list of units in unit strength order
     *
     * @return a list of units in unit strength order
     */
    public List<Unit> getMyUnitsAsList() {
        List<Unit> res = new ArrayList<>();
        for (String name: Unit.unitNameList) {
            res.add(this.myUnits.get(name));
        }
        return res;
    }

    /**
     * Get all units' numbers in list
     *
     * @return all units' numbers in list
     */
    public List<Integer> getUnitsNumsAsList() {
        List<Integer> res = new ArrayList<>();
        for (String name: Unit.unitNameList) {
            res.add(this.myUnits.get(name).getNum());
        }
        return res;
    }

    /**
     * Get the neighbors of the territory
     *
     * @return a map containing neighbors, key - name of neighbor territory, value - neighbor territory instance
     */
    public Map<String, Territory> getMyNeighbors() {
        return this.myNeighbors;
    }

    /**
     * Get the cost to this territory's neighbors
     *
     * @return the cost this all this territory's neighbors
     */
    public Map<String, Integer> getMyCostToNeighInfo() {
        return this.myCostToNeighInfo;
    }

    /**
     * Get the cost from this territory to the specified neighbor territory
     *
     * @return the cost from this territory to the specified neighbor
     */
    public int getCostToNeighbor(String neighborName) {
        return myCostToNeighInfo.get(neighborName);
    }

    /**
     * Returns the name of the territory
     *
     * @return the name of the territory in string type
     */
    public String getName() {
        return this.name;
    }

    /**
     * Helper function to set up territory's units
     */
    private void setUpMyUnits() {
        for (String unitName: Unit.unitNameList) {
            this.myUnits.put(unitName, new Unit(unitName));
        }
    }

    public String getInfo() {
        String toReturn = "";
        toReturn += "Name: " + this.name + "\n";
        toReturn += "Owner: " + this.owner + "\n";
        toReturn += "Troops:\n";
        for (String unitName: myUnits.keySet()) {
            toReturn += myUnits.get(unitName).getNum() + " " + unitName + "\n";
        }
        return toReturn;
    }
}
