package org.shinodanpen.javachat.common.packets;

import org.shinodanpen.javachat.common.Packet;
import org.shinodanpen.javachat.common.PacketType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;

public class ClosePacket implements Packet, Serializable {
    private String name;
    private String message;
    private LocalDateTime dateTime;
    private boolean usernameRefuse;
    private InetAddress senderAddress;

    public ClosePacket() {
    }

    public ClosePacket(Packet packet) {
        this.name = packet.getName();
        this.message = packet.getMessage();
        this.dateTime = packet.getDateTime();
        this.senderAddress = packet.getSenderAddress();
        this.usernameRefuse = false;
    }

    public ClosePacket(String name, String reason, InetAddress address) {
        this.name = name;
        this.message = reason;
        this.dateTime = LocalDateTime.now();
        this.senderAddress = address;
        this.usernameRefuse = false;
    }

    @Override
    public void read(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ClosePacket temp = (ClosePacket) in.readObject();
        this.name = temp.getName();
        this.message = temp.getReason();
        this.dateTime = temp.getDateTime();
        this.senderAddress = temp.getSenderAddress();
        this.usernameRefuse = false;
    }

    @Override
    public void write(ObjectOutputStream out) throws IOException {
        out.writeObject(this);
    }

    @Override
    public String getName() {return name;}

    @Override
    public String getMessage() {
        return " has disconnected. Reason: " + message;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public String getReason() {return message;}

    @Override
    public PacketType getType() {return PacketType.CLOSE;}

    @Override
    public InetAddress getSenderAddress() {
        return this.senderAddress;
    }

    @Override
    public int getId() {
        return 1;
    }

    public boolean isUsernameRefuse() {
        return usernameRefuse;
    }

    public void setUsernameRefuse(boolean usernameRefuse) {
        this.usernameRefuse = usernameRefuse;
    }

    @Override
    public String toString() {
        return "StartPacket{" +
                "name='" + message + '\'' +
                '}';
    }
}
