package edu.duke.ece651.risc.shared;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This is the Action Helper class
 */
public abstract class ActionHelper {

    /**
     * This is the game map instance of the class
     */
    public GameMap gameMap;

    /**
     *
     */
    public Map<String, ResourceHelper> resourceHelpers;

    /**
     *
     */
    public Map<String, VisionHelper> visionHelpers;

    /**
     * This is the action check rehearser instance
     */
    public ActionCheckerRehearser actionCheckerRehearser;

    /**
     *
     * @param gameMap
     * @param resourceHelpers
     * @param visionHelpers
     */
    public ActionHelper(GameMap gameMap, Map<String, ResourceHelper> resourceHelpers, Map<String, VisionHelper> visionHelpers) {
        this.gameMap = gameMap;
        this.resourceHelpers = resourceHelpers;
        this.visionHelpers = visionHelpers;
        this.actionCheckerRehearser = new ActionCheckerRehearser(gameMap, resourceHelpers, visionHelpers);
    }

    /**
     *
     * @param gameMap
     */
    public ActionHelper(GameMap gameMap) {
        this.gameMap = gameMap;
        this.resourceHelpers = new HashMap<String, ResourceHelper>();
        this.visionHelpers = new HashMap<String, VisionHelper>();
        this.actionCheckerRehearser = new ActionCheckerRehearser(gameMap, resourceHelpers, visionHelpers);
    }

    /**
     *
     * @param playerName
     */
    public void initPlayerResource(String playerName) {
        this.resourceHelpers.put(playerName, new ResourceHelper());
        this.visionHelpers.put(playerName, new VisionHelper(gameMap,playerName));
    }

    /**
     *
     */
    public void updatePlayerResourcePerRound() {
        for (String playerName : this.resourceHelpers.keySet()) {
            resourceHelpers.get(playerName).updateResoursePerRound(gameMap, playerName);
            System.out.println(playerName + " " + resourceHelpers.get(playerName).getResourcesAttr().get("food"));
        }
    }

    /**
     *
     */
    public void updatePlayerVisionPerRound() {
        for (VisionHelper visionHelper: visionHelpers.values()) {
            visionHelper.updateMyCloakPerRound(gameMap);
            visionHelper.updateVisionPerRound(gameMap, visionHelpers);
            // visionHelper.updateCloakingPerRound();
            visionHelper.updateEnemyCloakPerRound(gameMap);
        }
    }

    /**
     * Do check and rehearse on given action
     *
     * @param action is the action to perform check and rehearse on
     * @return error message in String if there's any else null
     */
    public abstract String doCheckRehearser(Action action);

    /**
     * Get the map instance
     *
     * @return the map instance
     */
    public GameMap getMap() {
        return gameMap;
    }
}
