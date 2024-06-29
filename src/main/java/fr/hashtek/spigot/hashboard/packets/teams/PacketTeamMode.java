package fr.hashtek.spigot.hashboard.packets.teams;

public enum PacketTeamMode
{

    CREATE_TEAM(0),
    UPDATE_TEAM_INFO(2),
    DELETE_TEAM(1),
    ADD_PLAYER(3),
    REMOVE_PLAYER(4),

    ;

    private final int mode;

    PacketTeamMode(int mode)
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return this.mode;
    }

}
