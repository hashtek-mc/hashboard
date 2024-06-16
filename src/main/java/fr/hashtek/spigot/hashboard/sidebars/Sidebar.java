package fr.hashtek.spigot.hashboard.sidebars;

import fr.hashtek.hashutils.Reflection;
import fr.hashtek.spigot.hashboard.packets.PacketManager;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Sidebar extends Reflection
{

    private final String id;
    private final Constructor<?> packetPlayOutScoreboardObjective;
    private final HashMap<Integer, String> lines;

    public Sidebar(String id)
            throws ClassNotFoundException, NoSuchMethodException
    {
        this.id = id;
        this.packetPlayOutScoreboardObjective = super.getNMSClass("PacketPlayOutScoreboardObjective")
                .getDeclaredConstructor();
        this.lines = new HashMap<Integer, String>();
    }

    public void send(Player player)
            throws Exception
    {
        final Object objective = this.buildObjective();
        final Object playerConnection = this.getPlayerConnection(player);

        playerConnection.getClass().getMethod("sendPacket", super.getNMSClass("Packet"))
            .invoke(playerConnection, objective);
    }

    private Object buildObjective()
            throws Exception
    {
        final Object objective = this.packetPlayOutScoreboardObjective.newInstance();

        PacketManager.setField(objective, "a", id);
        PacketManager.setField(objective, "d", mode);

        switch (mode) {
            case CREATE:
            case EDIT:
                PacketManager.setField(packet, "b", "NULL");
                PacketManager.setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
            case DELETE:
                break;
        }
        return packet;
    }

    private Object getPlayerConnection(Player player)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException
    {
        final Object craftPlayer = player.getClass()
            .getMethod("getHandle").invoke(player);

        return craftPlayer.getClass()
            .getField("playerConnection").get(craftPlayer);
    }

}
