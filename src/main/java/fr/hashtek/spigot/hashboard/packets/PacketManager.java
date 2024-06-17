package fr.hashtek.spigot.hashboard.packets;

import fr.hashtek.hashutils.Reflection;

import java.lang.reflect.Field;

public class PacketManager extends Reflection
{

    public static void setField(Object packet, String fieldName, Object value)
    {
        Field field = null;

        try {
            field = packet.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(packet, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            // TODO: Use HashLogger to log the errors
        }
    }

}