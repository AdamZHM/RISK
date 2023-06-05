package edu.duke.ece651.risc.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpgradeInfoTest {

    @Test
    void test_upgrade_info() {
        UpgradeInfo upgradeInfo = new UpgradeInfo("Oz", "soldier", "warrior", 3);
        assertEquals("Oz", upgradeInfo.getTerritory());
        assertEquals("soldier", upgradeInfo.getSrcUnitType());
        assertEquals("warrior", upgradeInfo.getTarUnitType());
        assertEquals(3, upgradeInfo.getNum());
    }

}
