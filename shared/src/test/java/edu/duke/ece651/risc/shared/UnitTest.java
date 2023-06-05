package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTest {

    @Test
    public void test_unit() {
        Unit unit = new Unit("test");
        assertEquals(0, unit.getNum());
        assertEquals("test", unit.getName());
        unit.setNum(10);
        assertEquals(10, unit.getNum());
    }

}
