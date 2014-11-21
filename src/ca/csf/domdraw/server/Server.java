package ca.csf.domdraw.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static ObjectInputStream input;
	private static ObjectOutputStream output;
	private static int port;
	private static final int DEFAULT_PORT = 4444;
	private static final int DEFAULT_L33T_LEVEL = 30;
	
	
    public void main(String[] args){
        System.out.println("Hello world");
    }
}
