package fr.hashtek.spigot.hashboard.packets;

public enum PacketScoreMode
{

    CHANGE("CHANGE"),
    REMOVE("REMOVE")

    ;

    private final String mode;

    PacketScoreMode(String mode)
    {
        this.mode = mode;
    }

    public String get()
    {
        return mode;
    }

}
