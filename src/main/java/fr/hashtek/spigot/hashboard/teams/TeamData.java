package fr.hashtek.spigot.hashboard.teams;

import fr.hashtek.spigot.hashboard.exceptions.AlreadyInTeamException;
import fr.hashtek.spigot.hashboard.exceptions.NotInTeamException;
import fr.hashtek.spigot.hashboard.exceptions.TeamFullException;
import fr.hashtek.spigot.hashboard.packets.teams.PacketTeamFlags;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * A class that represent the data of a team.
 */
public class TeamData
{

    private final ArrayDeque<String> currentPlayers;
    private final ArrayDeque<String> addedPlayersQueue;
    private final ArrayDeque<String> removedPlayersQueue;

    private final String id;
    private final int size;

    private TeamTagVisibility tagVisibility;
    private TeamColor color;
    private String prefix;
    private String suffix;
    private byte flags;

    private boolean hasChanged;

    /**
     * Instantiate a new TeamData class.
     *
     * @param   teamId      The unique id of the team.
     * @param   teamColor   The color of the team.
     * @param   teamSize    The size of the team. (0 for a not-sized team)
     */
    public TeamData(String teamId, TeamColor teamColor, int teamSize)
    {
        this.currentPlayers = new ArrayDeque<String>();
        this.addedPlayersQueue = new ArrayDeque<String>();
        this.removedPlayersQueue = new ArrayDeque<String>();
        this.flags = 0;

        this.id = teamId;
        this.size = teamSize;
        this.color = teamColor;
    }

    /**
     * Search for a player in the team.
     * {@code NOTE} - If you call this method before calling the {@link TeamData#validateChanges} method,
     * all the changes (added / removed players) will not be updated yet. This method search in the players that are
     * currently in the team AND the players that are going to be added in the team. Use {@link TeamData#containsPlayer(Player, boolean, boolean, boolean)}
     * for a fully-custom search.
     *
     * @param   player      The player to search for.
     * @return              {@code true} if the player has been found, {@code false} otherwise.
     */
    public boolean containsPlayer(Player player)
    {
        final String playerName = player.getName();
        final ArrayList<String> playersToCheck = new ArrayList<>();

        playersToCheck.addAll(this.addedPlayersQueue);
        playersToCheck.addAll(this.currentPlayers);

        return playersToCheck.contains(playerName);
    }

    /**
     * Search for a player in the team.
     * {@code NOTE} - If you call this method before calling the {@link TeamData#validateChanges} method,
     * all the changes (added / removed players) will not be updated yet. To fix this, you can filter
     * the different lists to check.
     *
     * @param       player                  The player to search for.
     * @param       checkCurrentPlayers     Search in the current players list (useful only if you haven't called the {@link TeamData#validateChanges} method yet).
     * @param       checkAddedPlayers       Search in the added players list (useful only if you haven't called the {@link TeamData#validateChanges} method yet).
     * @param       checkRemovedPlayers     Search in the removed players list (useful only if you haven't called the {@link TeamData#validateChanges} method yet).
     * @return                              {@code true} if the player has been found, {@code false} otherwise.
     */
    public boolean containsPlayer(Player player, boolean checkCurrentPlayers, boolean checkAddedPlayers, boolean checkRemovedPlayers)
    {
        final String playerName = player.getName();
        final ArrayList<String> playersToCheck = new ArrayList<>();

        if (checkAddedPlayers)
            playersToCheck.addAll(this.addedPlayersQueue);
        if (checkRemovedPlayers)
            playersToCheck.addAll(this.removedPlayersQueue);
        if (checkCurrentPlayers)
            playersToCheck.addAll(this.currentPlayers);

        return playersToCheck.contains(playerName);
    }

    /**
     * Add a player to the team.
     * {@code NOTE} - Don't forget to call the {@link TeamData#validateChanges} method to take changes into account.
     *
     * @param       player                  The player to add to the team.
     * @throws      TeamFullException       If the team is full.
     * @throws      AlreadyInTeamException  If the player is already in the team.
     */
    public void addPlayer(Player player)
            throws TeamFullException, AlreadyInTeamException
    {
        final String playerUsername = player.getName();

        if (this.addedPlayersQueue.contains(playerUsername))
            return;

        if (this.size != 0 && this.currentPlayers.size() >= this.size)
            throw new TeamFullException("The team \"" + this.id + "\" is full.");
        if (this.currentPlayers.contains(playerUsername))
            throw new AlreadyInTeamException("The player \"" + playerUsername + "\" is already in the team \"" + this.id + "\".");

        if (!this.removedPlayersQueue.contains(playerUsername))
            this.addedPlayersQueue.add(playerUsername);
        else
            this.removedPlayersQueue.remove(playerUsername);

        return;
    }

    /**
     * Remove a player from the team.
     * {@code NOTE} - Don't forget to call the {@link TeamData#validateChanges} method to take changes into account.
     *
     * @param       player              The player to add to the team.
     * @throws      NotInTeamException  If the player is not in the team.
     */
    public void removePlayer(Player player)
            throws NotInTeamException
    {
        final String playerUsername = player.getName();

        if (this.removedPlayersQueue.contains(playerUsername))
            return;

        if (!this.currentPlayers.contains(playerUsername))
            throw new NotInTeamException("The player \"" + playerUsername + "\" is not in the team \"" + this.id + "\".");

        if (!this.addedPlayersQueue.contains(playerUsername))
            this.removedPlayersQueue.add(playerUsername);
        else
            this.addedPlayersQueue.remove(playerUsername);
    }

    /**
     * Enable a flag.
     *
     * @param   flag    The flag to enable.
     */
    public void enableFlag(PacketTeamFlags flag)
    {
        this.flags |= flag.getFlag();
    }

    /**
     * Disable a flag.
     *
     * @param   flag   The flag to enable.
     */
    public void disableFlag(PacketTeamFlags flag)
    {
        this.flags &= (byte) ~flag.getFlag();
    }

    /**
     * Set the tag visibility of the team.
     *
     * @param   tagVisibility   The tag visibility of the team.
     */
    public void setTagVisibility(TeamTagVisibility tagVisibility)
    {
        this.tagVisibility = tagVisibility;
        this.hasChanged = true;
    }

    /**
     * Set the color of the team.
     *
     * @param   color   The color of the team.
     */
    public void setColor(TeamColor color)
    {
        this.color = color;
        this.hasChanged = true;
    }

    /**
     * Set the prefix of the team.
     *
     * @param   prefix  The prefix of the team.
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
        this.hasChanged = true;
    }

    /**
     * Set the suffix of the team.
     *
     * @param   suffix  The suffix of the team.
     */
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
        this.hasChanged = true;
    }

    /**
     * Get a copy of the added players list.
     *
     * @return A copy of the added players list.
     */
    public ArrayList<String> getAddedPlayers()
    {
        return new ArrayList<String>(this.addedPlayersQueue);
    }

    /**
     * Get a copy of the removed players list.
     *
     * @return A copy of the removed players list.
     */
    public ArrayList<String> getRemovedPlayers()
    {
        return new ArrayList<String>(this.removedPlayersQueue);
    }

    /**
     * Get a copy of the current players list.
     *
     * @return A copy of the current players list.
     */
    public ArrayList<String> getCurrentPlayers()
    {
        return new ArrayList<String>(this.currentPlayers);
    }

    /**
     * Get the flags as bit-mask.
     *
     * @return The flags as bit-mask.
     */
    public byte getFlags()
    {
        return this.flags;
    }

    /**
     * Get the team tag visibility.
     *
     * @return The team tag visibility.
     */
    public TeamTagVisibility getTagVisibility()
    {
        return this.tagVisibility;
    }

    /**
     * Get the team color.
     *
     * @return The team color.
     */
    public TeamColor getColor()
    {
        return this.color;
    }

    /**
     * Get the prefix of the team.
     *
     * @return The prefix of the team.
     */
    public String getPrefix()
    {
        return this.prefix;
    }

    /**
     * Get the suffix of the team.
     *
     * @return The suffix of the team.
     */
    public String getSuffix()
    {
        return this.suffix;
    }

    /**
     * Check if a data has changed.
     *
     * @return {@code true} if at least one data has changed, {@code false} otherwise.
     */
    public boolean checkIfHasChanged()
    {
        return this.hasChanged;
    }

    /**
     * Validate all changes.
     */
    public void validateChanges()
    {
        this.currentPlayers.addAll(this.addedPlayersQueue);
        this.currentPlayers.removeAll(this.removedPlayersQueue);
        this.hasChanged = false;
    }

}
