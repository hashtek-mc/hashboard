package fr.hashtek.spigot.hashboard.manager;

import fr.hashtek.spigot.hashboard.enums.ObjectiveMode;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;

import java.util.HashMap;

public class ObjectiveManager
{

    private static final HashMap<String, PacketPlayOutScoreboardObjective> objectives = new HashMap<String, PacketPlayOutScoreboardObjective>();

    public static PacketPlayOutScoreboardObjective getOrCreateObjective(ObjectiveMode mode, String displayName, String id)
    {
        PacketPlayOutScoreboardObjective packet = getOrCreateObjective(mode, id);

        PacketManager.setField(packet, "b", displayName);
        return packet;
    }

    /**
     * @param   mode    The objective mode.
     * @param   id      The unique identifier of the objective. (Use the player's name for example, or team name)
     * @return
     */
    public static PacketPlayOutScoreboardObjective getOrCreateObjective(ObjectiveMode mode, String id)
    {
        PacketPlayOutScoreboardObjective packet = null;

        if (objectives.containsKey(id))
            return objectives.get(id);

        packet = new PacketPlayOutScoreboardObjective();
        PacketManager.setField(packet, "a", id);
        PacketManager.setField(packet, "d", mode);

        switch (mode) {
            case CREATE:
            case EDIT:
                PacketManager.setField(packet, "b", "NULL");
                PacketManager.setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
            case DELETE:
                break;
        }
        objectives.put(id, packet);
        return packet;
    }

}