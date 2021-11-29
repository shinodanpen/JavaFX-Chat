package org.shinodanpen.javachat.server;

import org.shinodanpen.javachat.common.packets.ClosePacket;
import org.shinodanpen.javachat.common.packets.StartPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.shinodanpen.javachat.server.BroadcastWorker.PacketQueue;
import static org.shinodanpen.javachat.server.Main.ConnectedClients;

public class ServerThread extends Thread {
    private ServerSocket server;
    private boolean running;

    public ServerThread() {
        this.running = true;
    }

    public void setup(int port) {
        try {
            //Creation of new Socket dedicated to the server on port 49654
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //infinite loop to accept any request, at any time
        System.out.println("Server started on address: [" + server.getInetAddress().getHostAddress()
        + "] and port " + server.getLocalPort());
        while (running) {
            try {
                //The try-catch is used here, and not on the whole while loop, because otherwise
                // a single exception with one single client, would make the server crash.
                // This way, the only thing crashing would be the thread, which would get abruptly stopped.

                //server receives and accepts an incoming request and dedicates a socket to it
                //also displays client address
                Socket client = server.accept();
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                //create new thread for client
                ClientHandler clientThread = new ClientHandler(client);
                ObjectInputStream in = clientThread.getObjectInputStream();

                StartPacket packet = (StartPacket) in.readObject();

                String clientName = packet.getName();

                System.out.println("Receiving packet with type: " + packet.getType() + " from " + clientName);



                //starts the worker
                BroadcastWorker worker = new BroadcastWorker();
                worker.start();


                if(ConnectedClients.isEmpty()){
                    //puts newly created client socket and thread in the map of currently connected clients
                    clientThread.setClientName(clientName);
                    ConnectedClients.put(clientThread.getClientName(), clientThread);
                    clientThread.start();
                    PacketQueue.put(packet);

                } else if(!(ConnectedClients.containsKey(clientName))){

                    clientThread.setClientName(clientName);
                    ConnectedClients.put(clientThread.getClientName(), clientThread);
                    clientThread.start();
                    PacketQueue.put(packet);

                } else if(ConnectedClients.containsKey(clientName)) {
                    ClosePacket closePacket = new ClosePacket(clientName, "Username already in use. Retry.", client.getInetAddress());
                    closePacket.setUsernameRefuse(true);
                    worker.sendPacket(closePacket, client, clientThread);
                    clientThread.interrupt();
                    client.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
