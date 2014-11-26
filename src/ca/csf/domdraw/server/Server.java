package ca.csf.domdraw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {

	private static List<ClientThread> clientThreads;
	private static final int DEFAULT_PORT = 4444;

	public static void main(String[] args) throws IOException {
		new Server().runServer();
	}

	public void runServer() throws IOException{
		ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
		System.out.println("Server is running & waiting for connection !");

		while(true){
			Socket socket = serverSocket.accept();
			new ClientThread(socket).start();
		}
	}
}
