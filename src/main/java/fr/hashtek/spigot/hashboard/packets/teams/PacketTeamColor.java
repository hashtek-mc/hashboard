package fr.hashtek.spigot.hashboard.packets.teams;

public enum PacketTeamColor
{

    BLACK(0),
    DARK_BLUE(1),
    DARK_GREEN(2),
    DARK_RED(3),
    DARK_CYAN(4),
    PURPLE(5),
    GOLD(6),
    GRAY(7),
    DARK_GRAY(8),
    BLUE(9),
    GREEN(10),
    CYAN(11),
    RED(12),
    PINK(13),
    YELLOW(14),
    WHITE(15),
    OBFUSCATED(16),
    BOLD(17),
    STRIKETHROUGH(18),
    UNDERLINE(19),
    ITALIC(20),
    RESET(21),

    ;

    private final int code;

    PacketTeamColor(int code)
    {
        this.code = code;
    }

    /**
     * Get the team color code.
     *
     * @return the team color code.
     */
    public int getCode()
    {
        return this.code;
    }

}
