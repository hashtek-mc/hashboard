package fr.hashtek.spigot.hashboard;

import fr.hashtek.spigot.hashboard.exceptions.StrangeException;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * HashTabList is a class that allow you to manage and customize a tablist.
 * You can only customize the header and the footer.
 */
public class HashTabList
{

    private final PacketPlayOutPlayerListHeaderFooter packet;
    private String header;
    private String footer;


    /**
     * Create a tablist with a custom header and a custom footer.
     */
    public HashTabList()
    {
        this.packet = new PacketPlayOutPlayerListHeaderFooter();
    }

    /**
     * Update the tablist of the players.
     *
     * @param players The list of players to update the tablist to.
     * @throws StrangeException If the impossible happen. (See the exception message)
     */
    public void update(Collection<? extends Player> players)
        throws StrangeException
    {
        this.update(players.toArray(new Player[0]));
    }

    /**
     * Update the tablist of the players.
     *
     * @param players The list of players to update the tablist to.
     * @throws StrangeException If the impossible happen. (See the exception message)
     */
    public void update(Player... players)
        throws StrangeException
    {
        Class<?> packetClass = this.packet.getClass();
        CraftPlayer craftPlayer = null;

        this.setFieldValue(packetClass, "a", this.header);
        this.setFieldValue(packetClass, "b", this.footer);

        for (Player player : players) {
            craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().playerConnection.sendPacket(this.packet);
        }
    }

    /**
     * Set a new header to the tablist. (Require an update to see the changes)
     *
     * @param header The new header text.
     */
    public void setHeader(String header)
    {
        this.header = header;
    }

    /**
     * Set a new footer to the tablist. (Require an update to see the changes)
     *
     * @param footer The new footer text.
     */
    public void setFooter(String footer)
    {
        this.footer = footer;
    }

    /**
     * Set the value of a packet field.
     *
     * @param packetClass The class of the packet.
     * @param key The declared field name
     * @param value The new value to set to the field
     * @throws StrangeException If the impossible happen (See the exception message)
     */
    private void setFieldValue(Class<?> packetClass, String key, String value)
        throws StrangeException
    {
        Field field = null;

        try {
            field = packetClass.getDeclaredField(key);
            field.setAccessible(true);
            field.set(this.packet, new ChatComponentText(value));
        } catch (Exception exception) {
            throw new StrangeException(exception.getLocalizedMessage());
        }
    }

}
