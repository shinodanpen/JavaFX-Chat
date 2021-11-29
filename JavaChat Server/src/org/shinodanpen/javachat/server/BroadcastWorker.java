package org.shinodanpen.javachat.server;

import org.shinodanpen.javachat.common.Packet;
import org.shinodanpen.javachat.common.packets.ClosePacket;
import org.shinodanpen.javachat.common.packets.MessagePacket;
import org.shinodanpen.javachat.common.packets.StartPacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.shinodanpen.javachat.server.Main.ConnectedClients;


public class BroadcastWorker extends Thread {

    public static BlockingQueue<Packet> PacketQueue = new LinkedBlockingQueue<>();
    private boolean running;

    public BroadcastWorker() {
        this.running = true;
    }


    public void sendPacket(Packet packet, Socket sendTo, ClientHandler client) throws IOException {

        ObjectOutputStream out = client.getOut();
        out.flush();
        try {
            switch (packet.getType()){
                case START -> {
                    StartPacket temp = (StartPacket) packet;
                    out.writeObject(temp);
                    System.out.println("Forwarding packet with type: " + temp.getType() + " to " + client.getClientName());
                }

                case MESSAGE -> {
                    MessagePacket temp = (MessagePacket) packet;
                    out.writeObject(temp);
                    System.out.println("Forwarding packet with type: " + temp.getType() + " to " + client.getClientName());
                }

                case CLOSE -> {
                    ClosePacket temp = (ClosePacket) packet;
                    out.writeObject(temp);
                    if(temp.isUsernameRefuse()){
                        System.out.println("Refusing connection from [" + temp.getSenderAddress().getHostAddress() + "], reason: " + temp.getReason());
                    } else{
                        System.out.println("Forwarding packet with type: " + temp.getType() + " to " + client.getClientName());
                    }
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Packet packet;
        while(running){
            try{
                packet = PacketQueue.take();
                Objects.requireNonNull(packet);
                broadcast(packet);
            } catch (IOException | InterruptedException e){
                //TODO Add proper stack trace, maybe even create custom exception
            }


        }
    }

    private void broadcast(Packet packet) throws IOException {


        for (Map.Entry<String, ClientHandler> connectedClient: ConnectedClients.entrySet()) {

            sendPacket(packet, connectedClient.getValue().getClientSocket(), connectedClient.getValue());

        }

    }
}
