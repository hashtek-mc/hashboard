package fr.hashtek.spigot.hashboard.manager;

import fr.hashtek.spigot.hashboard.data.SidebarData;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SidebarManager
{

    private final ArrayList<PlayerConnection> receivers = new ArrayList<PlayerConnection>();
    private final String displayName;


    public SidebarManager(String displayName)
    {
        this.displayName = displayName;
    }

    public void addReceiver(Player player)
    {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;

        if (!this.receivers.contains(conn))
            this.receivers.add(conn);
    }

    public void removeReceiver(Player player)
    {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;

        this.receivers.remove(conn);
    }

    public void update()
    {
        for (PlayerConnection conn : this.receivers)
            conn.sendPacket();
    }

}