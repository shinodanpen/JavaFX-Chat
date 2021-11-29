package org.shinodanpen.javachat.common.packets;

import org.shinodanpen.javachat.common.Packet;
import org.shinodanpen.javachat.common.PacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;

public class StartPacket implements Packet, Serializable {
    private String name;
    private LocalDateTime dateTime;
    private InetAddress senderAddress;

    public StartPacket() {}

    public StartPacket(Packet packet) {
        this.name = packet.getName();
        this.dateTime = packet.getDateTime();
        this.senderAddress = packet.getSenderAddress();
    }

    public StartPacket(String name, InetAddress address) {
        this.name = name;
        this.dateTime = LocalDateTime.now();
        this.senderAddress = address;
    }

    @Override
    public void read(ObjectInputStream in) throws IOException, ClassNotFoundException {
        StartPacket temp = (StartPacket) in.readObject();
        this.name = temp.getName();
        this.dateTime = temp.getDateTime();
        this.senderAddress = temp.getSenderAddress();
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeObject(this);
    }

    @Override
    public String getName() {return name;}

    @Override
    public String getMessage() {
        return " has connected.";
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    @Override
    public InetAddress getSenderAddress() {
        return this.senderAddress;
    }

    @Override
    public boolean isUsernameRefuse() {
        return false;
    }

    @Override
    public PacketType getType() {return PacketType.START;}

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "StartPacket{" +
                "name='" + name + '\'' +
                '}';
    }
}
