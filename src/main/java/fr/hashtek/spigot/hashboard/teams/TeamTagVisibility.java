package fr.hashtek.spigot.hashboard.teams;

/**
 * An enum that contains all the possible tag visibility.
 */
public enum TeamTagVisibility
{

    VISIBLE("always"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
    HIDDEN("never")

    ;

    private final String visibility;

    /**
     * Instantiate a new TeamTagVisibility.
     *
     * @param visibility The corresponding string of the tag visibility.
     */
    TeamTagVisibility(String visibility)
    {
        this.visibility = visibility;
    }

    /**
     * Get the corresponding string of the tag visibility.
     *
     * @return the corresponding string of the tag visibility.
     */
    public String getVisibility()
    {
        return this.visibility;
    }

}
