package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the Vision Helper Class
 */
public class VisionHelper implements Serializable {

    /**
     * This contains the cloak info of my territories
     * key is the territory name (only my own territories), value is the remaining
     * cloak
     */
    public Map<String, Integer> cloakInfo;

    /**
     * Keeps vision information
     * key is the territory name (only enemy territories), value is displayed info
     * for that territory (could be blank)
     */
    public Map<String, String> visionInfo;

    /**
     * Keeps spy information
     * key is the territory name (all territories are possible), value is the num of
     * spy in that territory
     */
    public Map<String, Integer> spyInfo;

    /**
     * This is the name of the owner of this vision helper
     */
    public String owner;

    /**
     *
     */
    public VisionHelper() {};

    /**
     * This is the constructor of the class
     */
    public VisionHelper(GameMap map, String owner) {
        this.owner = owner;
        this.cloakInfo = new HashMap<>();
        this.visionInfo = new HashMap<>();
        this.spyInfo = new HashMap<>();
        for (Territory territory : map.getMyTerritories().values()) {
            // Initialize cloakInfo
            cloakInfo.put(territory.getName(), 0);
            // Initialize spyInfo
            spyInfo.put(territory.getName(), 0);
            // Initialize the vision information for all territories
            visionInfo.put(territory.getName(), "<No Info Available>\n");
            if (territory.getOwner().equals(owner)) {
                // Update the vision information of owner's own territories
                visionInfo.put(territory.getName(), "<Latest Info>\n" + territory.getInfo());
                visionInfo.put(territory.getName(), visionInfo.get(territory.getName()) + "Cloak round: " + this.cloakInfo.get(territory.getName()) + "\n");
            } else {
                for (Territory neigh : territory.getMyNeighbors().values()) {
                    if (neigh.getOwner().equals(owner)) {
                        // Update the vision information of adjacent territories
                        visionInfo.put(territory.getName(), "<Latest Info>\n" + territory.getInfo());
                    }
                }
            }
        }
    }

    public void updateMyCloakPerRound(GameMap map) {
        for (String territoryName: this.cloakInfo.keySet()) {
            if (map.getTerritoryByName(territoryName).getOwner().equals(owner)) {
                int curr_num = this.cloakInfo.get(territoryName);
                if (curr_num >= 1) {
                    this.cloakInfo.put(territoryName, curr_num - 1);
                }
            }
        }
    }

    public void updateEnemyCloakPerRound(GameMap map) {
        for (String territoryName: this.cloakInfo.keySet()) {
            if (!map.getTerritoryByName(territoryName).getOwner().equals(owner)) {
                int curr_num = this.cloakInfo.get(territoryName);
                if (curr_num >= 1) {
                    this.cloakInfo.put(territoryName, curr_num - 1);
                }
            }
        }
    }

    /**
     * This is used to upgrade the cloaking information per round
     */
    public void updateCloakingPerRound() {
        // TODO this method should be called after updateVisionPerRound()
        for (String territoryName : this.cloakInfo.keySet()) {
            int curr_num = this.cloakInfo.get(territoryName);
            if (curr_num >= 1) {
                this.cloakInfo.put(territoryName, curr_num - 1);
            }
        }
    }

    /**
     * This is used at the client side to upgrade vision immediately after each action
     *
     * @param map is the map instance that it relies on
     */
    public void updateVisionPerAction(GameMap map) {
        for (Territory territory : map.getMyTerritories().values()) {
            if (territory.getOwner().equals(owner)) {
                // Update the vision information of owner's own territories
                visionInfo.put(territory.getName(), "<Latest Info>\n" + territory.getInfo());
                visionInfo.put(territory.getName(), visionInfo.get(territory.getName()) + "Cloak round: " + this.cloakInfo.get(territory.getName()) + "\n");
            }
        }
    }

    /**
     * This should be called right before the end of each round to upgrade the vision
     *
     * @param map is the map instance it relies on
     * @param visionHelpers is the vision helpers it relies on
     */
    public void updateVisionPerRound(GameMap map, Map<String, VisionHelper> visionHelpers) {
        for (Territory territory : map.getMyTerritories().values()) {
            System.out.println("----------------------------------------------");
            System.out.println("Currently updating: " + territory.getName() + "\n");
            if (territory.getOwner().equals(owner)) {
                System.out.println("This is my own territory, I can definitely see it\n");
                // Update the vision information of owner's own territories
                visionInfo.put(territory.getName(), "<Latest Info>\n" + territory.getInfo());
                visionInfo.put(territory.getName(), visionInfo.get(territory.getName()) + "Cloak round: " + this.cloakInfo.get(territory.getName()) + "\n");
            } else if (checkSpyAt(territory)) {
                System.out.println("This is not my territory, but I have spy here, so I can see it\n");
                visionInfo.put(territory.getName(), "<Latest Info>\n" + territory.getInfo());
            } else {
                // Flag to check if the territory info is going to be become an old one
                String tOwner = territory.getOwner();
                System.out.println("This is not my territory, the owner is " + tOwner +"\n");
                // if (territory.getName().equals("Midkemia")) System.out.println(territory.getInfo());
                Boolean isOld = true;
                // Update the vision information of territories that don't belong to owner
                if (!checkTerritoryCloak(visionHelpers, territory)) { // step2: make sure the territory is not cloaked
                    System.out.println("This territory is not cloaked\n");
                    for (String name : territory.getMyNeighbors().keySet()) {
                        Territory neigh = map.getTerritoryByName(name);
                        if (neigh.getOwner().equals(this.owner)) {
                            // System.out.println(neigh.getInfo());
                            // System.out.println("Neighbor owner: " + neigh.getOwner());
                            // System.out.println("Neighbor owner (in real map): " + map.getTerritoryByName(neigh.getName()).getOwner());
                            // System.out.println("This territory's owner: " + this.owner);
                            System.out.println("Found neighbor territory that belongs to me, and that is " + neigh.getName() +"\n");
                            // step1: adjacent to owner's territories
                            // The territory info is no longer going to be an old one
                            isOld = false;
                            visionInfo.put(territory.getName(), "<Latest Info>\n" + territory.getInfo());
                            break;
                        }
                    }
                }
                if (isOld == true) {
                    System.out.print("info becomes old!!!\n");
                    // The territory info becomes an old one
                    if (!visionInfo.get(territory.getName()).equals("<No Info Available>\n")) {
                        visionInfo.put(territory.getName(), getOldInfoFromLatest(visionInfo.get(territory.getName())));
                    }
                }
            }
        }
    }

    /**
     * This method checks if there's spy at the specified territory
     *
     * @param territory is the specified territory to check
     * @return true if there's spy else false
     */
    public Boolean checkSpyAt(Territory territory) {
        if (spyInfo.containsKey(territory.getName()) && spyInfo.get(territory.getName()) > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method checks if there's cloak (by any other enemies) at the specified territory
     *
     * @param visionHelpers is the visionhelpes that this method relies on
     * @param territory is the specified territory to check
     * @return true if there's cloak else false
     */
    public Boolean checkTerritoryCloak(Map<String, VisionHelper> visionHelpers, Territory territory) {
        String owner = territory.getOwner();
        String name = territory.getName();
        VisionHelper visionHelper = visionHelpers.get(owner);
        Map<String, Integer> cloakInfo = visionHelper.cloakInfo;
        if (!cloakInfo.containsKey(name)) {
            return false;
        }
        if (cloakInfo.get(name) <= 0) {
            return false;
        }
        return true;
    }

    /**
     * This is a helper method to convert latest info to old info
     *
     * @param s is the string to convert
     * @return the converted string
     */
    public String getOldInfoFromLatest(String s) {
        String toReturn = "<Old Info>\n" + s.substring(s.indexOf('\n') + 1);
        return toReturn;
    }

    /**
     * Get the cloak info
     *
     * @return the cloak info
     */
    public Map<String, Integer> getCloakInfo() {
        return this.cloakInfo;
    }

    /**
     * Get the vision info
     *
     * @return the vision info
     */
    public Map<String, String> getVisionInfo() {
        return this.visionInfo;
    }

    /**
     * Get the spy info
     *
     * @return the spy info
     */
    public Map<String, Integer> getSpyInfo() {
        return this.spyInfo;

    }

    /**
     * Get the ower
     *
     * @return the owner's name
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Get the spy info in formatted string
     *
     * @return the spy info formatted in string
     */
    public String getSpyInfoStr() {
        String toReturn = "<Spy Info>\n";
        Boolean hasSpy = false;
        for (String key: this.spyInfo.keySet()) {
            if (spyInfo.get(key) > 0) {
                toReturn += key + ": " + this.spyInfo.get(key) + "\n";
                hasSpy = true;
            }
        }
        if (!hasSpy) {
            toReturn += "You currently have no spy\n";
        }
        return toReturn;
    }

}
