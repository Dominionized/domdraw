package ca.csf.domdraw.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Vector;

public class Server {

	private static final int DEFAULT_PORT = 4444;
	private static Vector serverThreads = new Vector();
	private int nbrClient = 0;

	public static void main(String[] args) throws IOException {
		Server server = new Server();
		try {
			Integer port;
			port = new Integer(DEFAULT_PORT);

			new Commands(server);

			ServerSocket serverSocket = new ServerSocket(port.intValue());

			printWelcome(port);

			while(true){
				new ServerThreads(serverSocket.accept(), server);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	static private void printWelcome(Integer port)
	{
		System.out.println("DomDraw Server : Par Dominique Bégin et Dominique Septembre");
		try {
			System.out.println("Démarre sur : " + InetAddress.getLocalHost() + ":" + port.toString());
		} catch(UnknownHostException e){
			e.printStackTrace();
		}
		System.out.println("--------");
	}

	synchronized public void sendAll(Object object)
	{
		ObjectOutputStream out;
		for (int i = 0; i < serverThreads.size(); i++)
		{
			out = (ObjectOutputStream) serverThreads.elementAt(i);
			if (out != null){
				try{
					out.writeObject(object);
					System.out.println("Élément distribué à tout les clients");
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	synchronized public void delClient(int i)
	{
		if (serverThreads.elementAt(i) != null)
		{
			serverThreads.removeElementAt(i);
			nbrClient--;
		}
	}

	synchronized public int addClient(ObjectOutputStream out)
	{
		nbrClient++;
		serverThreads.addElement(out);
		return serverThreads.size()-1;
	}

	synchronized public void sendClientNumber(int clientNumber){
		ObjectOutputStream out = (ObjectOutputStream) serverThreads.elementAt(clientNumber);
		try {
			out.write(clientNumber);
		} catch( IOException e ){
			e.printStackTrace();
		}
	}

	synchronized public int getNbClients(){
		return this.nbrClient;
	}
}
