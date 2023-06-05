package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerritoryTest {

    @Test
    public void test_territory() {
        Territory t = new Territory("Narnia", "owner");
        assertEquals("Narnia", t.getName());
        assertEquals("owner", t.getOwner());
        t.setOwner("someone");
        assertEquals("someone", t.getOwner());
        Map<String, Unit> myUnits = t.getMyUnits();
        assertEquals(Unit.unitNameList.size(), myUnits.size());
        for (String unitName: Unit.unitNameList) {
            assertEquals(unitName, myUnits.get(unitName).getName());
        }
        Map<String, Territory> myNeighbors = t.getMyNeighbors();
        assertEquals(0, myNeighbors.size());
    }
}
