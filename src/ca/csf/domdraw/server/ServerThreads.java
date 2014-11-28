package ca.csf.domdraw.server;

import java.io.*;
import java.net.Socket;

public class ServerThreads implements Runnable{

    private Thread thread;
    private Socket socket;
    private ObjectOutputStream _out;
    private ObjectInputStream _in;
    private Server server;
    private int numClient=0;

    public ServerThreads(Socket socket, Server server){
        this.socket = socket;
        this.server = server;

        try{
            _out = new ObjectOutputStream(socket.getOutputStream());
            _in = new ObjectInputStream(socket.getInputStream());
            numClient = server.addClient(_out);
        } catch(IOException e) {
            e.printStackTrace();
        }

        thread = new Thread(this);
        thread.start();
    }

    public void run()
    {
        Object object;
        System.out.println("Un nouveau client s'est connecté, no "+numClient);
        server.sendClientNumber(numClient);
        try
        {
            while(true){
                object = _in.readObject();
                if(object != null) {
                    server.sendAll(object);
                    object = null;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            try
            {
                System.out.println("Le client no "+numClient+" s'est deconnecté");
                server.delClient(numClient);
                socket.close();
            }
            catch (IOException e){ }
        }
    }

    public int getNumClient(){
        return this.numClient;
    }
}
