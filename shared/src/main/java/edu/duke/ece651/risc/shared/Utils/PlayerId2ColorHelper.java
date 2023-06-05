package edu.duke.ece651.risc.shared.Utils;

import java.util.HashMap;
import java.util.Map;

public class PlayerId2ColorHelper {
  public final static Map<Integer, String> playerId2Color = new HashMap<>() {
    {
      put(0, "Red");
      put(1, "Green");
      put(2, "Blue");
      put(3, "Black");
      put(4, "White");
    }
  };
  
  public final static Map<String, Integer> playerColor2Id = new HashMap<>() {
    {
      put("Red", 0);
      put("Green", 1);
      put("Blue", 2);
      put("Black", 3);
      put("White", 4);
    }
  };
}
