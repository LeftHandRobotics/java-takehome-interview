package com.lhr.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that implements a basic echo server.
 * By default, listed on localhost:8080 (see main()).
 */
public class Server implements AutoCloseable
{
    private final ServerSocket fListener;

    public Server( String host, int port, int backlogConnectionQueueLength ) throws IOException
    {
        fListener = new ServerSocket( port, backlogConnectionQueueLength, InetAddress.getByName( host ) );
        System.out.println( Thread.currentThread() + " Created Server" );
    }

    private void start()
    {
        System.out.println( "Started" );

        try
        {
            while ( true )
            {
                Socket clientSocket = fListener.accept();
                ( new Thread( () -> handleNewClient( clientSocket ) ) ).start();
            }
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
    }

    private void handleNewClient( Socket clientSocket )
    {
        try
        {
            System.out.println( Thread.currentThread() + " Received Connection from " + clientSocket );
            BufferedReader is = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
            PrintStream os = new PrintStream( clientSocket.getOutputStream() );
            // echo that data back to the client, except for QUIT.

            String line;

            while ( ( line = is.readLine() ) != null )
            {
                if ( line.equalsIgnoreCase( "quit" ) )
                {
                    break;
                }
                else
                {
                    os.println( line );
                    os.flush();
                }
            }

            os.println( "Ok" );
            os.flush();
            is.close();
            os.close();
        }
        catch ( IOException ex )
        {
            System.out.println( "Exception while reading from a client: " + ex.getMessage() );
        }

        System.out.println( "Exiting client handler: " + clientSocket );
    }

    @Override
    public void close() throws IOException
    {
        fListener.close();
    }

    public static void main( String[] args )
    {
        try ( Server server = new Server( "localhost", 8080, 50 ) )
        {
            server.start();
        }
        catch ( IOException e )
        {
            System.out.println( "IOException found: " + e.getMessage() );
        }
    }
}