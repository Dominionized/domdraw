package ca.csf.domdraw.client;

import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Socket socket;

    private int port;
    private String address;

    public ClientThread(int port, String address) {
        this.port = port;
        this.address = address;
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(address, port);
            wait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Host not found");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
