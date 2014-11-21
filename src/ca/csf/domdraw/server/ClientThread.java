package ca.csf.domdraw.server;

import java.net.Socket;

public class ClientThread extends Thread {
	private Socket socket;
	
	public ClientThread(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		
	}
	
	public String getIPAddress(){
		return "Connected successfully to " + socket.getInetAddress();
	}
}
