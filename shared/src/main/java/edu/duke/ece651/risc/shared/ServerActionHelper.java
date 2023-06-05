package edu.duke.ece651.risc.shared;

import java.util.*;

/**
 * This is the server action helper class
 */
public class ServerActionHelper extends ActionHelper {

    /**
     * This is a list containing all players' head actions
     */
    public List<Action> headActions;

    /**
     * This is the injected action executor
     */
    private ActionExecutor actionExecutor;

    /**
     * This is the constructor of the class
     *
     * @param gameMap    is the GameMap it takes
     * @param resourceHelpers is the head action
     */
    public ServerActionHelper(GameMap gameMap, Map<String, ResourceHelper> resourceHelpers, Map<String, VisionHelper> visionHelpers) {
        super(gameMap,resourceHelpers, visionHelpers);
        headActions = new ArrayList<>();
        actionExecutor = new ActionExecutor(this.gameMap, this.resourceHelpers, this.visionHelpers, 42);
    }

    public ServerActionHelper(GameMap gameMap) {
        super(gameMap);
        headActions = new ArrayList<>();
        actionExecutor = new ActionExecutor(this.gameMap, this.resourceHelpers, this.visionHelpers, 42);
    }
    
    /**
     * Do check and rehearse on given action
     *
     * @param headAction is the action to perform check and rehearse on
     * @return error message in String if there's any else null
     */
    @Override
    public String doCheckRehearser(Action headAction) {
        if (headAction==null){
            return null;
        }
        String checkRehearseResult = headAction.accept(actionCheckerRehearser);
        if (checkRehearseResult != null) {
            System.out.println(checkRehearseResult);
            return checkRehearseResult;
        }
        headActions.add(headAction);
        return null;
        // this.gameMap = actionCheckerRehearser.gameMap;
    }

    /**
     * Do the execution part on the actions (for now, only the attack actions)
     */
    public void doExecute() {
        this.gameMap = actionCheckerRehearser.gameMap;
        UpgradeTechAction upgradeTechAction = getUpgradeTechActionOfAllPlayers();
        AttackAction headAttack = getMergedAttackActionOfAllPlayers();
        AttackAction shuffledHeadAttack = (AttackAction) getShuffledActions(headAttack);
        AttackAction cur = shuffledHeadAttack;
        if (cur == null) {
            if (upgradeTechAction != null) {
                upgradeTechAction.accept(actionExecutor);
            }
            return;
        }
        while (cur.nextAction != null) {
            cur = (AttackAction) cur.nextAction;
        }
        cur.nextAction = upgradeTechAction;
        shuffledHeadAttack.accept(actionExecutor);
    }

    /**
     *
     * @return
     */
    public UpgradeTechAction getUpgradeTechActionOfAllPlayers() {
        List<UpgradeTechAction> upgradeTechs = new ArrayList<>();
        for (Action headAction: headActions) {
            upgradeTechs.add(getUpgradeTechAction(headAction));
        }
        UpgradeTechAction dummy = new UpgradeTechAction("dummy", null);
        UpgradeTechAction cur = dummy;
        for (UpgradeTechAction upgradeTechAction : upgradeTechs) {
            cur.nextAction = (UpgradeTechAction) upgradeTechAction;
            while (cur.nextAction != null) {
                cur = (UpgradeTechAction) cur.nextAction;
            }
        }
        return (UpgradeTechAction) dummy.nextAction;
    }

    /**
     *
     * @param headAction
     * @return
     */
    public UpgradeTechAction getUpgradeTechAction(Action headAction) {
        UpgradeTechAction dummy = new UpgradeTechAction("dummy", null);
        UpgradeTechAction cur = dummy;
        Action action = headAction;
        while (action != null) {
            System.out.println(action.getClass());
            if (action.getClass().equals(UpgradeTechAction.class)) {
                System.out.println("shit");
                cur.nextAction = (UpgradeTechAction) action;
                cur = (UpgradeTechAction) cur.nextAction;
                break;
            }
            action = action.nextAction;
        }
        cur.nextAction = null;
        return (UpgradeTechAction) dummy.nextAction;
    }

    /**
     * Get the head of attackAction of all players after merging
     * Might return null if there is no attack action at all in any player
     *
     * @return the head of attackAction of all players after merging
     */
    public AttackAction getMergedAttackActionOfAllPlayers() {
        List<AttackAction> headAttacks = new ArrayList<>();
        for (Action headAction : headActions) {
            AttackAction headAttack = getMergedAttackAction(getAttackAction(headAction));
            headAttacks.add(headAttack);
        }
        AttackAction dummy = new AttackAction();
        AttackAction cur = dummy;
        for (AttackAction attackAction : headAttacks) {
            cur.nextAction = (AttackAction) attackAction;
            while (cur.nextAction != null) {
                cur = (AttackAction) cur.nextAction;
            }
        }
        return (AttackAction) dummy.nextAction;
    }

    /**
     * Randomly shuffle the action list
     *
     * @param headAction is the head action after random shuffling
     */
    public Action getShuffledActions(Action headAction) {
        if (headAction == null)
            return null;
        List<Action> toShuffle = new ArrayList<>();
        Action cur = headAction;
        while (cur != null) {
            toShuffle.add(cur);
            cur = cur.nextAction;
        }
        Collections.shuffle(toShuffle);
        Action toReturn = toShuffle.get(0);
        Action curr = toReturn;
        for (int i = 1; i < toShuffle.size(); i += 1) {
            curr.nextAction = toShuffle.get(i);
            curr = curr.nextAction;
        }
        curr.nextAction = null;
        return toReturn;
    }

    /**
     * Get the head attack action in the attack action list
     * This method may return null if there is no attack action at all
     *
     * @param headAction is the headAction to start the check with
     * @return the head AttackAction
     */
    public AttackAction getAttackAction(Action headAction) {
        AttackAction dummy = new AttackAction();
        AttackAction cur = dummy;
        Action action = headAction;
        while (action != null) {
            if (action.getClass().equals(AttackAction.class)) {
                cur.nextAction = (AttackAction) action;
                cur = (AttackAction) cur.nextAction;
            }
            action = action.nextAction;
        }
        cur.nextAction = null;
        return (AttackAction) dummy.nextAction;
    }

    /**
     * This method merge attack actions with same action owner and same destination
     * territory into one
     * Note: Might return null if there is no attack action at all
     *
     * @param headAction is the head attackAction to start the merging with
     * @return the head AttackAction after merging
     */
    public AttackAction getMergedAttackAction(AttackAction headAction) {
        Map<String, AttackAction> map = new HashMap<>();
        AttackAction cur = headAction;
        while (cur != null) {
            if (!map.containsKey(cur.getDestination())) {
                map.put(cur.getDestination(), cur);
            } else {
                Map<String, Integer> unitInfoToUpgrade = map.get(cur.getDestination()).getUnitInfo();
                Map<String, Integer> unitInfoToBeMerged = cur.getUnitInfo();
                for (String unitKey: unitInfoToBeMerged.keySet()) {
                    if (unitInfoToUpgrade.containsKey(unitKey)) {
                        unitInfoToUpgrade.put(unitKey,
                                unitInfoToBeMerged.get(unitKey) + unitInfoToUpgrade.get(unitKey));
                    } else {
                        unitInfoToUpgrade.put(unitKey, unitInfoToBeMerged.get(unitKey));
                    }
                }
            }
            Action nextAction = cur.nextAction;
            cur.nextAction = null;
            cur = (AttackAction) nextAction;
        }
        AttackAction dummy = new AttackAction();
        AttackAction curr = dummy;
        for (AttackAction action : map.values()) {
            curr.nextAction = action;
            curr = (AttackAction) curr.nextAction;
        }
        return (AttackAction) dummy.nextAction;
    }

    /**
     * Clear all actions after each round is complete
     */
    public void resetAction(){
        headActions.clear();
        actionCheckerRehearser.actionChecker.resetSpyMoveMap();
    }
}