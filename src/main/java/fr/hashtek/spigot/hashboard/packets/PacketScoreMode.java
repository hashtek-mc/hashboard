package fr.hashtek.spigot.hashboard.packets;

/**
 * An enum that contains all the available modes for a score.
 *
 * <li>SET - <emphasis>Set a new score.</emphasis></li>
 * <li>REMOVE - <emphasis>Remove a score.</emphasis></li>
 */
public enum PacketScoreMode
{

    SET("CHANGE"),
    REMOVE("REMOVE")

    ;

    private final String mode;

    PacketScoreMode(String mode)
    {
        this.mode = mode;
    }

    /**
     * Get the mode value.
     *
     * @return The mode value.
     */
    public String getMode()
    {
        return mode;
    }

}
