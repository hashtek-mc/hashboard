package fr.hashtek.spigot.hashboard.packets.teams;

public enum PacketTeamTagVisibility
{

    // always, hideForOtherTeams, hideForOwnTeam, never.

    VISIBLE("always"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
    HIDDEN("never")

    ;

    private final String visibility;

    PacketTeamTagVisibility(String visibility)
    {
        this.visibility = visibility;
    }

    public String getVisibility()
    {
        return this.visibility;
    }

}
