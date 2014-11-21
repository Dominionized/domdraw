package ca.csf.domdraw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class Server {

	private static ServerSocket serverSocket;
	private static List<ClientThread> clientThreads;
	private static int port;
	private static final int DEFAULT_PORT = 4444;
	private static final int DEFAULT_L33T_LEVEL = 30;

	public void main(String[] args) {
		if (args[0] == null) {
			this.port = DEFAULT_PORT;
		} else {
			this.port = Integer.parseInt(args[0]);
		}

		try {
			serverSocket = new ServerSocket(port);

			while (true) {
				try {
					System.out.println("Waiting for a connection...");

					clientThreads.add(new ClientThread(serverSocket.accept()));
					for (ClientThread client : clientThreads) {
						client.getIPAddress();
					}

				} catch (SocketException e) {
					System.out.println("Client ended the connection");
				} /*catch (ClassNotFoundException e) {
					e.printStackTrace();
				}*/
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
