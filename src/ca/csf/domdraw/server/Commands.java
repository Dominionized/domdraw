package ca.csf.domdraw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Commands implements Runnable{

    Server server;
    BufferedReader _in;
    String commandStr = "";
    Thread thread;

    public Commands(Server server){
        this.server = server;
        _in = new BufferedReader(new InputStreamReader(System.in));
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try{
            while((commandStr=_in.readLine()) != null){
                if(commandStr.equalsIgnoreCase("quit")){
                    System.exit(0);
                } else if(commandStr.equalsIgnoreCase("total")){
                    System.out.println("Nombre de clients : "+ server.getNbClients());
                    System.out.println("---------");
                } else if(commandStr.equalsIgnoreCase("send")){
                    server.sendAll(_in.readLine(), "\u0000", -1);
                    System.out.println("Message envoyé");
                } else {
                    System.out.println("Commande non-trouvée");
                }
                System.out.flush();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
