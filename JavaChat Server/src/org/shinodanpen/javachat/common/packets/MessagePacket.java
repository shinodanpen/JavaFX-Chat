package org.shinodanpen.javachat.common.packets;

import org.shinodanpen.javachat.common.Packet;
import org.shinodanpen.javachat.common.PacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;

public class MessagePacket implements Packet, Serializable {
    private String name;
    private String message;
    private LocalDateTime dateTime;
    private InetAddress senderAddress;

    public MessagePacket() {
    }

    public MessagePacket(Packet packet) {
        this.name = packet.getName();
        this.message = packet.getMessage();
        this.dateTime = packet.getDateTime();
        this.senderAddress = packet.getSenderAddress();
    }

    public MessagePacket(String name, String message, InetAddress address) {
        this.name = name;
        this.message = message;
        this.dateTime = LocalDateTime.now();
        this.senderAddress = address;
    }

    @Override
    public void read(ObjectInputStream in) throws IOException, ClassNotFoundException {
        MessagePacket temp = (MessagePacket) in.readObject();
        this.name = temp.getName();
        this.message = temp.getMessage();
        this.dateTime = temp.getDateTime();
        this.senderAddress = temp.getSenderAddress();
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeObject(this);
    }

    public String getMessage() {
        return " >> " + this.message;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    @Override
    public String getName() {return name;}

    @Override
    public PacketType getType() {return PacketType.MESSAGE;}

    @Override
    public InetAddress getSenderAddress() {
        return this.senderAddress;
    }

    @Override
    public boolean isUsernameRefuse() {
        return false;
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String toString() {
        return "MessagePacket{" +
                "message='" + message + '\'' +
                '}';
    }
}
