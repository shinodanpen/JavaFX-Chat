package org.shinodanpen.javachat.server;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Main {


    public static Map<String, ClientHandler> ConnectedClients = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        ServerThread server = new ServerThread();
        int port;
        try{
            port = Integer.parseInt(Arrays.toString(args));
        }catch(NumberFormatException e){
            port = 49654;
        }
        server.setup(port);
        server.start();

        ConnectedClients.clear();
    }
}