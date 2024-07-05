package fr.hashtek.spigot.hashboard.teams;

import fr.hashtek.hashutils.Reflection;
import fr.hashtek.spigot.hashboard.exceptions.AlreadyInTeamException;
import fr.hashtek.spigot.hashboard.exceptions.NotInTeamException;
import fr.hashtek.spigot.hashboard.exceptions.TeamFullException;
import fr.hashtek.spigot.hashboard.packets.teams.*;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class HashTeam extends Reflection
{

    private final PacketTeamManager packetTeamManager;

    private final ArrayDeque<String> currentPlayers;
    private final ArrayDeque<String> addedPlayersQueue;
    private final ArrayDeque<String> removedPlayersQueue;
    private final HashMap<Player, Boolean> receivers;

    private final ArrayList<PacketTeamFlags> flags = new ArrayList<PacketTeamFlags>();
    private final String id;
    private final int size;

    private TeamColor color;
    private String prefix;
    private String suffix;

    public HashTeam(String id, TeamColor color, int size)
            throws Exception
    {
        this.packetTeamManager = new PacketTeamManager(id);
        this.id = id;
        this.size = size;
        this.currentPlayers = new ArrayDeque<String>();
        this.addedPlayersQueue = new ArrayDeque<String>();
        this.removedPlayersQueue = new ArrayDeque<String>();
        this.receivers = new HashMap<Player, Boolean>();
        this.color = color;
    }

    /**
     * Search for a player in the team.
     * {@code NOTE} - If you call this method before calling the {@link HashTeam#update} method,
     * all the changes (added / removed players) will not be updated yet. To fix this, you can filter
     * the different lists to check.
     *
     * @param player The player to search for.
     * @param checkCurrentPlayers Search for the current players before calling the {@link HashTeam#update} method.
     * @param checkAddedPlayers Search for the added players before calling the {@link HashTeam#update} method.
     * @param checkRemovedPlayers Search for the removed players before calling the {@link HashTeam#update} method.
     * @return {@code true} if the player has been found, {@code false} otherwise.
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
     * {@code NOTE} - Don't forget to call the {@link HashTeam#update} method to take changes into account.
     *
     * @param player The player to add to the team.
     * @throws TeamFullException If the team is full.
     * @throws AlreadyInTeamException If the player is already in the team.
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
    }

    /**
     * Remove a player from the team.
     * {@code NOTE} - Don't forget to call the {@link HashTeam#update} method to take changes into account.
     *
     * @param player The player to add to the team.
     * @throws NotInTeamException If the player is not in the team.
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

    private void create(Player player)
            throws Exception
    {
        final Object packet = this.packetTeamManager.PacketPlayOutScoreboardTeam(
            PacketTeamMode.CREATE_TEAM, PacketTeamTagVisibility.VISIBLE, this.color,
            this.prefix, this.suffix, this.currentPlayers, this.flags
        );

        this.sendPacket(player, packet);
    }

    private void delete(Player player)
            throws Exception
    {
        final Object packet = this.packetTeamManager.PacketPlayOutScoreboardTeam(PacketTeamMode.DELETE_TEAM);

        this.sendPacket(player, packet);
    }

}
