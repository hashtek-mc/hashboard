package fr.hashtek.spigot.hashboard.packets;

import fr.hashtek.spigot.hashboard.sidebars.HashSidebar;

import java.lang.reflect.Constructor;

public class PacketObjectiveManager extends PacketManager
{

    private final String sidebarId;
    private final Constructor<?> packetPlayOutScoreboardObjective;
    private final Constructor<?> packetPlayOutScoreboardDisplayObjective;

    /**
     * Create a new packet manager for the objective.
     *
     * @param   hashSidebar     The targeted sidebar.
     * @throws  Exception   If an error has occurred with the NMS package.
     */
    public PacketObjectiveManager(HashSidebar hashSidebar)
            throws Exception
    {
        this.sidebarId = hashSidebar.getId();
        this.packetPlayOutScoreboardObjective = this.getNMSClass("PacketPlayOutScoreboardObjective").getConstructor();
        this.packetPlayOutScoreboardDisplayObjective = this.getNMSClass("PacketPlayOutScoreboardDisplayObjective").getConstructor();
    }

    /**
     * Create a new objective packet to be sent to a player connection.
     * An objective packet can be used to create custom tablist or custom sidebars.
     *
     * @param   mode            The objective mode.
     * @param   displayName     The display name of the objective.
     * @return                  The created (or got) packet of the objective, ready to be sent.
     */
    public Object PacketPlayOutScoreboardObjective(PacketObjectiveMode mode, String displayName)
            throws Exception
    {
        final Object packet = this.packetPlayOutScoreboardObjective.newInstance();

        PacketManager.setField(packet, "a", this.sidebarId);
        PacketManager.setField(packet, "d", mode);

        if (displayName == null)
            displayName = "";

        switch (mode) {
            case CREATE:
            case EDIT:
                PacketManager.setField(packet, "b", displayName);
                PacketManager.setField(packet, "c", 0);
            case DELETE:
                break;
        }
        return packet;
    }

    /**
     * Create a packet for the sidebar's display slot.
     *
     * @return              The created packet of the sidebar's display slot.
     * @throws Exception    If an error has occurred with the NMS package.
     */
    public Object PacketPlayOutScoreboardDisplayObjective()
            throws Exception
    {
        final Object packet = this.packetPlayOutScoreboardDisplayObjective.newInstance();

        PacketManager.setField(packet, "a", 1);
        PacketManager.setField(packet, "b", this.sidebarId);
        return packet;
    }

}
