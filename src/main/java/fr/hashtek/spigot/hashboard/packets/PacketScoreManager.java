package fr.hashtek.spigot.hashboard.packets;

import fr.hashtek.spigot.hashboard.sidebars.Sidebar;

import java.lang.reflect.Constructor;

public class PacketScoreManager extends PacketManager
{

    private final String sidebarId;
    private final Constructor<?> packetPlayOutScoreboardScore;

    public PacketScoreManager(Sidebar sidebar)
            throws Exception
    {
        this.sidebarId = sidebar.getId();
        this.packetPlayOutScoreboardScore = this.getNMSClass("packetPlayOutScoreboardScore").getConstructor();
    }

    public Object PacketPlayOutScoreboardScore()
    {
        return null;
    }

}
