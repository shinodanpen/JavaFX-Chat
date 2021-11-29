package org.shinodanpen.javachat.server;

import org.shinodanpen.javachat.common.Packet;
import org.shinodanpen.javachat.common.PacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.shinodanpen.javachat.server.BroadcastWorker.PacketQueue;
import static org.shinodanpen.javachat.server.Main.ConnectedClients;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private String clientName;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        out.flush();
        in  = new ObjectInputStream(clientSocket.getInputStream());
    }



    @Override
    public void run() {
        boolean open = true;
        while(open) {
            try {
                out.flush();
                Packet packet = (Packet) in.readObject();
                if(packet.getType() == PacketType.CLOSE){
                    ConnectedClients.remove(clientName, this);
                    PacketQueue.put(packet);
                    clientSocket.close();
                    this.interrupt();
                }
                /*else if(packet.getType() == PacketType.START){

                    this.clientName = packet.getName();
                    System.out.println(clientName);
                }*/

                System.out.println("Receiving packet with type: " + packet.getType() + " from " + clientName);
                PacketQueue.put(packet);

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                //TODO Add proper stack trace, maybe even create custom exception


                open = false;
            }
        }
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String name){
        this.clientName = name;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public Socket getClientSocket() {return clientSocket;}

    public ObjectInputStream getObjectInputStream() {
        return in;
    }
}
