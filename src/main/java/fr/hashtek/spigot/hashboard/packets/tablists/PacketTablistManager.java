package fr.hashtek.spigot.hashboard.packets.tablists;

import fr.hashtek.spigot.hashboard.packets.PacketManager;

import java.lang.reflect.Constructor;

public class PacketTablistManager extends PacketManager
{

    private final Constructor<?> packetPlayOutPlayerListHeaderFooter;

    /**
     * Create a new instance of the PacketTablistManager.
     *
     * @throws Exception If an error has occurred in the NMS package.
     */
    public PacketTablistManager()
            throws Exception
    {
        this.packetPlayOutPlayerListHeaderFooter = this.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor();
    }

    /**
     * Create a new PacketPlayOutPlayerListHeaderFooter to customize the tablist of a player.
     *
     * @param header The header of the tablist.
     * @param footer The footer of the tablist.
     * @return The corresponding PacketPlayOutPlayerListHeaderFooter packet.
     * @throws Exception If an error has occurred in the NMS package.
     */
    public Object PacketPlayOutPlayerListHeaderFooter(String header, String footer)
            throws Exception
    {
        final Object packet = this.packetPlayOutPlayerListHeaderFooter.newInstance();

        PacketManager.setField(packet, "a", header);
        PacketManager.setField(packet, "b", footer);
        return packet;
    }

}
