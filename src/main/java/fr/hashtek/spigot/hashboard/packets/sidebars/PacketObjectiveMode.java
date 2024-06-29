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

    /**
     * Get the mode value.
     *
     * @return The mode value.
     */
    public int getMode()
    {
        return this.mode;
    }

}
