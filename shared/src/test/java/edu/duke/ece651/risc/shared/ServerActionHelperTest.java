package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ServerActionHelperTest {

    private GameMap test_helper() {
        GameMap m = new GameMap(3);
        Map<String, Territory> mt = m.getMyTerritories();
        mt.get("Scadrial").setOwner("Player1");
        mt.get("Scadrial").getUnitByName("soldier").setNum(10);
        mt.get("Roshar").setOwner("Player1");
        mt.get("Roshar").getUnitByName("soldier").setNum(4);
        mt.get("Mordor").setOwner("Player2");
        mt.get("Mordor").getUnitByName("soldier").setNum(14);
        mt.get("Narnia").setOwner("Player1");
        mt.get("Narnia").getUnitByName("soldier").setNum(10);
        mt.get("Oz").setOwner("Player2");
        mt.get("Oz").getUnitByName("soldier").setNum(8);
        mt.get("Midkemia").setOwner("Player2");
        mt.get("Midkemia").getUnitByName("soldier").setNum(12);
        return m;
    }

    private List<Action> getTestActions() {
        List<Action> res = new ArrayList<>();

        Map<String, Integer> attackInfo1 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction1 = new AttackAction(attackInfo1, "Scadrial", "Mordor", "Player1", null);
        Map<String, Integer> moveInfo1 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction1 = new MoveAction(moveInfo1, "Scadrial", "Roshar", "Player1", attackAction1);
        Map<String, Integer> moveInfo2 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction2 = new MoveAction(moveInfo2, "Scadrial", "Roshar", "Player1", moveAction1);

        Map<String, Integer> attackInfo3 = new HashMap<>() {
            {
                put("soldier", 1);
            }
        };
        AttackAction attackAction3 = new AttackAction(attackInfo3, "Scadrial", "Mordor", "Player1", null);
        Map<String, Integer> attackInfo2 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction2 = new AttackAction(attackInfo2, "Scadrial", "Mordor", "Player1", attackAction3);
        Map<String, Integer> moveInfo3 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction3 = new MoveAction(moveInfo3, "Roshar", "Scadrial", "Player1", attackAction2);
        Map<String, Integer> moveInfo4 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction4 = new MoveAction(moveInfo4, "Roshar", "Scadrial", "Player1", moveAction3);

        Map<String, Integer> attackInfo4 = new HashMap<>() {
            {
                put("soldier", 1);
            }
        };
        AttackAction attackAction4 = new AttackAction(attackInfo4, "Scadrial", "Oz", "Player1", null);
        Map<String, Integer> attackInfo5 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction5 = new AttackAction(attackInfo5, "Scadrial", "Mordor", "Player1", attackAction4);
        Map<String, Integer> attackInfo6 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction6 = new AttackAction(attackInfo6, "Scadrial", "Mordor", "Player1", attackAction5);
        Map<String, Integer> moveInfo5 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        Map<String, Integer> attackInfo7 = new HashMap<>() {
            {
                put("warrior", 2);
            }
        };
        AttackAction attackAction7 = new AttackAction(attackInfo7, "Scadrial", "Mordor", "Player1", attackAction6);
        MoveAction moveAction5 = new MoveAction(moveInfo5, "Roshar", "Scadrial", "Player1", attackAction7);
        Map<String, Integer> moveInfo6 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction6 = new MoveAction(moveInfo6, "Roshar", "Scadrial", "Player1", moveAction5);

        res.add(moveAction2);
        res.add(moveAction4);
        res.add(moveAction6);

        return res;
    }

    private List<Action> getAllPlayerActions() {
        List<Action> res = new ArrayList<>();

        Map<String, Integer> attackInfo4 = new HashMap<>() {
            {
                put("soldier", 1);
            }
        };
        AttackAction attackAction4 = new AttackAction(attackInfo4, "Scadrial", "Oz", "Player1", null);
        Map<String, Integer> attackInfo5 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction5 = new AttackAction(attackInfo5, "Scadrial", "Mordor", "Player1", attackAction4);
        Map<String, Integer> moveInfo5 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction5 = new MoveAction(moveInfo5, "Roshar", "Scadrial", "Player1", attackAction5);
        Map<String, Integer> moveInfo6 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction6 = new MoveAction(moveInfo6, "Roshar", "Scadrial", "Player1", moveAction5);

        Map<String, Integer> attackInfo6 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        AttackAction attackAction6 = new AttackAction(attackInfo6, "Mordor", "Scadrial", "Player2", null);
        Map<String, Integer> moveInfo7 = new HashMap<>() {
            {
                put("soldier", 2);
            }
        };
        MoveAction moveAction7 = new MoveAction(moveInfo7, "Mordor", "Oz", "Player2", attackAction6);

        res.add(moveAction6);
        res.add(moveAction7);

        return res;
    }

    private List<Action> badActions() {
        List<Action> res = new ArrayList<>();

        Map<String, Integer> attackInfo = new HashMap<>() {
            {
                put("soldier", 1);
            }
        };
        AttackAction attackAction = new AttackAction(attackInfo, "Scadrial", "Oz", "Player2", null);

        res.add(attackAction);

        return res;
    }

    @Test
    public void test_get_attack_action() {
        GameMap testMap = test_helper();
        List<Action> testActions = getTestActions();
        ServerActionHelper helper = new ServerActionHelper(testMap, getResourceHelpers(), getVisionHelpers());
        AttackAction action = helper.getAttackAction(testActions.get(0));
        assertEquals(true, action != null);
        assertEquals(2, action.getUnitInfo().get("soldier"));
    }

    @Test
    public void test_get_merged_attack_action() {
        GameMap testMap = test_helper();
        List<Action> testActions = getTestActions();
        ServerActionHelper helper = new ServerActionHelper(testMap, getResourceHelpers(), getVisionHelpers());
        AttackAction action = helper.getMergedAttackAction(helper.getAttackAction(testActions.get(2)));
        assertEquals(true, action != null);
        assertEquals(null, action.nextAction.nextAction);
        int count = 0;
        while (action != null) {
            System.out.println(action.getDestination() + " " + action.getName());
            count += action.getUnitInfo().get("soldier");
            action = (AttackAction) action.nextAction;
        }
        assertEquals(5, count);
    }

    @Test
    public void test_get_merged_attack_action_of_all_players() {
        GameMap testMap = test_helper();
        List<Action> testActions = getAllPlayerActions();
        ServerActionHelper helper = new ServerActionHelper(testMap, getResourceHelpers(), getVisionHelpers());
        helper.headActions = testActions;
        AttackAction action = helper.getMergedAttackActionOfAllPlayers();
        int count = 0;
        int attackCount = 0;
        Set<String> set = new HashSet<>();
        while (action != null) {
            count += 1;
            attackCount += action.getUnitInfo().get("soldier");
            System.out.println(action.getDestination() + " " + action.getName());
            assertEquals(false, set.contains(action.getDestination() + action.getName()));
            set.add(action.getDestination() + action.getName());
            action = (AttackAction) action.nextAction;
        }
        assertEquals(3, count);
        assertEquals(5, attackCount);
    }

    @Test
    public void test_do_execute() {
        GameMap testMap = test_helper();
        ResourceHelper resourceHelper = new ResourceHelper();
        resourceHelper.updateResoursePerRound(testMap, "Player1");
        resourceHelper.updateResoursePerRound(testMap, "Player2");
        List<Action> testActions = getAllPlayerActions();
        ServerActionHelper helper = new ServerActionHelper(testMap, getResourceHelpers(), getVisionHelpers());
        for (Action action : testActions) {
            helper.doCheckRehearser(action);
        }
        helper.doExecute();
        GameMapTextView view = new GameMapTextView();
        System.out.println(view.displayMyTerritory(helper.gameMap, "Player1"));
        System.out.println(view.displayMyTerritory(helper.gameMap, "Player2"));

        testMap = test_helper();

        helper = new ServerActionHelper(testMap, getResourceHelpers(), getVisionHelpers());
        helper.doExecute();
    }

    @Test
    public void test_bad_attack_actions() {
        GameMap testMap = test_helper();
        List<Action> testActions = badActions();
        ServerActionHelper helper = new ServerActionHelper(testMap, getResourceHelpers(), getVisionHelpers());
        for (Action action : testActions) {
            String result = helper.doCheckRehearser(action);
            assertEquals("Scadrial Territory isn't own by you and illegal", result);
        }
    }

    @Test
    public void test_get_shuffled_actions() {
        GameMap testMap = test_helper();
        List<Action> testActions = getAllPlayerActions();
        ServerActionHelper helper = new ServerActionHelper(testMap, getResourceHelpers(), null);
        helper.headActions = testActions;
        AttackAction action = helper.getMergedAttackActionOfAllPlayers();
        AttackAction cur1 = action;
        System.out.println("Action order before shuffle");
        while (cur1 != null) {
            System.out.println(cur1.getName() + ": " + cur1.getSource() + "->" + cur1.getDestination());
            cur1 = (AttackAction) cur1.nextAction;
        }
        AttackAction shuffledAction = (AttackAction) helper.getShuffledActions(action);
        AttackAction cur2 = shuffledAction;
        System.out.println("Action order after shuffle");
        while (cur2 != null) {
            System.out.println(cur2.getName() + ": " + cur2.getSource() + "->" + cur2.getDestination());
            cur2 = (AttackAction) cur2.nextAction;
        }
    }
    
    @Test
    public void test_get_shuffled_actions_with_null_input() {
        GameMap testMap = test_helper();
        ServerActionHelper helper = new ServerActionHelper(testMap, getResourceHelpers(), getVisionHelpers());
        helper.initPlayerResource("a");
        helper.updatePlayerResourcePerRound();
        assertEquals(null, helper.getShuffledActions(null));
    }

    @Test
    public void test_reset_action() {
        GameMap map = test_helper();
        ResourceHelper resourceHelper = new ResourceHelper();
        resourceHelper.updateResoursePerRound(map, "Player1");
        ServerActionHelper p = new ServerActionHelper(map, getResourceHelpers(), getVisionHelpers());
        MoveAction ac5 = new MoveAction(3, "Roshar", "Scadrial", "Player1", "soldier", null);
        ServerActionHelper serverActionHelper = new ServerActionHelper(map);
        serverActionHelper.getMap();
        p.doCheckRehearser(ac5);
        assertEquals(null, p.doCheckRehearser(null));
        assertEquals(true, p.headActions.size() != 0);
        p.resetAction();
        assertEquals(true, p.headActions.size() == 0);
    }

    @Test
    public void test_get_map() {
        GameMap map = test_helper();
        ServerActionHelper p = new ServerActionHelper(map, getResourceHelpers(), getVisionHelpers());
        GameMap returnedMap = p.getMap();
        assertEquals("Player1", returnedMap.getTerritoryByName("Scadrial").getOwner());
        assertEquals("Player1", returnedMap.getTerritoryByName("Roshar").getOwner());
        assertEquals("Player1", returnedMap.getTerritoryByName("Narnia").getOwner());
    }

    private Map<String, ResourceHelper> getResourceHelpers() {
        Map<String, ResourceHelper> res = new HashMap<>(){{
            put("Player1", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
            put("Player2", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
            put("Player3", new ResourceHelper(5, new HashMap<>(){{put("food", 100); put("technology", 100);}}));
        }};
        return res;
    }

    public Map<String, VisionHelper> getVisionHelpers() {
        Map<String, VisionHelper> res = new HashMap<>() {{
            put("Player1", new VisionHelper(test_helper(), "Player1"));
            put("Player2", new VisionHelper(test_helper(), "Player2"));
            put("Player3", new VisionHelper(test_helper(), "Player3"));
        }};
        return res;
    }

}
