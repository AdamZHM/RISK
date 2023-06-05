package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.jupiter.api.Test;

public class MessengerTest {

  @Test
  public void test_sendGameInfo() throws IOException, Exception {
    Thread th = new Thread() {
      @Override()
      public void run() {
        try {
          ServerSocket serverSocket = new ServerSocket(23456);
          Messenger serverMessenger = new Messenger(serverSocket);
          serverMessenger.send("ABC");
          serverMessenger.messengerAlive();
          // serverMessenger.closeMessenger();
          // serverSocket.close();
        } catch (Exception e) {

        }
      }
    };
    th.start();
    Thread.sleep(100); // this is a bit of hack
    Messenger messenger = new Messenger("127.0.0.1", 23456);
    String message = (String) messenger.recv();
    assertEquals("ABC", message);
    
    messenger.closeMessenger();
    th.interrupt();
    th.join();
  }
}
