package fr.hashtek.spigot.hashboard.sidebars;

import fr.hashtek.spigot.hashboard.objectives.ObjectiveMode;
import fr.hashtek.spigot.hashboard.objectives.ObjectiveSlot;
import fr.hashtek.spigot.hashboard.objectives.ObjectiveManager;
import fr.hashtek.spigot.hashboard.packets.PacketManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SidebarManager
{

    private static final HashMap<String, SidebarManager> sidebars = new HashMap<String, SidebarManager>();

    private final HashMap<PlayerConnection, Boolean> receivers = new HashMap<PlayerConnection, Boolean>();
    private final HashMap<Integer, SidebarLine> lines = new HashMap<Integer, SidebarLine>();
    private final String displayName;
    private final String id;


    public SidebarManager(String displayName, String id)
    {
        this.displayName = displayName;
        this.id = id;
        sidebars.put(this.id, this);
    }

    public void addReceiver(Player player)
    {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;

        if (!this.receivers.containsKey(conn))
            this.receivers.put(conn, false);
    }

    public void removeReceiver(Player player)
    {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;

        this.receivers.remove(conn);
    }

    public void update()
    {
        HashMap<Integer, PacketPlayOutScoreboardScore> linesPackets = this.getLinesPackets();

        this.receivers.forEach((conn, hasReceived) -> {
            if (!hasReceived) {
                this.create(conn);
                this.receivers.put(conn, true);
            }

        });
    }

    public void delete()
    {
        this.receivers.forEach((conn, hasReceived) -> {
            if (!hasReceived)
                return;
            conn.sendPacket();
        });
    }

    public static SidebarManager getSidebar(String id)
    {
        return sidebars.get(id);
    }

    private void create(PlayerConnection conn)
    {
        conn.sendPacket(ObjectiveManager.createObjective(ObjectiveMode.CREATE, this.id, this.displayName));
        conn.sendPacket(ObjectiveManager.getDisplayObjectiveSlot(ObjectiveSlot.SIDEBAR, this.id));
    }

    private HashMap<Integer, PacketPlayOutScoreboardScore> getLinesPackets()
    {
        HashMap<Integer, PacketPlayOutScoreboardScore> linesPackets = new HashMap<Integer, PacketPlayOutScoreboardScore>();

        this.lines.forEach((index, line) -> {
            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line.getValue());
            PacketManager.setField(packet, "b", this.id);
            PacketManager.setField(packet, "c", line.getValue());
            PacketManager.setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
        });

        return linesPackets;
    }

}