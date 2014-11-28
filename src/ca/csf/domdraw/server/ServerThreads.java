package ca.csf.domdraw.server;

import java.io.*;
import java.net.Socket;

public class ServerThreads implements Runnable{

    private Thread thread;
    private Socket socket;
    private PrintWriter _out;
    private BufferedReader _in;
    private Server server;
    private int numClient=0;

    public ServerThreads(Socket socket, Server server){
        this.socket = socket;
        this.server = server;

        try{
            _out = new PrintWriter(socket.getOutputStream());
            _in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            numClient = server.addClient(_out);
        } catch(IOException e) {
            e.printStackTrace();
        }

        thread = new Thread(this);
        thread.start();
    }

    public void run()
    {
        String message = "";
        System.out.println("Un nouveau client s'est connecté, no "+numClient);
        server.sendClientNumber(numClient);
        try
        {
            char charCur[] = new char[1];
            while(_in.read(charCur, 0, 1)!=-1){
                if(charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r'){
                    message += charCur[0];
                } else if(!message.equalsIgnoreCase("")){
                    if(charCur[0]=='\u0000') {
                        server.sendAll(message, "" + charCur[0], numClient);
                    } else {
                        server.sendAll(message,"", numClient);
                    }
                        System.out.println(message);
                    message = "";
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
