package org.shinodanpen.javachat.client;

import org.shinodanpen.javachat.common.Packet;
import org.shinodanpen.javachat.common.PacketType;
import org.shinodanpen.javachat.common.packets.ClosePacket;
import org.shinodanpen.javachat.common.packets.MessagePacket;
import org.shinodanpen.javachat.common.packets.StartPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread extends Thread {

    private Socket client;
    private InetAddress address;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientName;
    private ClientController controller;

    public ClientThread(ClientController c){
        this.controller = c;
    }

    public void connect( String address, int port) throws IOException {
        if(address.isEmpty()){
            this.client = new Socket(InetAddress.getLocalHost(), port);
        }
        else{
            this.client = new Socket(InetAddress.getByName(address), port);
        }

        this.address = client.getInetAddress();

        System.out.println("Client started and connected");
        this.out = new ObjectOutputStream(client.getOutputStream());
        out.flush();
        this.in  = new ObjectInputStream(client.getInputStream());
        out.flush();
    }

    public void sendPacket(PacketType type, Packet packet) {
        try {
            System.out.println("Sending packet with type: " + packet.getType() + " as " + packet.getName());
            switch (type){
                case START -> {
                    StartPacket temp = (StartPacket) packet;
                    out.writeObject(temp);
                }

                case MESSAGE -> {
                    MessagePacket temp = (MessagePacket) packet;
                    out.writeObject(temp);
                }

                case CLOSE -> {
                    ClosePacket temp = (ClosePacket) packet;
                    out.writeObject(temp);
                    client.close();
                    this.interrupt();
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        sendPacket(PacketType.MESSAGE, new MessagePacket(clientName, message, client.getInetAddress()));
    }

    public void closeConnection(String reason) throws IOException {
        sendPacket(PacketType.CLOSE, new ClosePacket(clientName, reason, client.getInetAddress()));
    }

    public void startConnection(String name, int port, String address) throws IOException {
        connect(address, port);
        this.clientName = name;
        sendPacket(PacketType.START, new StartPacket(name, client.getInetAddress()));
    }

    // STARTS LISTENING FROM SERVER
    @Override
    public void run() {
        boolean open = true;
        while (open) {
            try {
                Packet packet = (Packet) in.readObject();

                    controller.addMessageToList(packet);

            } catch (IOException | ClassNotFoundException e) {
                open = false;
            }
        }
    }

    public Socket getClientSocket() {
        return client;
    }

    public InetAddress getAddress() {
        return address;
    }
}
