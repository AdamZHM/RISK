package edu.duke.ece651.risc.shared;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This is the Action Executor class
 */
public class ActionExecutor extends ActionRehearser implements ActionVisitor {

    /**
     * The random seed for the computer text player
     */
    private int randomSeed;

    /**
     * The instance of the Random() used to mimic random behavior of computer
     */
    private Random rand;

    /**
     * This is the constructor of the class
     *
     * @param gameMap is the game map that the class should rely on
     */
    public ActionExecutor(GameMap gameMap, Map<String, ResourceHelper> resourceHelpers, Map<String, VisionHelper> visionHelpers, int randomSeed) {
        super(gameMap, resourceHelpers, visionHelpers);
        this.randomSeed = randomSeed;
        this.rand = new Random(randomSeed);
    }

    /**
     * The visit method to visit MoveAction class
     *
     * @param moveAction is the moveAction instance to visit
     * @return error message in String if there's any else null
     */
    @Override
    public String visit(MoveAction moveAction) {
        if (moveAction.hasNext()) {
            return moveAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit AttackAction class
     *
     * @param attackAction is the attackAction instance to visit
     * @return error message in String if there's any else null
     */
    @Override
    public String visit(AttackAction attackAction) {
        // Implement the execution logics (besides minus the unit num from source territory, which is done during rehearsal) for attack action
        // step 1: Figure out the number of unit in the destination territory
        // step 2: Implement a random dice for each side that ranges from 0 - 20, e.g., rand.nextInt(some number);
        // step 3: Pre set up "pairing rule, e.g., At first, highest-bonus attacker unit paired with the lowest-bonus defender unit"
        // step 4: While both side have units left, do...
        // step 4.2: Get the specific unit involved in this rolling based on the "pairing rule"
        // step 4.3: While both units are not equal to zero
        // step 4.3.1: Roll dice for both side
        // step 4.3.2: Add bonus points to the rolled dice
        // step 4.3.3: Determine who wins, minus one from the lost unit
        // step 4.4: Switch the "pairing rule"
        // step 5: change the owner of the destination territory accordingly
        Territory tarTerritory = gameMap.getTerritoryByName(attackAction.getDestination());
        List<Integer> tarUnits = tarTerritory.getUnitsNumsAsList();

        int count = 0;
        for (int tarUnit: tarUnits) count += tarUnit;

        int tarHead = 0, tarTail = tarUnits.size() - 1;
        while (tarHead < tarUnits.size() && tarUnits.get(tarHead) == 0) tarHead += 1;
        while (tarTail >= 0 && tarUnits.get(tarTail) == 0) tarTail -= 1;

        List<Integer> srcUnits = attackAction.getCompleteUnitInfoAsList();

        if (count == 0) {
            // no unit at the target territory, attacker wins directly
            tarTerritory.setOwner(attackAction.getName());
            List<Unit> units = tarTerritory.getMyUnitsAsList();
            for (int i = 0; i < units.size(); i += 1) {
                units.get(i).setNum(srcUnits.get(i));
            }
            if (attackAction.hasNext()) {
                return attackAction.nextAction.accept(this);
            }
            return null;
        }

        int srcHead = 0, srcTail = srcUnits.size() - 1;
        while (srcHead < srcUnits.size() && srcUnits.get(srcHead) == 0) srcHead += 1;
        while (srcTail >= 0 && srcUnits.get(srcTail) == 0) srcTail -= 1;

        boolean pairFlag = false; // IF FALSE, ATTACKER STARTS WITH TAIL (HIGHEST), DEFENDER WITH HEAD (LOWEST), ELSE THE OPPOSITE
        int srcCur, tarCur;

        while (tarHead <= tarTail && srcHead <= srcTail) {
            if (!pairFlag) {
                srcCur = srcTail;
                tarCur = tarHead;
            } else {
                srcCur = srcHead;
                tarCur = tarTail;
            }
            int srcNum = srcUnits.get(srcCur);
            int tarNum = tarUnits.get(tarCur);

            int srcRand;
            int tarRand;

            while (srcNum > 0 && tarNum > 0) {
                srcRand = rand.nextInt(20) + Unit.unitBonusList.get(srcCur);
                tarRand = rand.nextInt(20) + Unit.unitBonusList.get(tarCur);
                if (srcRand <= tarRand) {
                    // attack fails
                    srcNum -= 1;
                } else {
                    // attack success
                    tarNum -= 1;
                }
            }

            srcUnits.set(srcCur, srcNum);
            tarUnits.set(tarCur, tarNum);

            if (!pairFlag) {
                while (srcTail >= 0 && srcUnits.get(srcTail) == 0) srcTail -= 1;
                while (tarHead < tarUnits.size() && tarUnits.get(tarHead) == 0) tarHead += 1;
            } else {
                while (srcHead < srcUnits.size() && srcUnits.get(srcHead) == 0) srcHead += 1;
                while (tarTail >= 0 && tarUnits.get(tarTail) == 0) tarTail -= 1;
            }
            pairFlag = !pairFlag;
        }

        List<Integer> numsToUpdate;

        if (tarHead > tarTail) {
            // Attacker wins the combat
            // if defender has cloaking at this territory, make it zero
            Map<String, Integer> tarCloakInfo = visionHelpers.get(tarTerritory.getOwner()).getCloakInfo();
            if (tarCloakInfo.get(tarTerritory.getName()) >= 1) {
                tarCloakInfo.put(tarTerritory.getName(), 0);
            }
            // assign territory owner to attacker
            tarTerritory.setOwner(attackAction.getName());
            numsToUpdate = srcUnits;
        } else {
            // Defender wins the combat
            numsToUpdate = tarUnits;
        }

        List<Unit> units = tarTerritory.getMyUnitsAsList();
        for (int i = 0; i < units.size(); i += 1) {
            units.get(i).setNum(numsToUpdate.get(i));
        }

        if (attackAction.hasNext()) {
            return attackAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit UpgradeAction class
     *
     * @param upgradeUnitAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeUnitAction upgradeUnitAction) {
        if (upgradeUnitAction.hasNext()) {
            return upgradeUnitAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit UpgradeTechAction class
     *
     * @param upgradeTechAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeTechAction upgradeTechAction) {
        String actionOwner = upgradeTechAction.getName();

        ResourceHelper myResource = this.resourceHelpers.get(actionOwner);
        myResource.updateTechnologyLevel();

        if (upgradeTechAction.hasNext()) {
            return upgradeTechAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit UpgradeSpyAction class
     *
     * @param upgradeSpyAction is the upgradeAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(UpgradeSpyAction upgradeSpyAction) {
        if (upgradeSpyAction.hasNext()) {
            return upgradeSpyAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit CloakingAction class
     *
     * @param cloakingAction is the CloakingAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(CloakingAction cloakingAction) {
        if (cloakingAction.hasNext()) {
            return cloakingAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     * The visit method to visit MoveSpyAction class
     *
     * @param moveSpyAction is the MoveSpyAction instance to visit
     * @return error message in String if there's any else null
     */
    public String visit(MoveSpyAction moveSpyAction) {
        if (moveSpyAction.hasNext()) {
            return moveSpyAction.nextAction.accept(this);
        }
        return null;
    }

    /**
     *
     * @param upgradeCloakAction
     * @return
     */
    @Override
    public String visit(UpgradeCloakAction upgradeCloakAction) {
        if (upgradeCloakAction.hasNext()) {
            return upgradeCloakAction.nextAction.accept(this);
        }
        return null;
    }
}
