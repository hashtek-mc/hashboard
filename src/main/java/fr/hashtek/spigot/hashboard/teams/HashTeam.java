package fr.hashtek.spigot.hashboard.teams;

import fr.hashtek.hashutils.Reflection;
import fr.hashtek.spigot.hashboard.exceptions.AlreadyInTeamException;
import fr.hashtek.spigot.hashboard.exceptions.NotInTeamException;
import fr.hashtek.spigot.hashboard.exceptions.TeamFullException;
import fr.hashtek.spigot.hashboard.packets.teams.PacketTeamManager;
import fr.hashtek.spigot.hashboard.packets.teams.PacketTeamMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A class that represent a team.
 */
public class HashTeam extends Reflection
{

    private final PacketTeamManager packetTeamManager;

    private final HashMap<Player, Boolean> receivers;

    private final TeamData teamData;

    /**
     * Create a new team.
     *
     * @param   id          The unique identifier of the team.
     * @param   color       The color of the team.
     * @param   size        The size of the team. (Set to 0 for a non-sized team)
     * @throws  Exception   If an error occurred with the NMS package.
     */
    public HashTeam(String id, TeamColor color, int size)
            throws Exception
    {
        this.packetTeamManager = new PacketTeamManager(id);
        this.teamData = new TeamData(id, color, size);
        this.receivers = new HashMap<Player, Boolean>();
    }

    /**
     * Search for a player in the team.
     * {@code NOTE} - If you call this method before calling the {@link HashTeam#update} method,
     * all the changes (added / removed players) will not be updated yet. To fix this, you can filter
     * the different lists to check.
     *
     * @param       player                  The player to search for.
     * @param       checkCurrentPlayers     Search for the current players before calling the {@link HashTeam#update} method.
     * @param       checkAddedPlayers       Search for the added players before calling the {@link HashTeam#update} method.
     * @param       checkRemovedPlayers     Search for the removed players before calling the {@link HashTeam#update} method.
     * @return                              {@code true} if the player has been found, {@code false} otherwise.
     */
    public boolean containsPlayer(Player player, boolean checkCurrentPlayers, boolean checkAddedPlayers, boolean checkRemovedPlayers)
    {
        return this.teamData.containsPlayer(player, checkCurrentPlayers, checkAddedPlayers, checkRemovedPlayers);
    }

    /**
     * Add a player to the team.
     * {@code NOTE} - Don't forget to call the {@link HashTeam#update} method to take changes into account.
     *
     * @param       player                  The player to add to the team.
     * @throws      TeamFullException       If the team is full.
     * @throws      AlreadyInTeamException  If the player is already in the team.
     * @return                              The team itself.
     */
    public HashTeam addPlayer(Player player)
            throws TeamFullException, AlreadyInTeamException
    {
        this.teamData.addPlayer(player);
        return this;
    }

    /**
     * Remove a player from the team.
     * {@code NOTE} - Don't forget to call the {@link HashTeam#update} method to take changes into account.
     *
     * @param       player              The player to add to the team.
     * @throws      NotInTeamException  If the player is not in the team.
     * @return                          The team itself.
     */
    public HashTeam removePlayer(Player player)
            throws NotInTeamException
    {
        this.teamData.removePlayer(player);
        return this;
    }

    /**
     * Add a player to the receivers list and send him the team's data using packets. <br/>
     * {@code INFO} - A receiver is a player that will receive the team data
     * when the {@link HashTeam#update} method is called.
     *
     * @param   player      The player to add to the receivers list.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    public void addReceiver(Player player)
            throws Exception
    {
        this.receivers.put(player, false);
        this.create(player);
    }


    /**
     * Remove a player from the receivers list and remove the team's data using packets. <br/>
     * {@code INFO} - A receiver is a player that will receive the team data
     * when the {@link HashTeam#update} method is called.
     *
     * @param   player      The player to remove from the receivers list.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    public void removeReceiver(Player player)
            throws Exception
    {
        this.receivers.remove(player);
        this.delete(player);
    }

    /**
     * Send the packets to update the team.
     *
     * @throws  Exception    If an error occurred with the NMS package.
     */
    public void update()
            throws Exception
    {
        for (Player player : this.receivers.keySet()) {
            if (!this.receivers.get(player)) {
                this.create(player);
                continue;
            }
            this.update(player);
        }
        this.teamData.validateChanges();
    }

    /**
     * Send the packets to update the team's data to a specific player.
     *
     * @param   player      The player to send the packets.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    private void update(Player player)
            throws Exception
    {
        ArrayList<String> addedPlayers = this.teamData.getAddedPlayers();
        ArrayList<String> removedPlayers = this.teamData.getRemovedPlayers();

        if (!addedPlayers.isEmpty())
            this.updateAddedPlayers(player);
        if (!removedPlayers.isEmpty())
            this.updateRemovedPlayers(player);
        if (this.teamData.checkIfHasChanged())
            this.updateTeamInformation(player);
    }

    /**
     * Send the packets to a player to update the added players to the team.
     *
     * @param   player      The player to send the packets.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    private void updateAddedPlayers(Player player)
            throws Exception
    {
        final Object packet = this.packetTeamManager.PacketPlayOutScoreboardTeam(PacketTeamMode.ADD_PLAYER, this.teamData.getAddedPlayers());

        this.sendPacket(player, packet);
    }

    /**
     * Send the packets to a player to update the removed players from the team.
     *
     * @param   player      The player to send the packets.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    private void updateRemovedPlayers(Player player)
            throws Exception
    {
        final Object packet = this.packetTeamManager.PacketPlayOutScoreboardTeam(PacketTeamMode.ADD_PLAYER, this.teamData.getRemovedPlayers());

        this.sendPacket(player, packet);

    }

    /**
     * Send the packets to a specific player to update the team's data.
     *
     * @param   player      The player to send the packets.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    private void updateTeamInformation(Player player)
            throws Exception
    {
        final TeamTagVisibility visibility = this.teamData.getTagVisibility();
        final TeamColor color = this.teamData.getColor();
        final String prefix = this.teamData.getPrefix();
        final String suffix = this.teamData.getSuffix();
        final byte flags = this.teamData.getFlags();

        final Object packet = this.packetTeamManager.PacketPlayOutScoreboardTeam(
            PacketTeamMode.UPDATE_TEAM_INFO, visibility, color,
            prefix, suffix, Collections.emptyList(), flags
        );

        this.sendPacket(player, packet);
    }

    /**
     * Send the packets to a specific player to create the team.
     *
     * @param   player      The player to send the packets.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    private void create(Player player)
            throws Exception
    {
        final TeamTagVisibility visibility = this.teamData.getTagVisibility();
        final TeamColor color = this.teamData.getColor();
        final String prefix = this.teamData.getPrefix();
        final String suffix = this.teamData.getSuffix();
        final ArrayList<String> currentPlayers = this.teamData.getCurrentPlayers();
        final byte flags = this.teamData.getFlags();

        final Object packet = this.packetTeamManager.PacketPlayOutScoreboardTeam(
                PacketTeamMode.CREATE_TEAM, visibility, color,
                prefix, suffix, currentPlayers, flags
        );

        this.sendPacket(player, packet);
    }

    /**
     * Send the packets to a specific player to delete the team.
     *
     * @param   player      The player to send the packets.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    private void delete(Player player)
            throws Exception
    {
        final Object packet = this.packetTeamManager.PacketPlayOutScoreboardTeam(PacketTeamMode.DELETE_TEAM);

        this.sendPacket(player, packet);
    }

}
