package ca.csf.domdraw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {
	private Socket socket;
	
	public ClientThread(Socket socket){
		this.socket = socket;
	}

	public void run(){
		try {
			String message = null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while((message = bufferedReader.readLine()) != null){
				System.out.println("Message entrant du client"+ message);
			}

			socket.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public String getIPAddress(){
		return "Connected successfully to " + socket.getInetAddress();
	}
}
