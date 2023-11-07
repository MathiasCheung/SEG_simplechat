package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

import java.io.IOException;


/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  ChatIF serverUI;

  String loginKey = "loginID";

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) throws IOException
  {

    super(port);
    this.serverUI = serverUI;
    listen();
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
  (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client.getInfo(loginKey));

    String msgStr = (String)msg;
    if (msgStr.startsWith("#login")){
      String loginID = msgStr.substring(7,msgStr.length()-1);

      client.setInfo(loginKey,loginID);
      System.out.println("<" + client.getInfo(loginKey) + "> has logged on" );
    }
    else{
      this.sendToAllClients(client.getInfo(loginKey) + "> " + msgStr);
    }




  }

  public void handleMessageFromServerUI(String message)
  {
    try {
      if (message.startsWith("#")){
        handleCommand(message);
      }
      else {
        sendToAllClients("SERVERMSG" + "> " + message);
        serverUI.display(message);
      }
    }
    catch (IOException e){

    }


  }

  private void handleCommand(String command) throws IOException {
    if (command.equals("#quit")){
      quit();

    }
    else if (command.equals("#stop")){
      stopListening();

    }
    else if (command.equals("#close")){
      close();


    }
    else if (command.equals("#setport")) {
      if (!isListening()){
        setPort(getPort());
        serverUI.display("port set to "+getPort());
      }
      else {
        serverUI.display(command +" command can not be executed");
      }
    }

    else if (command.equals("#getport")) {
      serverUI.display(Integer.toString(getPort()));
    }
    else if (command.equals("#start")) {
      if (!isListening()){
        listen();

      }
      else {
        serverUI.display(command +" command can not be executed");
      }
    }
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
            ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
            ("Server has stopped listening for connections.");
  }

  /**
   * Implements the Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("a client has connected");
  }

  /**
   * Implements the Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println(client.getInfo(loginKey) + " has disconnected");
  }


  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  @Override
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println("a client has disconnected");

  }

  /**
   * Implements the Hook method called when the server is clased.
   * The default implementation does nothing. This method may be
   * overriden by subclasses. When the server is closed while still
   * listening, serverStopped() will also be called.
   */
  protected void serverClosed() {

    System.out.println("Server have closed");
  }

  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }



}
//End of EchoServer class
