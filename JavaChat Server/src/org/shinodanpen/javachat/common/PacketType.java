package org.shinodanpen.javachat.common;


public enum PacketType {
    START(0),
    CLOSE(1),
    MESSAGE(2);

    private int id;

    PacketType(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }
}
