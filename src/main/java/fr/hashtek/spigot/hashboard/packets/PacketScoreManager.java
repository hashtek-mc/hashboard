package fr.hashtek.spigot.hashboard.packets;

import fr.hashtek.spigot.hashboard.sidebars.Sidebar;

import java.lang.reflect.Constructor;

public class PacketScoreManager extends PacketManager
{

    private final String sidebarId;
    private final Constructor<?> packetPlayOutScoreboardScore;
    private final Class<?> enumScoreboardAction;

    public PacketScoreManager(Sidebar sidebar)
            throws Exception
    {
        this.sidebarId = sidebar.getId();
        this.packetPlayOutScoreboardScore = this.getNMSClass("PacketPlayOutScoreboardScore").getConstructor();
        this.enumScoreboardAction = this.getNMSClass("PacketPlayOutScoreboardScore.EnumScoreboardAction");
    }

    public Object PacketPlayOutScoreboardScore(PacketScoreMode mode, int index, String value)
            throws Exception
    {
        @SuppressWarnings("unchecked")
        final Object modeValue = this.getEnumValue((Class<? extends Enum>) this.enumScoreboardAction, mode.get());
        final Object packet = this.packetPlayOutScoreboardScore.newInstance();

        setField(packet, "a", value);
        setField(packet, "b", sidebarId);
        setField(packet, "c", index);
        setField(packet, "d", modeValue);
        return packet;
    }

}
