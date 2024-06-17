package fr.hashtek.spigot.hashboard.sidebars;

import fr.hashtek.hashutils.Reflection;
import fr.hashtek.spigot.hashboard.packets.PacketObjectiveManager;
import fr.hashtek.spigot.hashboard.packets.PacketObjectiveMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A sidebar manager.
 */
public class Sidebar extends Reflection
{


    private final String id;
    private final DisplayName displayName;
    private final ArrayList<SidebarLine> lines;
    private final HashMap<Player, Boolean> receivers;

    private final PacketObjectiveManager packetObjectiveManager;

    /**
     * Create a new sidebar.
     *
     * @param id The unique identifier of the sidebar.
     * @throws Exception If an error occurred with the NMS package.
     */
    public Sidebar(String id)
            throws Exception
    {
        this.id = id;
        this.lines = new ArrayList<SidebarLine>();
        this.receivers = new HashMap<Player, Boolean>();
        this.displayName = new DisplayName();
        this.packetObjectiveManager = new PacketObjectiveManager(this);
    }

    /**
     * Add a receiver.
     *
     * @param player    The receiver.
     * @return          The sidebar itself.
     */
    public Sidebar addReceiver(Player player)
    {
        if (this.receivers.containsKey(player))
            return this;

        this.receivers.put(player, false);
        return this;
    }

    /**
     * Remove a receiver.
     *
     * @param player    The receiver.
     * @return          The sidebar itself.
     */
    public Sidebar removeReceiver(Player player)
    {
        this.receivers.remove(player);

        return this;
    }

    /**
     * Send the sidebar with all the modifications to every receiver.
     *
     * @throws Exception If an error occurred in the NMS package.
     */
    public void update()
            throws Exception
    {
        for (Player player : this.receivers.keySet()) {
            if (!this.receivers.get(player)) {
                this.create(player);
                continue;
            }
            this.updateScores(player);
        }
    }

    /**
     * Get the id of the sidebar.
     *
     * @return the id of the sidebar.
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Create the objective's packets to the player.
     *
     * @param   player      The receiver.
     * @throws  Exception   If an error has occurred with the NMS package.
     */
    private void create(Player player)
            throws Exception
    {
        final Object createObjectivePacket = this.packetObjectiveManager.PacketPlayOutScoreboardObjective(PacketObjectiveMode.CREATE, this.displayName.get());
        final Object setDisplaySlotPacket = this.packetObjectiveManager.PacketPlayOutScoreboardDisplayObjective();

        this.sendPacket(player, createObjectivePacket);
        this.sendPacket(player, setDisplaySlotPacket);
        this.receivers.put(player, true);
    }

    /**
     * Create the objective's packets to the player.
     *
     * @param   player      The receiver.
     * @throws  Exception   If an error has occurred with the NMS package.
     */
    private void delete(Player player)
            throws Exception
    {
        final Object deleteObjectivePacket = this.packetObjectiveManager.PacketPlayOutScoreboardObjective(PacketObjectiveMode.DELETE, null);

        this.sendPacket(player, deleteObjectivePacket);
        this.receivers.remove(player);
    }

    /**
     * Send the sidebar's modified scores to a player.
     *
     * @param   player      The receiver.
     * @throws  Exception   If an error has occurred with the NMS package.
     */
    private void updateScores(Player player)
            throws Exception
    {
        final Object packet = null;

        this.sendPacket(player, packet);
    }

    /**
     * Get the player connection.
     *
     * @param player The player.
     * @return The player connection.
     * @throws Exception If an error has occurred with the NMS package.
     */
    private Object getPlayerConnection(Player player)
            throws Exception
    {
        final Object craftPlayer = player.getClass()
            .getMethod("getHandle").invoke(player);

        return craftPlayer.getClass()
            .getField("playerConnection").get(craftPlayer);
    }

}
