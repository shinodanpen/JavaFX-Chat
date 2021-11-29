package org.shinodanpen.javachat.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.time.LocalDateTime;

public interface Packet {


    PacketType getType();
    int getId();
    String getName();
    String getMessage();
    LocalDateTime getDateTime();
    InetAddress getSenderAddress();
    boolean isUsernameRefuse();

    /**
     * Reads the data from the input and populates the packet
     * @param in
     */
    void read(ObjectInputStream in) throws IOException, ClassNotFoundException;

    /**
     * Writes the data in the output
     * @param out
     */
    void write(ObjectOutputStream out) throws IOException;

}
