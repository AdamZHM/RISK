package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackMoveActionTest {

    @Test
    public void test_attack_action() {
        AttackAction action1 = new AttackAction(10, "start", "end", "test", "testUnit", null);
        //assertEquals(10, action1.getNum());
        assertEquals("start", action1.getSource());
        assertEquals("end", action1.getDestination());
        //assertEquals("testUnit", action1.getUnitName());
        assertEquals(false, action1.hasNext());
        assertEquals(null, action1.next());
        AttackAction action2 = new AttackAction(10, "start", "end", "test", "testUnit", action1);
        assertEquals(true, action2.hasNext());
        assertEquals(action1, action2.next());
        AttackAction action3 = new AttackAction();
        assertEquals("", action3.getName());
    }

    @Test
    public void test_move_action() {
        MoveAction action1 = new MoveAction(10, "start", "end", "test", "testUnit", null);
        //assertEquals(10, action1.getNum());
        assertEquals("start", action1.getSource());
        assertEquals("end", action1.getDestination());
        //assertEquals("testUnit", action1.getUnitName());
        assertEquals(false, action1.hasNext());
        assertEquals(null, action1.next());
        MoveAction action2 = new MoveAction(10, "start", "end", "test", "testUnit", action1);
        assertEquals(true, action2.hasNext());
        assertEquals(action1, action2.next());
        MoveAction action3 = new MoveAction();
        assertEquals("", action3.getName());
    }

    @Test
    public void test_constructor() {
        MoveAction moveAction = new MoveAction("test");
        assertEquals("", moveAction.getSource());
        assertEquals("", moveAction.getDestination());
        assertEquals(0, moveAction.getUnitInfo().get("soldier"));
        AttackAction attackAction = new AttackAction("test");
        assertEquals("", attackAction.getSource());
        assertEquals("", attackAction.getDestination());
        assertEquals(0, attackAction.getUnitInfo().get("soldier"));
        attackAction.updateParam("source", "destination", "unitName", 1);
        assertEquals("source", attackAction.getSource());
    }

}
