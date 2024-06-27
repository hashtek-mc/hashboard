package fr.hashtek.spigot.hashboard.packets;

/**
 * An enum that contains all the available modes for an objective.
 */
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