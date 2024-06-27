package fr.hashtek.spigot.hashboard.packets;

import fr.hashtek.hashutils.Reflection;

import java.lang.reflect.Field;

public class PacketManager extends Reflection
{

    /**
     * Set a new value to a field.
     *
     * @param   packet      The packet instance.
     * @param   fieldName   The field name to edit.
     * @param   value       The new value to set.
     */
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