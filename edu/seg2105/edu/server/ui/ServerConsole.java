package edu.seg2105.edu.server.ui;

import edu.seg2105.edu.server.backend.*;
import edu.seg2105.client.common.ChatIF;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class constructs the UI for a server.  It implements the
 * chat interface in order to activate the display() method.
 */

public class ServerConsole implements ChatIF {

    final public static int DEFAULT_PORT = 5555;

    EchoServer server;

    Scanner fromConsole;

    public ServerConsole(int port){
        try{
            server = new EchoServer(port, this);
        }
        catch (IOException exception)
        {
            System.out.println("Error: Can't setup connection!"
                    + " Terminating client.");
            System.exit(1);
        }




        // Create scanner object to read from console
        fromConsole = new Scanner(System.in);
    }

    public void accept()
    {
        try
        {

            String message;

            while (true)
            {
                message = fromConsole.nextLine();
                server.handleMessageFromServerUI(message);
            }
        }
        catch (Exception ex)
        {
            System.out.println
                    ("Unexpected error while reading from console!");
        }
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message)
    {
        System.out.println(message);
    }

    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of
     * the server instance (there is no UI in this phase).
     *
     * @param args[0] The port number to listen on.  Defaults to 5555
     *          if no argument is entered.
     */
    public static void main(String[] args)
    {
        int port = 0; //Port to listen on

        try
        {
            port = Integer.parseInt(args[0]); //Get port from command line
        }
        catch(Throwable t)
        {
            port = DEFAULT_PORT; //Set port to 5555
        }

        ServerConsole sv = new ServerConsole(port);

        try
        {
            sv.accept();
        }
        catch (Exception ex)
        {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}
