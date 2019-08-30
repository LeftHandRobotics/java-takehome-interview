package com.lhr.chatroom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class that implements a basic echo client.
 * By default, looks for a server at localhost:8080 (see main()).
 */
public class Client implements AutoCloseable
{
    private final DataOutputStream fToServer;
    private final BufferedReader fFromServer;

    public Client( String host, int port ) throws IOException
    {
        Socket serverConnection = new Socket( host, port );
        fToServer = new DataOutputStream( serverConnection.getOutputStream() );
        fFromServer = new BufferedReader( new InputStreamReader( serverConnection.getInputStream() ) );

        Thread receiveThread = new Thread( this::receive );
        receiveThread.setDaemon( true );
        receiveThread.start();
    }

    /**
     * Loop on input from the console and send it to the server.
     */
    private void send()
    {
        try
        {
            System.out.println( "Connected" );

            String msg;
            do
            {
                System.out.print( "> " );
                msg = System.console().readLine();
                fToServer.writeBytes( msg + "\n" );
                fToServer.flush();
            }
            while ( !msg.equalsIgnoreCase( "quit" ) );
        }
        catch ( UnknownHostException e )
        {
            System.err.println( "Don't know about host: hostname" );
        }
        catch ( IOException e )
        {
            System.out.println( "IO Exception: " + e.getMessage() );
        }
    }

    /**
     * Spin on the input stream, read off messages, and print them to the console.
     */
    private void receive()
    {
        try
        {
            while ( true )
            {
                String responseLine = fFromServer.readLine();

                if ( responseLine != null && responseLine.equals( "Ok" ) )
                {
                    return;
                }

                System.out.println( " << " + responseLine );
            }
        }
        catch ( UnknownHostException e )
        {
            System.err.println( "Don't know about host: " + e.getMessage() );
        }
        catch ( IOException e )
        {
            System.out.println( "IO Exception: " + e.getMessage() );
        }
    }

    @Override
    public void close() throws IOException
    {
        fFromServer.close();
        fToServer.close();
    }

    public static void main( String[] args )
    {
        final int port = 8080;
        final String host = "localhost";
        System.out.println( "Connecting to server" );

        try ( Client client = new Client( host, port ) )
        {
            client.send();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        System.out.println( "Done" );
    }
}