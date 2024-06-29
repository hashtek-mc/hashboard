package fr.hashtek.spigot.hashboard.sidebars;

import fr.hashtek.hashutils.Reflection;
import fr.hashtek.spigot.hashboard.packets.sidebars.PacketObjectiveManager;
import fr.hashtek.spigot.hashboard.packets.sidebars.PacketObjectiveMode;
import fr.hashtek.spigot.hashboard.packets.sidebars.PacketScoreManager;
import fr.hashtek.spigot.hashboard.packets.sidebars.PacketScoreMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * A class that manage all the data of a sidebar, from its title to its lines, using packets.
 */
public class HashSidebar extends Reflection
{

    private final String id;
    private final SidebarTitle title;
    private final HashMap<Integer, SidebarLine> lines;
    private final HashMap<Player, Boolean> receivers;

    private final PacketObjectiveManager packetObjectiveManager;
    private final PacketScoreManager packetScoreManager;

    /**
     * Create a new sidebar.
     *
     * @param   id          The unique identifier of the sidebar.
     * @throws  Exception   If an error occurred with the NMS package.
     */
    public HashSidebar(String id)
            throws Exception
    {
        this.id = id;
        this.lines = new HashMap<Integer, SidebarLine>();
        this.receivers = new HashMap<Player, Boolean>();
        this.title = new SidebarTitle();
        this.packetObjectiveManager = new PacketObjectiveManager(this);
        this.packetScoreManager = new PacketScoreManager(this);
    }

    /**
     * Add a receiver.
     *
     * @param player    The receiver.
     * @return          The sidebar itself.
     */
    public HashSidebar addReceiver(Player player)
    {
        if (this.receivers.containsKey(player))
            return this;

        this.receivers.put(player, false);
        return this;
    }

    /**
     * Remove a receiver.
     * WARNING - Removing a receiver will not remove the sidebar to the player, except if .
     *
     * @param   player      The receiver.
     * @return              The sidebar itself.
     */
    public HashSidebar removeReceiver(Player player)
    {
        this.receivers.remove(player);

        return this;
    }

    /**
     * Set a new sidebar title.<br>
     * WARNING - The changes will take effect after calling the {@link HashSidebar#update()} method.
     *
     * @param   title   The new title.
     * @return          The sidebar itself.
     */
    public HashSidebar setTitle(String title)
    {
        this.title.set(title);
        return this;
    }

    /**
     * Set a new line.
     * WARNING - The changes will take effect after calling the {@link HashSidebar#update()} method.
     *
     * @param   index   The index of the line.
     * @param   value   The value of the line.
     * @return          The sidebar itself.
     */
    public HashSidebar setLine(int index, String value)
    {
        final SidebarLine line = new SidebarLine(index, value);

        this.lines.put(line.getIndex(), line);
        return this;
    }

    /**
     * Set multiple lines at one time.
     * WARNING - The changes will take effect after calling the {@link HashSidebar#update()} method.
     *
     * @param   value       The value of the line.
     * @param   indexes     The list of indexes.
     * @return              The sidebar itself.
     */
    public HashSidebar setLines(String value, int... indexes)
    {
        for (int index : indexes)
            this.setLine(index, value);
        return this;
    }

    /**
     * Fill multiple lines of the same value.
     * WARNING - The changes will take effect after calling the {@link HashSidebar#update()} method.
     *
     * @param   value       The value of the line.
     * @param   from        The first index (included).
     * @param   to          The last index (included).
     * @return              The sidebar itself.
     */
    public HashSidebar setLines(String value, int from, int to)
    {
        for (int index = from; index <= to; index++)
            this.setLine(index, value);
        return this;
    }

    /**
     * Remove a line.
     * WARNING - The changes will take effect after calling the {@link HashSidebar#update()} method.
     *
     * @param   index   The index of the line.
     * @return          The sidebar itself.
     */
    public HashSidebar clearLine(int index)
    {
        SidebarLine line = this.lines.get(index);

        if (line != null)
            line.delete();
        return this;
    }

    public HashSidebar clearLines()
    {
        for (SidebarLine line : this.lines.values())
            this.clearLine(line.getIndex());
        return this;
    }

    public HashSidebar clearLines(int from, int to)
    {
        for (int index = from; index <= to; index++)
            this.clearLine(index);
        return this;
    }

    public HashSidebar clearLines(int... indexes)
    {
        for (int index : indexes)
            this.clearLine(index);
        return this;
    }

    /**
     * Send the sidebar with all the modifications to every receiver.
     *
     * @throws Exception If an error has occurred in the NMS package.
     */
    public void update()
            throws Exception
    {
        for (Player player : this.receivers.keySet()) {
            if (!this.receivers.get(player)) {
                this.create(player);
                continue;
            }
            this.updateScores(player, false);
        }
        this.validateScores();
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
     * Display the sidebar packets to the player.
     *
     * @param   player      The receiver.
     * @throws  Exception   If an error has occurred with the NMS package.
     */
    private void create(Player player)
            throws Exception
    {
        final Object createObjectivePacket = this.packetObjectiveManager.PacketPlayOutScoreboardObjective(PacketObjectiveMode.CREATE, this.title.get());
        final Object setDisplaySlotPacket = this.packetObjectiveManager.PacketPlayOutScoreboardDisplayObjective();

        this.sendPacket(player, createObjectivePacket);
        this.sendPacket(player, setDisplaySlotPacket);
        this.updateScores(player, true);
        this.receivers.put(player, true);
    }

    /**
     * Remove the sidebar to a player.
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
     * @param   player          The receiver.
     * @param   forceUpdate     {@code true} to update all the scores, {@code false} otherwise.
     * @throws  Exception       If an error has occurred with the NMS package.
     */
    private void updateScores(Player player, boolean forceUpdate)
            throws Exception
    {
        Object removeScorePacket = null;
        Object setScorePacket = null;
        String previousValue = null;
        String newValue = null;
        int index = 0;

        for (SidebarLine line : this.lines.values()) {
            if (!forceUpdate && (!line.checkIfHasChanged() || !line.checkIfDeleted()))
                continue;

            index = line.getIndex();
            previousValue = line.getPreviousValue();
            newValue = line.getValue();

            removeScorePacket = this.packetScoreManager.PacketPlayOutScoreboardScore(PacketScoreMode.REMOVE, index, previousValue);
            setScorePacket = this.packetScoreManager.PacketPlayOutScoreboardScore(PacketScoreMode.SET, index, newValue);

            this.sendPacket(player, removeScorePacket);
            if (!line.checkIfDeleted())
                this.sendPacket(player, setScorePacket);
        }
    }

    /**
     * Validate the modified lines.
     */
    private void validateScores()
    {
        for (SidebarLine line : this.lines.values()) {
            if (line.checkIfHasChanged())
                line.validateChanges();
        }
    }

}
