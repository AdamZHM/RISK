package edu.duke.ece651.risc.shared;

import java.util.Map;

/**
 * This is the player action helper class
 */
public class PlayerActionHelper extends ActionHelper {

    protected Action headAction;
    protected Action tailAction;
    public Action dummyTailAction;// used to store the Upgrade Tech Action

    /**
     * This is the constructor of the class
     *
     * @param gameMap         is the GameMap it takes
     * @param resourceHelpers is the helper to calculate resource
     */
    public PlayerActionHelper(GameMap gameMap, Map<String, ResourceHelper> resourceHelpers, Map<String, VisionHelper> visionHelpers) {
        super(gameMap, resourceHelpers, visionHelpers);
        headAction = null;
        tailAction = null;
        dummyTailAction = null;
    }

    public PlayerActionHelper(GameMap gameMap) {
        super(gameMap);
        headAction = null;
        tailAction = null;
        dummyTailAction = null;
    }

    /**
     * Get the head action instance
     *
     * @return the head action instance
     */
    public Action getHeadAction() {
        if (headAction == null && dummyTailAction != null) {
            return dummyTailAction;
        }
        return headAction;
    }

    /**
     * return the tail Action
     */
    public Action getTailAction() {
        if (dummyTailAction != null) {
            return dummyTailAction;
        } else {
            return tailAction;
        }
    }

    /**
     * This method update the tail or head Action for Action chain
     * 
     * @param action is the new Action
     */
    public void setTailAction(Action action) {
        if (action instanceof UpgradeTechAction) {
            dummyTailAction = action;
            if (this.tailAction != null) {
                tailAction.nextAction = dummyTailAction;
            }
        } else {
            if (this.headAction == null) {
                headAction = action;
            } else {
                tailAction.nextAction = action;
            }
            tailAction = action;
            action.nextAction = dummyTailAction;
        }
    }

    /**
     * This method check if Action legal or not, then rehease the gamemap update
     *
     * @param action is the new received Action
     * @return if legal, return null; if not, return the error message
     */
    @Override
    public String doCheckRehearser(Action action) {
        String checkResult;
        if ((action instanceof UpgradeTechAction && dummyTailAction != null
                && dummyTailAction instanceof UpgradeTechAction)) {
            checkResult = "You could only upgrade technology level once in one round!";
            return checkResult;
        }
        checkResult = action.accept(actionCheckerRehearser);
        if (checkResult != null) {
            return checkResult;
        }
        sortAction(action);
        return null;
    }

    /**
     * This method sorts action so that all move actions are ahead of attack actions
     * 
     * @param action is the insert Action
     */
    private void sortAction(Action action) {
        if (action.getClass() != AttackAction.class && action.getClass() != UpgradeTechAction.class) {
            Action curr = headAction;
            while (curr != null) {
                if (curr.nextAction != null && curr.nextAction.getClass() == AttackAction.class) {
                    action.nextAction = curr.nextAction;
                    curr.nextAction = action;
                    return;
                } else {
                    curr = curr.nextAction;
                }
            }
        }
        setTailAction(action);
    }

    /**
     * This method reset Action chain after one round end
     */
    public void resetAction() {
        headAction = null;
        tailAction = null;
        dummyTailAction = null;
        actionCheckerRehearser.actionChecker.resetSpyMoveMap();
    }

    public void setupHelper(String playerName, ResourceHelper resourceHelper, VisionHelper visionHelper) {
        resourceHelpers.put(playerName, resourceHelper);
        //System.out.println(resourceHelper.getResourcesAttr().get("food"));
        visionHelpers.put(playerName,visionHelper);
        //System.out.println("Spy in Mordor should be 0, actual be "+visionHelper.getSpyInfo().get("Mordor"));
    }


    public void setVisionHelper(String playerName, VisionHelper visionHelper) {
        visionHelpers.put(playerName, visionHelper);
        // System.out.println(resourceHelper.getResourcesAttr().get("food"));
    }


}
