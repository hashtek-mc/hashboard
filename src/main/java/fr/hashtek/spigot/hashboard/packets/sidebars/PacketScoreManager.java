package fr.hashtek.spigot.hashboard.packets.sidebars;

import fr.hashtek.spigot.hashboard.packets.PacketManager;
import fr.hashtek.spigot.hashboard.sidebars.HashSidebar;

import java.lang.reflect.Constructor;

/**
 * A class to manage the scores of a sidebar.
 */
public class PacketScoreManager extends PacketManager
{

    private final String sidebarId;
    private final Constructor<?> packetPlayOutScoreboardScore;
    private final Class<?> enumScoreboardAction;

    /**
     * Create a new instance of the PacketScoreManager.
     *
     * @param       hashSidebar     The sidebar that will be managed by this PacketScoreManager.
     * @throws      Exception       If an error has occurred in the NMS package.
     */
    public PacketScoreManager(HashSidebar hashSidebar)
            throws Exception
    {
        this.sidebarId = hashSidebar.getId();
        this.packetPlayOutScoreboardScore = this.getNMSClass("PacketPlayOutScoreboardScore").getConstructor();
        this.enumScoreboardAction = this.getNMSClass("PacketPlayOutScoreboardScore.EnumScoreboardAction");
    }

    /**
     * Create a new PacketPlayOutScoreboardScore to manage the scores of the scoreboard.
     *
     * @param       mode        Either to set the score or to remove it.
     * @param       index       The index of the score. (Not important if the mode is REMOVE)
     * @param       value       The value of the score.
     * @return                  The corresponding PacketPlayOutScoreboardScore.
     * @throws      Exception   If an error has occurred in the NMS package.
     */
    public Object PacketPlayOutScoreboardScore(PacketScoreMode mode, int index, String value)
            throws Exception
    {
        @SuppressWarnings("unchecked")
        final Object modeValue = this.getEnumValue((Class<? extends Enum>) this.enumScoreboardAction, mode.getMode());
        final Object packet = this.packetPlayOutScoreboardScore.newInstance();

        setField(packet, "a", value);
        setField(packet, "b", sidebarId);
        setField(packet, "c", index);
        setField(packet, "d", modeValue);
        return packet;
    }

}
