package fr.hashtek.spigot.hashboard.packets;

public enum PacketObjectiveMode
{

    CREATE(0),
    DELETE(1),
    EDIT(2),
    ;

    private final int mode;

    PacketObjectiveMode(int mode)
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return this.mode;
    }

}