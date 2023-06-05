package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.duke.ece651.risc.shared.Utils.Status;

/**
 * This is the Messenger class
 */
public class Messenger {
  private Socket socket;
  private ObjectOutputStream objectOutputStream;
  private ObjectInputStream objectInputStream;

  /**
   * construct a client which connect to IP:port
   * 
   * @param hostName   the server host name
   * @param portNumber the port that server accepts
   * @throws IOException
   * @throws UnknownHostException
   */
  public Messenger(String hostName, int portNumber) throws UnknownHostException, IOException {
    this.socket = new Socket(hostName, portNumber);
    this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
    this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
  }

  /**
   * Socket that server accept connection from client
   * 
   * @param serverSocket server socket
   */
  public Messenger(ServerSocket serverSocket) throws UnknownHostException, IOException {
    this.socket = serverSocket.accept();
    this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
    this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
  }

  /**
   * send serialized object
   * 
   * @param o object to send
   * @throws IOException
   */
  public void send(Object o) throws IOException {
    objectOutputStream.writeObject(o);
    objectOutputStream.flush();
    objectOutputStream.reset();
  }

  /**
   * Recv serialized object
   *
   * @return the serialized object received
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Object recv() throws IOException, ClassNotFoundException {
    return objectInputStream.readObject();
  }

  /**
   * close the socket
   * 
   * @throws IOException
   */
  public void closeMessenger() throws IOException {
    this.socket.close();
  }

  /**
   * This function is to check if the socket is connected
   * 
   * @return true if socket is connected.
   * @throws IOException
   */
  public void messengerAlive() throws IOException {
    this.socket.sendUrgentData(0xFF);
    this.socket.sendUrgentData(0xFF);
  }
}
