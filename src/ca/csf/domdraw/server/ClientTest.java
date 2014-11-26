package ca.csf.domdraw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException{
        String name = "test";
        Socket socket = new Socket("127.0.0.1", 4444);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String readerInput = bufferedReader.readLine();
            printWriter.println(name + ": " + readerInput);
        }
    }
}
