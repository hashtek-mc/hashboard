package fr.hashtek.spigot.hashboard.packets.teams;

public enum PacketTeamFlags
{

    ALLOW_FRIENDLY_FIRE((byte) 0x01),
    CAN_SEE_INVISIBLE_PLAYER_ON_SAME_TEAM((byte) 0x02)

    ;

    private final byte flag;

    PacketTeamFlags(byte flag)
    {
        this.flag = flag;
    }

    public byte getFlag()
    {
        return this.flag;
    }

}
